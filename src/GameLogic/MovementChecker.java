package GameLogic;

import GameLogic.GameObjects.Direction;
import GameLogic.GameObjects.FieldObject;
import static GameLogic.Config.*;

/**
 * Created by Max on 09.06.2015.
 */

public class MovementChecker {
    /**
     * Checks whether a moving object should interact with borders of the screen.
     * @param obj The moving object.
     * @return Direction of the border relative to the object or NONE, if object should not interact with borders
     */
    public static Direction collidesWithBorders(FieldObject obj) {
        if (obj.getX() < 0 && obj.getVelocityX() < 0)
            return Direction.LEFT;
        if (obj.getX() > LOGICAL_WIDTH - obj.getSizeX() && obj.getVelocityX() > 0)
            return Direction.RIGHT;
        if (obj.getY() < 0 && obj.getVelocityY() < 0)
            return Direction.UP;
        if (obj.getY() > LOGICAL_HEIGHT - obj.getSizeY() && obj.getVelocityY() > 0)
            return Direction.DOWN;
        return Direction.NONE;
    }

    /**
     * Checks whether a moving object touches another object and should interact with it.
     * Should be used on most cases for moving objects.
     * @param moving The moving object
     * @param toCheck Object to check for interaction
     * @return The direction of the object to check relative to the moving one or NONE, if there should be no interactions
     */
    public static Direction collidesOnMovement(FieldObject moving, FieldObject toCheck) {
        double deltaX = moving.getX() - toCheck.getX();
        double deltaY = moving.getY() - toCheck.getY();
        double overlapX = deltaX > 0 ? toCheck.getSizeX() - deltaX : moving.getSizeX() + deltaX;
        double overlapY = deltaY > 0 ? toCheck.getSizeY() - deltaY : moving.getSizeY() + deltaY;

        if (overlapY < MAX_OVERLAP && overlapX > MAX_OVERLAP) {
            if (moving.getVelocityY() < 0 && deltaY > 0)
                return Direction.UP;
            if (moving.getVelocityY() > 0 && deltaY < 0)
                return Direction.DOWN;
        }

        if (overlapX < MAX_OVERLAP && overlapY > MAX_OVERLAP) {
            if (moving.getVelocityX() < 0 && deltaX > 0)
                return Direction.LEFT;
            if (moving.getVelocityX() > 0 && deltaX < 0)
                return Direction.RIGHT;
        }

        return Direction.NONE;
    }

    /**
     * Checks whether two objects collide and should interact.
     * Should be used on attempting to spawn objects and cases like that.
     * @param a The first object.
     * @param b The second object.
     * @return Whether the objects should interact
     */
    public static boolean collidesStatic(FieldObject a, FieldObject b) {
        double deltaX = a.getX() - b.getX();
        double deltaY = a.getY() - b.getY();
        double overlapX = deltaX > 0 ? b.getSizeX() - deltaX : a.getSizeX() + deltaX;
        double overlapY = deltaY > 0 ? b.getSizeY() - deltaY : a.getSizeY() + deltaY;
        return overlapX > MAX_OVERLAP && overlapY > MAX_OVERLAP;
    }

    /**
     * Checks whether an object can move in the given direction
     * @param obj The object to check.
     * @param direction The direction in which the object wants to move.
     * @return Whether the object can move in the requested direction.
     */
    public static boolean canMove(FieldObject obj, Direction direction) {
        // TODO
        return true;
    }

    /**
     * Determines whether the object will statically collide with any object in the collection
     * @param obj
     * @param objects
     * @return
     */
    public static boolean willCollideAny(FieldObject obj, Iterable<? extends FieldObject> objects) {
        for (FieldObject toCheck : objects)
            if (collidesStatic(obj, toCheck))
                return true;
        return false;
    }

    /**
     * Attempts to stop a moving object because of the collision with another object.
     * @param moving The moving object.
     * @param toCheck An object to check for collision with.
     * @return Whether the object has been stopped
     */
    public static boolean tryStop(FieldObject moving, FieldObject toCheck, boolean strafe) {
        Direction direction = MovementChecker.collidesOnMovement(moving, toCheck);
        if ( moving.isMoving() && (direction != Direction.NONE) ) {
            double overlap, delta;

            Direction strafeDirection;
            double strafeStep;
            if (direction == Direction.UP || direction == Direction.DOWN) {
                moving.setVelocityY(0);
                delta = moving.getX() - toCheck.getX();
                overlap = delta > 0 ? toCheck.getSizeX() - delta : moving.getSizeX() + delta;
                strafeDirection = delta > 0 ? Direction.RIGHT : Direction.LEFT;
                if (overlap < STRAFE_RADIUS && canMove(moving, strafeDirection)) {
                    strafeStep = strafeDirection == Direction.LEFT ? -STRAFE_STEP : STRAFE_STEP;
                    moving.shiftX(strafeStep);
                }
                return true;
            }
            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                moving.setVelocityX(0);
                delta = moving.getY() - toCheck.getY();
                overlap = delta > 0 ? toCheck.getSizeY() - delta : moving.getSizeY() + delta;
                strafeDirection = delta > 0 ? Direction.DOWN : Direction.UP;
                if (overlap < STRAFE_RADIUS && canMove(moving, strafeDirection)) {
                    strafeStep = strafeDirection == Direction.UP ? -STRAFE_STEP : STRAFE_STEP;
                    moving.shiftY(strafeStep);
                }
                return true;
            }
        }
        return false;
    }
}
