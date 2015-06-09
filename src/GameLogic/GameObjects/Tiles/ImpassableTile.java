package GameLogic.GameObjects.Tiles;

import GameLogic.GameObjects.FieldObject;
import GameLogic.GameObjects.MoveableObject;
import GameLogic.GameObjects.Player;
import GameLogic.GameWindow;
import GameLogic.MovementChecker;
import javafx.geometry.Rectangle2D;

/**
 * Created by Max on 07.06.2015.
 */

public abstract class ImpassableTile extends Tile {
    public ImpassableTile(GameWindow thisWindow, double xpos, double ypos) {
        super(thisWindow, xpos, ypos);
    }

    @Override
    public void effect(FieldObject obj) {
        if (obj instanceof MoveableObject) {
            if (obj instanceof Player)
                MovementChecker.tryStop((Player)obj, this, true);
            else
                MovementChecker.tryStop((MoveableObject)obj, this, false);
        }
    }
}
