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
    protected static Image sprite;

    protected GameWindow gameWindow;
    protected Iterable<FieldObject> objectCollection;

    public double getX() { return x.getLogical(); }
    public double getY() { return y.getLogical(); }
    public double getSizeX() { return sizeX.getLogical(); }
    public double getSizeY() { return sizeY.getLogical(); }
    public double getvelocityX() { return velocityX.getLogical(); }
    public double getvelocityY() { return velocityY.getLogical(); }

    public void setX(double value) { x.setValue(value); }
    public void setY(double value) { y.setValue(value); }
    public void setSizeX(double value) { sizeX.setValue(value); }
    public void setSizeY(double value) { sizeY.setValue(value); }
    public void setVelocityX(double value) { velocityX.setValue(value); }
    public void setVelocityY(double value) { velocityY.setValue(value); }

    public FieldObject(GameWindow window, double xpos, double ypos) {
        gameWindow = window;
        objectCollection = gameWindow.getObjects();
        x.setValue(xpos);
        y.setValue(ypos);
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(x.getLogical(), y.getLogical(),
                sizeX.getLogical(), sizeY.getLogical());
    }

    public void update(long now) {}
    public void checkCollisions() {}
    public abstract void draw(GraphicsContext context);
}
