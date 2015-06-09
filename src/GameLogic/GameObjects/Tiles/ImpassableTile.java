package GameLogic.GameObjects.Tiles;

import GameLogic.GameObjects.FieldObject;
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
        // TODO: add creep support
        if (obj instanceof Player)
            MovementChecker.tryStop(obj, this, true);
        else
            MovementChecker.tryStop(obj, this, false);
    }
}
