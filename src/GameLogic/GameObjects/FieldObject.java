package GameLogic.GameObjects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Shape;

/**
 * Created by Max on 03.06.2015.
 */
public abstract class FieldObject {
    protected double x, y;
    protected double velocityX = 0, velocityY = 0;

    public double getX() { return x; }
    public double getY() { return y; }
    public double getvelocityX() { return velocityX; }
    public double getvelocityY() { return velocityY; }
    public void setX(double value) { x = value; }
    public void setY(double value) { y = value; }
    public void setVelocityX(double value) { velocityX = value; }
    public void setVelocityY(double value) { velocityY = value; }

    public boolean intersects(FieldObject obj) {
        return false;
    }

    public void update(long now) {}

    public abstract Shape getBoundary();

    public abstract void draw(GraphicsContext context);
}
