package GameLogic.GameObjects.Tiles;

import GameLogic.GameWindow;
import GameLogic.SpriteManager;
import javafx.scene.image.Image;

/**
 * Created by Max on 06.06.2015.
 */
public class BackgroundTile extends Tile {

    public BackgroundTile(GameWindow window, double xpos, double ypos) {
        super(window, xpos, ypos);
    }

    @Override
    protected Image getSprite() {
        return SpriteManager.getBackgroundTile();
    }
}
