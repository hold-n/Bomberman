package GameLogic.GameObjects;

import GameLogic.AnimatedSprite;
import GameLogic.GameObjects.Bonuses.Bonus;
import GameLogic.GameObjects.Tiles.Tile;
import GameLogic.GameWindow;
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

    // TODO: remove this field
    private int currentBombCount = 0;

    private final ArrayList<Bonus> bonuses = new ArrayList<Bonus>();
    private final ArrayList<Bomb> bombs = new ArrayList<Bomb>();

    private boolean walking = false;
    private long walkStartTime;
    private boolean isDead = false;
    private long deathTime;
    private boolean canKick = false;

    private long lastTime;
    AnimatedSprite frontSprite = new AnimatedSprite(SpriteManager::getPlayerFront, WALK_DURATION);
    AnimatedSprite backSprite = new AnimatedSprite(SpriteManager::getPlayerBack, WALK_DURATION);
    AnimatedSprite leftSprite = new AnimatedSprite(SpriteManager::getPlayerLeft, WALK_DURATION);
    AnimatedSprite rightSprite = new AnimatedSprite(SpriteManager::getPlayerRight, WALK_DURATION);
    private Image currentSprite = SpriteManager.getPlayerFront(0);

    private static final double SPEED_CONST = PLAYER_VELOCITY / 1000000000L;
    private static final double CRITICAL_X = LOGICAL_WIDTH - PLAYER_WIDTH;
    private static final double CRITICAL_Y = LOGICAL_HEIGHT - PLAYER_WIDTH;
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

    public Player(GameWindow window, PlayerType playerType, double xpos, double ypos) {
        super(window, xpos, ypos);
        sizeY.setValue(PLAYER_WIDTH);
        sizeX.setValue(PLAYER_WIDTH);
        type = playerType;
    }

    @Override
    public void update(long now) {
        Move(now);
        // TODO: manage input on another level
        // TODO: manage temporary bonuses
        // TODO: alter control handling to allow multiple players
        for (KeyCode code : gameWindow.getCodes()) {
            if (code == plantKey1) {
                tryPutBomb();
                break;
            }
        }

        KeyCode movingCode = null;
        for (KeyCode code : gameWindow.getCodes()) {
            if (moveControls1.contains(code)) {
                if (code == KeyCode.DOWN) {
                    restoreVelocityY(true);
                    setVelocityX(0);
                }
                if (code == KeyCode.UP) {
                    restoreVelocityY(false);
                    setVelocityX(0);
                }
                if (code == KeyCode.LEFT) {
                    restoreVelocityX(false);
                    setVelocityY(0);
                }
                if (code == KeyCode.RIGHT) {
                    restoreVelocityX(true);
                    setVelocityY(0);
                }
                movingCode = code;
                break;
            }
        }

        lastTime = System.nanoTime();
        if (movingCode == null) {
            velocityY.setValue(0);
            velocityX.setValue(0);
            walking = false;
        } else {
            if (!walking) {
                walkStartTime = System.nanoTime();
                walking = true;
            }
            if (movingCode == KeyCode.DOWN)
                currentSprite = SpriteManager.getPlayerFront((int) ((lastTime - walkStartTime) / WALK_DURATION));
            if (movingCode == KeyCode.UP)
                currentSprite = SpriteManager.getPlayerBack((int) ((lastTime - walkStartTime) / WALK_DURATION));
            if (movingCode == KeyCode.LEFT)
                currentSprite = SpriteManager.getPlayerLeft((int) ((lastTime - walkStartTime) / WALK_DURATION));
            if (movingCode == KeyCode.RIGHT)
                currentSprite = SpriteManager.getPlayerRight((int) ((lastTime - walkStartTime) / WALK_DURATION));
        }
    }



    private void restoreVelocityX(boolean positive) {
        velocityX.setValue(positive ? velocityValue : -velocityValue);
    }
    private void restoreVelocityY(boolean positive) {
        velocityY.setValue(positive ? velocityValue : -velocityValue);
    }

    private void Move(long now) {
        double delta = (double) (now - lastTime) * SPEED_CONST;
        setX(getX() + getVelocityX() * delta);
        setY(getY() + getVelocityY() * delta);
    }

    @Override
    public void checkCollisions() {
        CheckBorders();
        for (Tile tile : gameWindow.getMap()) {
            if (collides(tile)) {
                tile.effect(this);
            }
        }
        for (Bonus bonus : gameWindow.getBonuses()) {
            if (collides(bonus)) {
                bonus.apply(this);
                bonuses.add(bonus);
                bonus.remove();
            }
        }
        for (Bomb bomb : gameWindow.getBombs()) {
            if (collides(bomb)) {
                if (canKick) {
                    kickBomb(bomb);
                }
                else {
                    tryStop(bomb);
                }
            }
        }
        for (Explosion explosion : gameWindow.getExplosions()) {
            if (collides(explosion)) {
                explode();
            }
        }
    }

    private void kickBomb(Bomb bomb) {

    }

    private void CheckBorders() {
        if (getX() < 0 && getVelocityX() < 0)
            setVelocityX(0);
        if (getX() > CRITICAL_X && getVelocityX() > 0)
            setVelocityX(0);
        if (getY() < 0 && getVelocityY() < 0)
            setVelocityY(0);
        if (getY() > CRITICAL_Y && getVelocityY() > 0)
            setVelocityY(0);
    }

    public void tryStop(FieldObject obj) {
        // TODO: fix object evasion, just try to stop
        double deltaX = getX() - obj.getX();
        double deltaY = getY() - obj.getY();
        double overlapX = deltaX > 0 ? obj.getSizeX() - deltaX : getSizeX() + deltaX;
        double overlapY = deltaY > 0 ? obj.getSizeY() - deltaY : getSizeY() + deltaY;

        // no check for overlap > 0, assuming method called on collision
        if (overlapY < MAX_OVERLAP && overlapX > MAX_OVERLAP) {
            if (getVelocityY() * deltaY < 0) {
                if (overlapX < STRAFE_RADIUS) {
                    if (!anythingStops())
                        shiftX(Math.signum(deltaX)*STRAFE_STEP);
                }
                setVelocityY(0);
            }
        }
        if (overlapX < MAX_OVERLAP && overlapY > MAX_OVERLAP) {
            if (getVelocityX() * deltaX < 0) {
                if (overlapY < STRAFE_RADIUS) {
                    shiftY(Math.signum(deltaY)*STRAFE_STEP);
                }
                setVelocityX(0);
            }
        }
    }

    protected boolean anythingStops() {
        return false;
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

    public boolean tryPutBomb() {
        if (currentBombCount < maxBombCount) {
            boolean willTouchBomb = false;
            for (Bomb bomb : gameWindow.getBombs()) {
                double deltaX = getX() - bomb.getX();
                double deltaY = getY() + BOMB_SHIFT - bomb.getY();
                double overlapX = deltaX > 0 ? bomb.getSizeX() - deltaX : BOMB_SIZE + deltaX;
                double overlapY = deltaY > 0 ? bomb.getSizeY() - deltaY : BOMB_SIZE + deltaY;
                if (overlapX > MAX_OVERLAP && overlapY > MAX_OVERLAP) {
                    willTouchBomb = true;
                    break;
                }
            }
            if (!willTouchBomb) {
                currentBombCount++;
                Bomb bomb = new Bomb(gameWindow, this, getX(), getY() + BOMB_SHIFT);
                applyBonuses(bomb);
                gameWindow.addObject(bomb);
                bombs.add(bomb);
                return true;
            }
            return false;
        }
        else {
            return false;
        }
    }


    public void removeBomb(Bomb bomb) {
        bombs.remove(bomb);
        currentBombCount--;
    }

    public void applyBonuses(Bomb bomb) {
        // TODO
    }

    @Override
    public void explode() {
        Die();
    }

    public void Die() {
        // TODO: add some animation, perhaps a dead player class
        gameWindow.removeObject(this);
    }

    public void teleport(double x, double y) {
        // TODO: add teleportation animation
        setX(x);
        setY(y);
    }

}