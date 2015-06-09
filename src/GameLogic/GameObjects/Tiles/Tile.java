package GameLogic.GameObjects.Tiles;

import GameLogic.GameObjects.Bomb;
import GameLogic.GameObjects.Explosion;
import GameLogic.GameObjects.FieldObject;
import GameLogic.GameObjects.Player;
import GameLogic.GameWindow;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

import static GameLogic.Config.*;

/**
 * Created by Max on 05.06.2015.
 */

public abstract class Tile extends FieldObject {
    public Tile(GameWindow thisWindow, double xpos, double ypos) {
        super(thisWindow, xpos, ypos);
        sizeX.setValue(TILE_LOGICAL_SIZE);
        sizeY.setValue(TILE_LOGICAL_SIZE);
    }

    @Override
    public Rectangle2D getBoundary() {
        return new Rectangle2D(x.getLogical(), y.getLogical(),
                TILE_LOGICAL_SIZE, TILE_LOGICAL_SIZE);
    }

    @Override
    public void checkCollisions() {
        // TODO: add creep support
        for (Player player : gameWindow.getPlayers())
            if (collides(player))
                effect(player);
        for (Bomb bomb : gameWindow.getBombs())
            if (collides(bomb))
                effect(bomb);
    }

    public void effect(FieldObject object) {}
}
