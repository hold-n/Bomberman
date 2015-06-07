package GameLogic.GameObjects.Tiles;

import GameLogic.GameWindow;
import GameLogic.SpriteManager;
import javafx.scene.image.Image;

/**
 * Created by Max on 06.06.2015.
 */
public class SolidTile extends ImpassableTile {
    public SolidTile(GameWindow window, double xpos, double ypos) {
        super(window, xpos, ypos);
    }

    @Override
    protected Image getSprite() {
        return SpriteManager.getSolidTile();
    }
}
