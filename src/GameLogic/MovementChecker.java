package GameLogic;

import GameLogic.GameObjects.Direction;
import GameLogic.GameObjects.FieldObject;
import GameLogic.GameObjects.MoveableObject;
import GameLogic.GameObjects.Player;

import static GameLogic.Config.*;

/**
 * Created by Max on 09.06.2015.
 */

class TempObject extends MoveableObject {

    public TempObject(GameWindow window, double xpos, double ypos) {
        super(window, xpos, 0, 0, ypos);
    }
}

// OMG! this entire class could have not existed, if I guessed you could stop objects somewhat before obstacles!
public class MovementChecker {

    /**
     * Checks whether a moving object should interact with borders of the screen.
     * @param obj The moving object.
     * @return Direction of the border relative to the object or NONE, if object should not interact with borders
     */
    public static Direction collidesWithBorders(MoveableObject obj) {
        if (obj.getBoundary() == null)
            return Direction.NONE;
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
    public static Direction collidesOnMovement(MoveableObject moving, FieldObject toCheck) {
        if (moving.getBoundary() == null || toCheck.getBoundary() == null)
            return Direction.NONE;
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
     * Checks whether two objects collide and should interact. Uses overlap > MAX_OVERLAP as collision sign
     * Should be used on attempting to spawn objects and cases like that.
     * @param a The first object.
     * @param b The second object.
     * @return Whether the objects should interact
     */
    public static boolean collidesStatic(FieldObject a, FieldObject b) {
        return collidesStatic(a, b, MAX_OVERLAP);
    }

    /**
     * Checks whether two objects collide and should interact.
     * Should be used on attempting to spawn objects and cases like that.
     * @param a The first object.
     * @param b The second object.
     * @param overlap Minimum overlap of boundaries to start collision.
     * @return Whether the objects should interact
     */
    public static boolean collidesStatic(FieldObject a, FieldObject b, double overlap) {
        double deltaX = a.getX() - b.getX();
        double deltaY = a.getY() - b.getY();
        double overlapX = deltaX > 0 ? b.getSizeX() - deltaX : a.getSizeX() + deltaX;
        double overlapY = deltaY > 0 ? b.getSizeY() - deltaY : a.getSizeY() + deltaY;
        return overlapX > overlap && overlapY > overlap;
    }

    /**
     * Checks whether an object can move in the given direction
     * @param object The object to check.
     * @param direction The direction in which the object wants to move.
     * @return Whether the object can move in the requested direction.
     */
    public static boolean canMove(MoveableObject object, Direction direction) {
        // TODO: fix
        boolean result = true;
        Direction walkDirection;
        if (!object.isMoving()) {
            walkDirection = Direction.NONE;
        }
        else {
            walkDirection = object.getDirection();
        }

        TempObject temp = new TempObject(object.getGameWindow(), object.getX(), object.getY());
        temp.setSizeX(object.getSizeX());
        temp.setSizeY(object.getSizeY());
        if (walkDirection == Direction.RIGHT)
            temp.moveByX(true, 0);
        if (walkDirection == Direction.LEFT)
            temp.moveByX(false, 0);
        if (walkDirection == Direction.UP)
            temp.moveByY(false, 0);
        if (walkDirection == Direction.DOWN)
            temp.moveByY(true, 0);

        for (FieldObject fieldObject : object.getGameWindow().getObjects()) {
            if (fieldObject.collides(object)) {
                result &= collidesOnMovement(temp, fieldObject) != direction;
            }
        }
        return result;
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
    public static boolean tryStop(MoveableObject moving, FieldObject toCheck, boolean strafe) {
        Direction direction = MovementChecker.collidesOnMovement(moving, toCheck);
        if ( moving.isMoving() && (direction != Direction.NONE) ) {
            moving.stop();
            if (!strafe)
                return true;

            double overlap, delta;
            Direction strafeDirection;
            double strafeStep;
            if (direction == Direction.UP || direction == Direction.DOWN) {

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

    /**
     * Finds the relative position of two objects. Only works properly is the two objects do not intersect much.
     * @param a The first object.
     * @param b The second object.
     * @return Position of the first object relative to the second or none, if the two objects are not on the same line.
     */
    public static Direction orientation(FieldObject a, FieldObject b) {
        double deltaX = a.getX() - b.getX();
        double deltaY = a.getY() - b.getY();
        double overlapX = deltaX > 0 ? b.getSizeX() - deltaX : a.getSizeX() + deltaX;
        double overlapY = deltaY > 0 ? b.getSizeY() - deltaY : a.getSizeY() + deltaY;
        if (overlapX < MAX_OVERLAP && overlapY < MAX_OVERLAP) {
            return Direction.NONE;
        }
        else {
            if (overlapX > MAX_OVERLAP) {
                if (deltaY > 0)
                    return Direction.UP;
                return Direction.DOWN;
            }
            if (deltaX > 0)
                return Direction.LEFT;
            return Direction.DOWN.RIGHT;
        }
    }
}
