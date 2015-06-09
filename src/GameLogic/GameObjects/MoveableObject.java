package GameLogic.GameObjects;

import GameLogic.GameValue;
import GameLogic.GameWindow;
import GameLogic.MovementChecker;

/**
 * Created by Max on 09.06.2015.
 */
public class MoveableObject extends FieldObject {
    protected double velocityValue;
    protected double velocityValueDelta;

    protected GameValue velocityX = new GameValue();
    protected GameValue velocityY = new GameValue();
    protected boolean moving = false;

    protected Direction direction = Direction.NONE;

    protected long lastTime;

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

    public MoveableObject(GameWindow window, double velocity, double velocityDelta, double xpos, double ypos) {
        super(window, xpos, ypos);
        lastTime = System.nanoTime();
        velocityValue = velocity;
        velocityValueDelta = velocityDelta;
        direction = Direction.DOWN;
    }

    public boolean isMoving() {
        return moving;
    }

    /**
     * Moves the object according to its current speed.
     * Requires the object to update lastTime to keep the time of the previous update() call.
     * @param now
     */
    protected void move(long now) {
        double delta = (double) (now - lastTime) / 1000000000L;
        shiftX(getVelocityX() * delta);
        shiftY(getVelocityY() * delta);
    }

    protected void checkBorders() {
        Direction direction = MovementChecker.collidesWithBorders(this);
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
}
