package GameLogic.GameObjects;

import GameLogic.AnimatedSprite;
import GameLogic.GameObjects.Bonuses.Bonus;
import GameLogic.GameObjects.Bonuses.TemporaryBonus;
import GameLogic.GameWindow;
import GameLogic.MovementChecker;
import GameLogic.SpriteManager;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

import static GameLogic.Config.*;

/**
 * Created by Max on 05.06.2015.
 */

public class Player extends MoveableObject {
    PlayerType type;

    private int currentBombCount = 0;
    private int maxBombCount = INITIAL_BOMB_COUNT;
    private int tempMaxBombCount = INITIAL_BOMB_COUNT;
    private boolean useTempMaxBombCount = false;

    private long bombLifeTime = BOMB_LIFE_TIME;
    private long tempBombLifeTime = BOMB_LIFE_TIME;
    private boolean useTempBombLifeTime = false;

    private int explosionLength = INITIAL_EXPLOSION_LENGTH;
    private int tempExplosionLength = INITIAL_EXPLOSION_LENGTH;
    private boolean useTempExplosionLength = false;

    private double tempVelocityValue = PLAYER_VELOCITY;
    private boolean useTempVelocityValue = false;

    private final ArrayList<Bonus> bonuses = new ArrayList<Bonus>();
    private final ArrayList<Bonus> bonusesToRemove = new ArrayList<Bonus>();
    private final ArrayList<Bomb> bombs = new ArrayList<Bomb>();

    private boolean isDead = false;
    private long deathTime;
    private boolean canKick = false;
    private boolean bombSpawn = false;

    // TODO: make different sprites for different players
    AnimatedSprite frontSprite = new AnimatedSprite(SpriteManager::getPlayerFront, WALK_DURATION);
    AnimatedSprite backSprite = new AnimatedSprite(SpriteManager::getPlayerBack, WALK_DURATION);
    AnimatedSprite leftSprite = new AnimatedSprite(SpriteManager::getPlayerLeft, WALK_DURATION);
    AnimatedSprite rightSprite = new AnimatedSprite(SpriteManager::getPlayerRight, WALK_DURATION);
    private Image currentSprite = SpriteManager.getPlayerFront(0);

    private static final double HALF_GRAPHIC_HEIGHT = PLAYER_HEIGHT * GLRATIO / 2;

    public PlayerType getType() {
        return type;
    }

    public int getCurrentBombCount() {
        return currentBombCount;
    }
    public int getMaxBombCount() {
        return useTempMaxBombCount ? tempMaxBombCount : maxBombCount;
    }
    public void setMaxBombCount(int value) {
        if (value >= 0)
            maxBombCount = value;
    }
    public void setTempMaxBombCount(int value) {
        if (value >= 0)
            tempMaxBombCount = value;
    }
    public void setUseTempMaxBombCount(boolean value) {
        useTempMaxBombCount = value;
    }
    public void increaseMaxBombCount() {
        maxBombCount++;
    }
    public void decreaseMaxBombCount() {
        if (maxBombCount > 0)
            maxBombCount--;
    }

    public long getBombLifeTime() {
        return useTempBombLifeTime ? tempBombLifeTime : bombLifeTime;
    }
    public void setBombLifeTime(long value) {
        if (value > 0)
            bombLifeTime = 0;
    }
    public void setTempBombLifeTime(long value) {
        if (value > 0)
            tempBombLifeTime = value;
    }
    public void setUseTempBombLifeTime(boolean value) {
        useTempBombLifeTime = value;
    }

    public int getExplosionLength() {
        return useTempExplosionLength ? tempExplosionLength : explosionLength;
    }
    public void setExplosionLength(int value) {
        if (value >= 0)
            explosionLength = value;
    }
    public void setTempExplosionLength(int value) {
        if (value >= 0)
            tempExplosionLength = value;
    }
    public void setUseTempExplosionLength(boolean value) {
        useTempExplosionLength = value;}
    public void increaseExplosionLength() {
        explosionLength++;
    }
    public void decreaseExplosionLength() {
        if (explosionLength > 1)
            explosionLength--;
    }

    @Override
    public double getVelocityValue() {
        return useTempVelocityValue ? tempVelocityValue : velocityValue;
    }

    public void setTempVelocityValue(double value) {
        tempVelocityValue = value;
    }
    public void setUseTempVelocityValue(boolean value) {
        useTempVelocityValue = value;
    }

    public void setKick(boolean value) {
        canKick = value;
    }
    public void setBombSpawn(boolean value) {
        bombSpawn = value;
    }

    public Player(GameWindow window, PlayerType playerType, double xpos, double ypos) {
        super(window, PLAYER_VELOCITY, PLAYER_VELOCITY_DELTA, xpos, ypos);
        sizeY.setValue(PLAYER_WIDTH);
        sizeX.setValue(PLAYER_WIDTH);
        type = playerType;
    }

    @Override
    public void update(long now) {
        move(now);
        considerBonuses(now);

        for (Bonus bonus : bonusesToRemove) {
            bonuses.remove(bonus);
        }
        bonusesToRemove.clear();

        if (bombSpawn)
            putBomb();

        if (moving) {
            switch (direction) {
                case UP:
                    currentSprite = backSprite.getSprite(now);
                    break;
                case DOWN:
                    currentSprite = frontSprite.getSprite(now);
                    break;
                case LEFT:
                    currentSprite = leftSprite.getSprite(now);
                    break;
                case RIGHT:
                    currentSprite = rightSprite.getSprite(now);
                    break;
            }
        }
        lastTime = now;
    }

    @Override
    public void startAnimation(long now) {
        switch (direction) {
            case UP:
                backSprite.setStart(now);
                return;
            case DOWN:
                frontSprite.setStart(now);
                return;
            case LEFT:
                leftSprite.setStart(now);
                return;
            case RIGHT:
                rightSprite.setStart(now);
                return;
        }
    }

    private void considerBonuses(long now) {
        for (Bonus bonus : bonuses) {
            if (bonus instanceof TemporaryBonus) {
                TemporaryBonus tempBonus = (TemporaryBonus)bonus;
                if (now - tempBonus.getPickUpTime() > tempBonus.getDuration()) {
                    bonus.discard(this);
                    bonusesToRemove.add(bonus);
                }
            }
        }
    }

    @Override
    public void checkCollisions() {
        checkBorders();
        for (Bomb bomb : gameWindow.getBombs()) {
            if (collides(bomb)) {
                if (canKick && isMoving() && MovementChecker.orientation(this, bomb) == direction) {
                    if (!MovementChecker.collidesStatic(this, bomb))
                        bomb.kick(direction);
                }
                MovementChecker.tryStop(bomb, this, false);
            }
        }
        for (Bonus bonus : gameWindow.getBonuses()) {
            if (collides(bonus)) {
                bonus.apply(this);
                bonuses.add(bonus);
                bonus.removeFromField();
            }
        }
    }

    public boolean putBomb() {
        if (currentBombCount < getMaxBombCount()) {
            Bomb bomb = new Bomb(gameWindow, this, getX(), getY() + BOMB_SHIFT);
            if (!MovementChecker.willCollideAny(bomb, gameWindow.getBombs())) {
                currentBombCount++;
                gameWindow.addObject(bomb);
                bombs.add(bomb);
                return true;
            }
        }
        return false;
    }

    public void removeBomb(Bomb bomb) {
        if (bomb != null) {
            bombs.remove(bomb);
            currentBombCount--;
        }
        else
            throw new NullPointerException();
    }

    public void Die() {
        // TODO: add some animation, perhaps a dead player class
        gameWindow.removeObject(this);
    }

    @Override
    public Rectangle2D getBoundary() {
        return new Rectangle2D(x.getLogical(), y.getLogical(), PLAYER_WIDTH, PLAYER_WIDTH);
    }

    @Override
    protected Image getSprite() {
        return currentSprite;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.drawImage(currentSprite, x.getGraphic(), y.getGraphic() - HALF_GRAPHIC_HEIGHT);
    }

    @Override
    public void explode() {
        Die();
    }

    @Override
    public void teleport(double x, double y) {
        // TODO: add teleportation animation and delay
        setX(x);
        setY(y);
    }
}