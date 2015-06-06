package GameLogic.GameObjects.Tiles;

import GameLogic.GameWindow;
import javafx.scene.image.Image;
import static GameLogic.Config.BACKGROUND_TILE_URL;
import static GameLogic.Config.TILE_GRAPHIC_SIZE;

/**
 * Created by Max on 06.06.2015.
 */
public class BackgroundTile extends Tile {

    public BackgroundTile(GameWindow window, double xpos, double ypos) {
        super(window, xpos, ypos);
        sprite = new Image(BACKGROUND_TILE_URL, TILE_GRAPHIC_SIZE, TILE_GRAPHIC_SIZE, true, true);
    }
}
