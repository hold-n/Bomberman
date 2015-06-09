package GameLogic.GameObjects.Tiles;

import GameLogic.GameObjects.FieldObject;
import GameLogic.GameObjects.MovableObject;
import GameLogic.GameObjects.Player;
import GameLogic.GameWindow;
import GameLogic.CollisionHandler;

/**
 * Created by Max on 07.06.2015.
 */

public abstract class ImpassableTile extends Tile {
    public ImpassableTile(GameWindow thisWindow, double xpos, double ypos) {
        super(thisWindow, xpos, ypos);
    }

    @Override
    public void effect(FieldObject obj) {
        if (obj instanceof MovableObject) {
            if (obj instanceof Player)
                CollisionHandler.tryStop((Player) obj, this, true);
            else
                CollisionHandler.tryStop((MovableObject) obj, this, false);
        }
    }
}
