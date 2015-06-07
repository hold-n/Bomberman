package GameLogic.GameObjects.Tiles;

import GameLogic.GameObjects.FieldObject;
import GameLogic.GameObjects.Player;
import GameLogic.GameWindow;
import static GameLogic.Config.MAX_OVERLAP;

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
        // TODO: add strafe
//        perhaps, on removing >0 comparisons, nothing will change
        if (overlapY > 0 && overlapY < MAX_OVERLAP && overlapX > MAX_OVERLAP) {
            if (player.getVelocityY() * deltaY < 0)
                player.setVelocityY(0);
        }
        if (overlapX > 0 && overlapX < MAX_OVERLAP && overlapY > MAX_OVERLAP) {
            if (player.getVelocityX() * deltaX < 0)
                player.setVelocityX(0);
        }
    }

    @Override
    public boolean collides(FieldObject other) {
        return getBoundary().intersects(other.getBoundary());
    }
}
