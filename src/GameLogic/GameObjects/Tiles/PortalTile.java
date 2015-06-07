package GameLogic.GameObjects.Tiles;

import GameLogic.GameObjects.FieldObject;
import GameLogic.GameObjects.Player;
import GameLogic.GameWindow;
import GameLogic.SpriteManager;
import javafx.scene.image.Image;

/**
 * Created by Max on 07.06.2015.
 */
public class PortalTile extends Tile {

    public PortalTile(GameWindow thisWindow, double xpos, double ypos) {
        super(thisWindow, xpos, ypos);
    }

    @Override
    public Image getSprite() {
        return SpriteManager.getPortalTile();
    }

    @Override
    public void effect(Player player) {
        // TODO
    }

    @Override
    public boolean collides(FieldObject other) {
        return getBoundary().intersects(other.getBoundary());
    }
}
