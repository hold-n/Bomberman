package GameLogic.GameObjects;

import GameLogic.GameObjects.Bonuses.Bonus;
import GameLogic.GameObjects.Tiles.ImpassableTile;
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
    private int maxBombCount = 2;
    private int currentBombCount = 0;
    private int explosionLength = 2;
    private double velocityValue = PLAYER_VELOCITY;
    private ArrayList<Bonus> bonuses = new ArrayList<Bonus>();
    private ArrayList<Bomb> bombs = new ArrayList<Bomb>();

    private boolean walking = false;
    private boolean dead = false;
    private long walkStartTime;

    private long lastTime;
    private Image currentSprite = SpriteManager.getPlayerFront(0);

    private static final double SPEED_CONST = PLAYER_VELOCITY / 1000000000L;
    private static final double CRITICAL_X = LOGICAL_WIDTH - PLAYER_WIDTH;
    private static final double CRITICAL_Y = LOGICAL_HEIGHT - PLAYER_WIDTH;
    private static final double HALF_GRAPHIC_HEIGHT = PLAYER_HEIGHT * GLRATIO / 2;
    // TODO: maybe keep track of available directions to go?

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

    // TODO: add getters
    public void removeBomb(Bomb bomb) {
        bombs.remove(bomb);
        currentBombCount--;
    }

    public int getMaxBombCount() {
        return maxBombCount;
    }
    public void setMaxBombCount(int value) {
        if (value >= 0)
            maxBombCount = value;
    }
    public void increaseMaxBombCoune() {
        maxBombCount++;
    }
    public void decreaseMaxBombCoune() {
        if (maxBombCount > 0)
            maxBombCount--;
    }
    public int getExplosionLength() {
        return explosionLength;
    }
    public void setExplosionLength(int value) {
        if (value >= 0)
            explosionLength = value;
    }
    public void increaseExplosionLength() {
        explosionLength++;
    }
    public void decreaseExplosionLength() {
        if (explosionLength > 2)
            explosionLength--;
    }


    public void restoreVelocityX(boolean positive) {
        velocityX.setValue(positive ? velocityValue : -velocityValue);
    }
    public void restoreVelocityY(boolean positive) {
        velocityY.setValue(positive ? velocityValue : -velocityValue);
    }
    public void increaseVelocityX(double value) {
        velocityX.add(value);
    }
    public void increaseVelocityX() {
        velocityX.add(PLAYER_VELOCITY_DELTA);
    }
    public void increaseVelocityY(double value) {
        velocityY.add(value);
    }
    public void increaseVelocityY() {
        velocityY.add(PLAYER_VELOCITY_DELTA);
    }

    public Player(GameWindow window, double xpos, double ypos) {
        super(window, xpos, ypos);
        sizeY.setValue(PLAYER_WIDTH);
        sizeX.setValue(PLAYER_WIDTH);
    }

    public void alterMaxBombCount(int delta) {
        if (maxBombCount + delta > 1)
            maxBombCount = maxBombCount + delta;
        else
            maxBombCount = 1;
    }

    public void alterExplosionLength(int delta) {
        if (explosionLength + delta > 1)
            explosionLength = explosionLength + delta;
        else
            explosionLength = 1;
    }

    @Override
    public void update(long now) {
        double delta = (double) (now - lastTime) * SPEED_CONST;
        setX(getX() + getVelocityX() * delta);
        setY(getY() + getVelocityY() * delta);
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
            }
        }
        for (Bomb bomb : gameWindow.getBombs()) {
            if (collides(bomb)) {
                // TODO: Kicking bombs bonus implementation
                tryStop(bomb);
            }
        }
        for (Explosion explosion : gameWindow.getExplosions()) {
            if (collides(explosion)) {
                Die();
            }
        }
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

    @Override
    public boolean collides(FieldObject other) {
        if (other instanceof  Explosion)
            return other.collides(this);
        return getBoundary().intersects(other.getBoundary());
    }

    public void tryStop(FieldObject obj) {
        // TODO: fix object evasion
        double deltaX = getX() - obj.getX();
        double deltaY = getY() - obj.getY();
        double overlapX = deltaX > 0 ? obj.getSizeX() - deltaX : getSizeX() + deltaX;
        double overlapY = deltaY > 0 ? obj.getSizeY() - deltaY : getSizeY() + deltaY;

        // no check for overlap > 0, assuming method called on collision
        if (overlapY < MAX_OVERLAP && overlapX > MAX_OVERLAP) {
            if (getVelocityY() * deltaY < 0) {
                if (overlapX < STRAFE_RADIUS) {
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

    public void applyBonuses(Bomb bomb) {
        // TODO
    }

    @Override
    public void explode() {
        Die();
    }

    public void Die() {
        // TODO: add some animation
        gameWindow.removeObject(this);
    }
}