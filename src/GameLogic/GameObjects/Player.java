package GameLogic.GameObjects;

import GameLogic.AnimatedSprite;
import GameLogic.GameObjects.Bonuses.Bonus;
import GameLogic.GameWindow;
import GameLogic.MovementChecker;
import GameLogic.SpriteManager;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import java.util.ArrayList;

import static GameLogic.Config.*;

/**
 * Created by Max on 05.06.2015.
 */

public class Player extends FieldObject {
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

    private double velocityValue = PLAYER_VELOCITY;
    private double tempVelocityValue = PLAYER_VELOCITY;
    private boolean useTempVelocityValue = false;

    private final ArrayList<Bonus> bonuses = new ArrayList<Bonus>();
    private final ArrayList<Bomb> bombs = new ArrayList<Bomb>();

    private boolean walking = false;
    private boolean isDead = false;
    private long deathTime;
    private boolean canKick = false;
    private boolean bombSpawn = false;

    Direction walkDirection = Direction.DOWN;
    AnimatedSprite frontSprite = new AnimatedSprite(SpriteManager::getPlayerFront, WALK_DURATION);
    AnimatedSprite backSprite = new AnimatedSprite(SpriteManager::getPlayerBack, WALK_DURATION);
    AnimatedSprite leftSprite = new AnimatedSprite(SpriteManager::getPlayerLeft, WALK_DURATION);
    AnimatedSprite rightSprite = new AnimatedSprite(SpriteManager::getPlayerRight, WALK_DURATION);
    private Image currentSprite = SpriteManager.getPlayerFront(0);

    private static final double HALF_GRAPHIC_HEIGHT = PLAYER_HEIGHT * GLRATIO / 2;

    // Controls: up, down, left, right
    private static final ArrayList<KeyCode> moveControls1 = new ArrayList<KeyCode>() {{
            add(KeyCode.UP);
            add(KeyCode.DOWN);
            add(KeyCode.LEFT);
            add(KeyCode.RIGHT);
    }};
    private static final KeyCode plantKey1 = KeyCode.SPACE;
    private static final ArrayList<KeyCode> getMoveControls2 = new ArrayList<KeyCode>() {{
        add(KeyCode.W);
        add(KeyCode.S);
        add(KeyCode.A);
        add(KeyCode.D);
    }};
    private static final KeyCode plantKey2 = KeyCode.SHIFT;

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

    public double getVelocityValue() {
        return velocityValue;
    }
    public void setVelocityValue(double value) {
        velocityValue = value;
    }
    public void setTempVelocityValue(double value) {
        tempVelocityValue = value;
    }
    public void setUseTempVelocityValue(boolean value) {
        useTempVelocityValue = value;
    }
    public void increaseVelocity() {
        velocityValue += PLAYER_VELOCITY_DELTA;
    }
    public void decreaseVelocity() {
        velocityValue -= PLAYER_VELOCITY_DELTA;
    }

    public void setKick(boolean value) {
        canKick = value;
    }
    public void setBombSpawn(boolean value) {
        bombSpawn = value;
    }

    public Direction getWalkDirection() {
        return walkDirection;
    }
    public void setWalkDirection(Direction direction) {
        walkDirection = direction;
    }

    public Player(GameWindow window, PlayerType playerType, double xpos, double ypos) {
        super(window, xpos, ypos);
        sizeY.setValue(PLAYER_WIDTH);
        sizeX.setValue(PLAYER_WIDTH);
        type = playerType;
        lastTime = System.nanoTime();
    }

    @Override
    public void update(long now) {
        move(now);
        // TODO: manage temporary bonuses
        if (bombSpawn)
            putBomb();
        else {
            for (KeyCode code : gameWindow.getCodes()) {
                if (code == plantKey1) {
                    putBomb();
                    break;
                }
            }
        }
        walking = isMoving();
        if (walking) {
            switch (walkDirection) {
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

    public void walkByX(boolean positive, long now) {
        if (positive) {
            if (!walking)
                rightSprite.setStart(now);
            walkDirection = Direction.RIGHT;
        }
        else {
            if (!walking)
                leftSprite.setStart(now);
            walkDirection = Direction.LEFT;
        }
        setVelocityX(positive ? getVelocityValue() : -getVelocityValue());
        setVelocityY(0);
    }

    public void walkByY(boolean positive, long now) {
        if (positive) {
            if (!walking)
                frontSprite.setStart(now);
            walkDirection = Direction.DOWN;
        }
        else {
            if (!walking)
                backSprite.setStart(now);
            walkDirection = Direction.UP;
        }
        setVelocityY(positive ? getVelocityValue() : -getVelocityValue());
        setVelocityX(0);
    }

    public void stop() {
        setVelocityX(0);
        setVelocityY(0);
    }

    @Override
    public void checkCollisions() {
        checkBorders();
        for (Bomb bomb : gameWindow.getBombs()) {
            if (collides(bomb)) {
//                if (canKick && bomb.isMoving()) {
////                TODO: kicking in the right direction
////                bomb.kick();
//                }
//                else {
//                    MovementChecker.tryStop(bomb, this);
//                }
                MovementChecker.tryStop(bomb, this, false);
            }
        }
        for (Bonus bonus : gameWindow.getBonuses()) {
            if (collides(bonus)) {
                bonus.apply(this);
                bonuses.add(bonus);
                bonus.remove();
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