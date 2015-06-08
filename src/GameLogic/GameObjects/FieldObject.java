package GameLogic.GameObjects;

import GameLogic.GameValue;
import GameLogic.GameWindow;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Created by Max on 03.06.2015.
 */

public abstract class FieldObject {
    protected GameValue x = new GameValue(0);
    protected GameValue y = new GameValue(0);
    protected GameValue sizeX = new GameValue(0);
    protected GameValue sizeY = new GameValue(0);
    protected GameValue velocityX = new GameValue(0);
    protected GameValue velocityY = new GameValue(0);

    protected GameWindow gameWindow;

    public double getX() { return x.getLogical(); }
    public double getY() { return y.getLogical(); }
    public double getSizeX() { return sizeX.getLogical(); }
    public double getSizeY() { return sizeY.getLogical(); }
    public double getVelocityX() { return velocityX.getLogical(); }
    public double getVelocityY() { return velocityY.getLogical(); }

    public void setX(double value) { x.setValue(value); }
    public void setY(double value) { y.setValue(value); }
    public void shiftX(double value) { x.add(value); }
    public void shiftY(double value) { y.add(value); }
    public void setSizeX(double value) { sizeX.setValue(value); }
    public void setSizeY(double value) { sizeY.setValue(value); }
    public void setVelocityX(double value) { velocityX.setValue(value); }
    public void setVelocityY(double value) { velocityY.setValue(value); }

    public FieldObject(GameWindow window, double xpos, double ypos) {
        gameWindow = window;
        x.setValue(xpos);
        y.setValue(ypos);
    }

    public Rectangle2D getBoundary() {
        return null;
    }
    public boolean collides(FieldObject other) {
        return false;
    }
    protected Image getSprite() { return null; }
    public void update(long now) {}
    // only active objects check for collisions
    public void checkCollisions() {}
    public abstract void draw(GraphicsContext context);
    public void explode() {}
}