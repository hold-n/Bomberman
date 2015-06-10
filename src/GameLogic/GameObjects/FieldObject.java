package GameLogic.GameObjects;

import GameLogic.GameValue;
import GameLogic.GameWindow;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Max on 03.06.2015.
 */

public abstract class FieldObject implements Serializable {
    protected GameValue x = new GameValue();
    protected GameValue y = new GameValue();
    protected GameValue sizeX = new GameValue();
    protected GameValue sizeY = new GameValue();

    protected GameWindow gameWindow;
    protected transient long creationTime;
    protected long lifeTime;

    public GameWindow getGameWindow() {
        return gameWindow;
    }
    public long getLifeTime() {
        return lifeTime;
    }

    public double getX() { return x.getLogical(); }
    public double getY() { return y.getLogical(); }
    public double getSizeX() { return sizeX.getLogical(); }
    public double getSizeY() { return sizeY.getLogical(); }

    public void setX(double value) { x.setValue(value); }
    public void setY(double value) { y.setValue(value); }
    public void shiftX(double value) { x.add(value); }
    public void shiftY(double value) { y.add(value); }
    public void setSizeX(double value) { sizeX.setValue(value); }
    public void setSizeY(double value) { sizeY.setValue(value); }


    public FieldObject(GameWindow window, double xpos, double ypos) {
        gameWindow = window;
        x.setValue(xpos);
        y.setValue(ypos);
        creationTime = System.nanoTime();
    }

    /**
     * Returns the border of the object for collision checks or null,
     * if it's impossible to interact with the object
     *
     * @return The border of the object
     */
    public Rectangle2D getBoundary() {
        return null;
    }

    /**
     * Whether the object collides with the other object.
     * Should be called as to determine whether to call collision handlers
     * @param other Object to check for collision
     * @return Whether the object is touching another object
     */
    public boolean collides(FieldObject other) {
        if (other instanceof Explosion)
            return other.collides(this);
        if (other.getBoundary() == null || getBoundary() == null)
            return false;
        return getBoundary().intersects(other.getBoundary());
    }

    /**
     * Returns the current sprite of the object.
     * @return The current sprite
     */
    protected Image getSprite() { return null; }

    /**
     * Called on every iteration of the game loop so the object can update its time-dependant parameters.
     * @param now Current time in nanoseconds
     */
    public void update(long now) {
        lifeTime = now - creationTime;
    }

    /**
     * Called on every iteration of the game loop so the object can check any other objects for collisions.
     * For better performance, only active objects override this method.
     */
    public void checkCollisions() {}

    /**
     * Called on every iteration of the game loop so the object can draw itself.
     * @param context Context to perform drawing on
     */
    public void draw(GraphicsContext context) {
        context.drawImage(getSprite(), x.getGraphic(), y.getGraphic());
    }

    /**
     * Method called by explosion objects so as to try to destroy objects it touches on creation
     */
    public void explode() {}


    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        creationTime = System.nanoTime() - lifeTime;
    }
}