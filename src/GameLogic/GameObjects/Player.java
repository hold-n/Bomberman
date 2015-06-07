package GameLogic.GameObjects;

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
    private int maxBombCount = 1;
    private int currentBombCount = 0;
    private double explosionLength = 2 * TILE_LOGICAL_SIZE;
    private double velocityValue = PLAYER_VELOCITY;
    private ArrayList<Bonus> bonuses = new ArrayList<Bonus>();
    private ArrayList<Bomb> bombs = new ArrayList<Bomb>();

    private boolean walking = false;
    private boolean dead = false;
    private long walkStartTime;

    private long lastTime;
    private Image currentSprite = SpriteManager.getPlayerFront(0);

    private static final double SPEED_CONST = (double) PLAYER_VELOCITY / 1000000000L;
    private static final double CRITICAL_X = LOGICAL_WIDTH - PLAYER_WIDTH;
    private static final double CRITICAL_Y = LOGICAL_HEIGHT - PLAYER_WIDTH;
    private static final double HALF_GRAPHIC_HEIGHT = PLAYER_HEIGHT * GLRATIO / 2;

    // TODO: Make a collection of collections for more players
    private static final ArrayList<KeyCode> moveControls = new ArrayList<KeyCode>() {
        {
            add(KeyCode.UP);
            add(KeyCode.DOWN);
            add(KeyCode.LEFT);
            add(KeyCode.RIGHT);
        }
    };

    // TODO: add getters
    public void removeBomb(Bomb bomb) {
        bombs.remove(bomb);
        currentBombCount--;
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
            if (code == KeyCode.SPACE) {
                tryPutBomb();
                break;
            }
        }

        KeyCode movingCode = null;
        for (KeyCode code : gameWindow.getCodes()) {
            if (moveControls.contains(code)) {
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
        // TODO; check for other objects
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
        if (!(other instanceof Explosion))
            return getBoundary().intersects(other.getBoundary());
        else {
            // TODO: explosion handling. or maybe make up smth
            return false;
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
            currentBombCount++;
            Bomb bomb = new Bomb(gameWindow, this, getX(), getY());
            applyBonuses(bomb);
            gameWindow.addObject(bomb);
            bombs.add(bomb);
            return true;
        }
        else {
            return false;
        }
    }

    public void applyBonuses(Bomb bomb) {
        // TODO
    }

    public void Die() {
        // TODO: add some animation
        gameWindow.removeObject(this);
    }
}