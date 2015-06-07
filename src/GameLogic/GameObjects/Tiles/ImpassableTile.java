package GameLogic.GameObjects.Tiles;

import GameLogic.GameObjects.FieldObject;
import GameLogic.GameObjects.Player;
import GameLogic.GameWindow;
import static GameLogic.Config.MAX_OVERLAP;
import static GameLogic.Config.STRAFE_RADIUS;
import static GameLogic.Config.STRAFE_STEP;

/**
 * Created by Max on 07.06.2015.
 */

public abstract class ImpassableTile extends Tile {
    public ImpassableTile(GameWindow thisWindow, double xpos, double ypos) {
        super(thisWindow, xpos, ypos);
    }

    @Override
    public void effect(Player player) {
        double deltaX = player.getX() - getX();
        double deltaY = player.getY() - getY();
        double overlapX = deltaX > 0 ? getSizeX() - deltaX : player.getSizeX() + deltaX;
        double overlapY = deltaY > 0 ? getSizeY() - deltaY : player.getSizeY() + deltaY;

        // no check for overlap > 0, assuming method called on collision
        if (overlapY < MAX_OVERLAP && overlapX > MAX_OVERLAP) {
            if (player.getVelocityY() * deltaY < 0) {
                if (overlapX < STRAFE_RADIUS) {
                    player.shiftX(Math.signum(deltaX)*STRAFE_STEP);
                }
                player.setVelocityY(0);
            }
        }
        if (overlapX < MAX_OVERLAP && overlapY > MAX_OVERLAP) {
            if (player.getVelocityX() * deltaX < 0) {
                if (overlapY < STRAFE_RADIUS) {
                    player.shiftY(Math.signum(deltaY)*STRAFE_STEP);
                }
                player.setVelocityX(0);
            }

        }
    }

    @Override
    public boolean collides(FieldObject other) {
        return getBoundary().intersects(other.getBoundary());
    }
}
