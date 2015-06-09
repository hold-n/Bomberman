package GameLogic.GameObjects;

import GameLogic.GameValue;
import GameLogic.GameWindow;
import GameLogic.CollisionHandler;

/**
 * Created by Max on 09.06.2015.
 */
public class MovableObject extends FieldObject {
    protected double velocityValue;
    protected double velocityValueDelta;

    protected GameValue velocityX = new GameValue();
    protected GameValue velocityY = new GameValue();
    protected boolean moving = false;
    protected boolean teleported = false;
    protected long lastTeleport;

    protected Direction direction = Direction.NONE;

    private long lastTime;

    public double getVelocityX() { return velocityX.getLogical(); }
    public double getVelocityY() { return velocityY.getLogical(); }
    protected void setVelocityX(double value) { velocityX.setValue(value); }
    protected void setVelocityY(double value) { velocityY.setValue(value); }

    public double getVelocityValue() {
        return velocityValue;
    }
    public void setVelocityValue(double value) {
        velocityValue = value;
    }

    public void increaseVelocity() {
        velocityValue += velocityValueDelta;
    }
    public void decreaseVelocity() {
        velocityValue -= velocityValueDelta;
    }

    public Direction getDirection() { return direction; }

    public MovableObject(GameWindow window, double velocity, double velocityDelta, double xpos, double ypos) {
        super(window, xpos, ypos);
        lastTime = System.nanoTime();
        velocityValue = velocity;
        velocityValueDelta = velocityDelta;
        direction = Direction.DOWN;
    }

    public boolean isMoving() {
        return moving;
    }
    public boolean isTeleported() {
        return teleported;
    }

    /**
     * Moves the object according to its current speed and previous move() call time.
     * @param now Current time
     */
    protected void move(long now) {
        double delta = (double) (now - lastTime) / 1000000000L;
        shiftX(getVelocityX() * delta);
        shiftY(getVelocityY() * delta);
        lastTime = now;
    }

    protected void checkBorders() {
        Direction direction = CollisionHandler.collidesWithBorders(this);
        if (direction == Direction.DOWN || direction == Direction.UP)
            setVelocityY(0);
        if (direction == Direction.LEFT || direction == Direction.RIGHT)
            setVelocityX(0);
    }

    /**
     * Makes an object move by the X axis.
     * @param positive Determines the direction in which to move.
     * @param now Current time to properly start movement animation
     */
    public void moveByX(boolean positive, long now) {
        if (!isMoving())
            startAnimation(now);
        moving = true;
        if (positive) {
            direction = Direction.RIGHT;
        }
        else {
            direction = Direction.LEFT;
        }
        setVelocityX(positive ? getVelocityValue() : -getVelocityValue());
        setVelocityY(0);
    }

    /**
     * Makes an object move by the Y axis.
     * @param positive Determines the direction in which to move.
     * @param now Current time to properly start movement animation
     */
    public void moveByY(boolean positive, long now) {
        if (!isMoving())
            startAnimation(now);
        moving = true;
        if (positive) {
            direction = Direction.DOWN;
        }
        else {
            direction = Direction.UP;
        }
        setVelocityY(positive ? getVelocityValue() : -getVelocityValue());
        setVelocityX(0);
    }

    protected void startAnimation(long now) {}

    public void stop() {
        moving = false;
        setVelocityX(0);
        setVelocityY(0);
    }

    public void teleport(double x, double y) {
        lastTeleport = System.nanoTime();
        teleported = true;
        setX(x);
        setY(y);
    }
}
