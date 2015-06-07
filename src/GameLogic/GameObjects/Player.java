package GameLogic.GameObjects;

import GameLogic.GameObjects.Bonuses.Bonus;
import GameLogic.GameObjects.Tiles.SolidTile;
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
    private int explosionLength = 2;
    private ArrayList<Bonus> bonuses = new ArrayList<Bonus>();

    private boolean walking = false;
    private long walkStartTime;

    private long lastTime;
    private Image currentSprite = SpriteManager.getPlayerFront(0);

    private final double SPEED_CONST = (double)PLAYER_VELOCITY/1000000000L;
    private final double CRITICAL_X = LOGICAL_WIDTH - PLAYER_WIDTH;
    private final double CRITICAL_Y = LOGICAL_HEIGHT - PLAYER_WIDTH;
    private final double HALF_GRAPHIC_HEIGHT = PLAYER_HEIGHT * GLRATIO / 2;

    // TODO: Make a collection of collections for more players
    private static final ArrayList<KeyCode> moveControls = new ArrayList<KeyCode>() { {
        add(KeyCode.UP);
        add(KeyCode.DOWN);
        add(KeyCode.LEFT);
        add(KeyCode.RIGHT);
    }};

    public Player(GameWindow window, double xpos, double ypos) {
        super(window, xpos, ypos);
        sizeY.setValue(PLAYER_WIDTH);
        sizeX.setValue(PLAYER_WIDTH);
        objectCollection = window.getObjects();
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
        double delta = (double)(now - lastTime)*SPEED_CONST;
        setX(getX() + getVelocityX()*delta);
        setY(getY() + getVelocityY()*delta);

        for (KeyCode code : gameWindow.getCodes()) {
            if (code == KeyCode.SPACE) {
                if (currentBombCount < maxBombCount) {
                    currentBombCount++;
                    PutBomb();
                }
                break;
            }
        }

        KeyCode movingCode = null;
        for (KeyCode code : gameWindow.getCodes()) {
            if (moveControls.contains(code)) {
                if (code == KeyCode.DOWN) {
                    setVelocityY(PLAYER_VELOCITY);
                    setVelocityX(0);
                }
                if (code == KeyCode.UP) {
                    setVelocityY(-PLAYER_VELOCITY);
                    setVelocityX(0);
                }
                if (code == KeyCode.LEFT) {
                    setVelocityX(-PLAYER_VELOCITY);
                    setVelocityY(0);
                }
                if (code == KeyCode.RIGHT) {
                    setVelocityX(PLAYER_VELOCITY);
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
        }
        else {
            if (!walking) {
                walkStartTime = System.nanoTime();
                walking = true;
            }
            if (movingCode == KeyCode.DOWN)
                currentSprite = SpriteManager.getPlayerFront((int)((lastTime - walkStartTime)/WALK_DURATION));
            if (movingCode == KeyCode.UP)
                currentSprite = SpriteManager.getPlayerBack((int) ((lastTime - walkStartTime) / WALK_DURATION));
            if (movingCode == KeyCode.LEFT)
                currentSprite = SpriteManager.getPlayerLeft((int) ((lastTime - walkStartTime) / WALK_DURATION));
            if (movingCode == KeyCode.RIGHT)
                currentSprite = SpriteManager.getPlayerRight((int) ((lastTime - walkStartTime)/WALK_DURATION));
        }
    }

    @Override
    public void checkCollisions() {
        CheckBorders();
        for (FieldObject obj : objectCollection) {
            if (collides(obj)) {
                if (obj instanceof SolidTile) {
                    ((Tile) obj).effect(this);
                }
            }
            // TODO: players checks evetything around him
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

    public void PutBomb() {
        // TODO: put in a current cell, not just below
    }

    public void Die() {
        // TODO: add some animation
        gameWindow.removePlayer(this);
    }
}
