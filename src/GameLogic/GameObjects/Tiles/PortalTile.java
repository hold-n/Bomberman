package GameLogic.GameObjects.Tiles;

import GameLogic.GameObjects.Player;
import GameLogic.GameValue;
import GameLogic.GameWindow;
import GameLogic.SpriteManager;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import static GameLogic.Config.*;

/**
 * Created by Max on 07.06.2015.
 */
public class PortalTile extends Tile {

    private double destinaitonX, destinationY;

    public PortalTile(GameWindow thisWindow, double xpos, double ypos) {
        super(thisWindow, xpos, ypos);
    }

    @Override
    public Image getSprite() {
        return SpriteManager.getPortalTile();
    }

    @Override
    public Rectangle2D getBoundary() {
        double offset = (TILE_LOGICAL_SIZE - PORTAL_INNER_SIZE)/2;
        return new Rectangle2D(getX() + offset, getY() + offset,
                PORTAL_INNER_SIZE, PORTAL_INNER_SIZE);
    }

    @Override
    public void effect(Player player) {
        player.teleport(destinaitonX, destinationY);
    }
}
