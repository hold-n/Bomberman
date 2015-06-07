package GameLogic.GameObjects;

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

    private boolean walking = false;
    private long walkStartTime;
    private long lastTime;
    private Image currentSprite = SpriteManager.getPlayerFront(0);

    private final double SPEED_CONST = (double)PLAYER_VELOCITY/1000000000L;

    private static final ArrayList<KeyCode> moveControls = new ArrayList<KeyCode>() { {
        add(KeyCode.UP);
        add(KeyCode.DOWN);
        add(KeyCode.LEFT);
        add(KeyCode.RIGHT);
    }};

    public Player(GameWindow window, double xpos, double ypos) {
        super(window, xpos, ypos);
        sizeY.setValue(PLAYER_HEIGHT);
        sizeX.setValue(PLAYER_WIDTH);
    }

    public void alterBombCount(int delta) {
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
    public Rectangle2D getBoundary() {
        return new Rectangle2D(x.getLogical(), y.getLogical(), PLAYER_WIDTH, PLAYER_WIDTH);
    }

    @Override
    public void update(long now) {
        double delta = (double)(now - lastTime)*SPEED_CONST;
        setX(getX() + getvelocityX()*delta);
        setY(getY() + getvelocityY()*delta);

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

            // TODO: alter the current sprite
        }
    }

    @Override
    public void checkCollisions() {
        // TODO: not to forget about borders
        // TODO: players checks evetything around him
    }

    @Override
    public boolean collides(FieldObject other) {
        if (!(other instanceof Explosion))
            return getBoundary().intersects(other.getBoundary());
        else {
            // TODO: explosion handling. or maybe make up smth to
            return false;
        }
    }

    @Override
    protected Image getSprite() {
        return currentSprite;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.drawImage(currentSprite, getX(), getY() - sizeY.getGraphic()/2);
    }

    public void PutBomb() {

    }

    public void Die() {
        // TODO: add some animation
        gameWindow.removePlayer(this);
    }
}
