package GameLogic.GameObjects.Tiles;

import GameLogic.CollisionHandler;
import GameLogic.GameObjects.Direction;
import GameLogic.GameObjects.FieldObject;
import GameLogic.GameObjects.MovableObject;
import GameLogic.GameWindow;
import GameLogic.SpriteManager;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static GameLogic.Config.*;

/**
 * Created by Max on 07.06.2015.
 */
public class PortalTile extends Tile {
    private static final double SHIFT = (TILE_LOGICAL_SIZE - PORTAL_INNER_SIZE)/2;
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

    public void setDestination(double x, double y) {
        destinaitonX = x;
        destinationY= y;
    }

    @Override
    public void effect(FieldObject obj) {
        if (obj instanceof MovableObject && !((MovableObject) obj).isTeleported())
            ((MovableObject) obj).teleport(destinaitonX, destinationY);
    }
}
