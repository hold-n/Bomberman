package GameLogic.GameObjects.Tiles;

import GameLogic.GameObjects.FieldObject;
import GameLogic.GameObjects.Player;
import GameLogic.GameWindow;
import javafx.geometry.Rectangle2D;

/**
 * Created by Max on 07.06.2015.
 */

public abstract class ImpassableTile extends Tile {
    public ImpassableTile(GameWindow thisWindow, double xpos, double ypos) {
        super(thisWindow, xpos, ypos);
    }

    @Override
    public void effect(Player player) {
        player.tryStop(this);
    }
}
