package GameLogic.GameObjects.Tiles;

import GameLogic.GameObjects.Player;
import GameLogic.GameWindow;
import javafx.scene.image.Image;
import static GameLogic.Config.*;

/**
 * Created by Max on 06.06.2015.
 */
public class SolidTile extends Tile {
    public SolidTile(GameWindow window, int xpos, int ypos) {
        super(window, xpos, ypos);
        sprite = new Image(SOLID_TILE_URL, TILE_GRAPHIC_SIZE, TILE_GRAPHIC_SIZE, true, true);
    }

    @Override
    public void effect(Player player) {
        if (player.getX() - getX() < player.getSizeX())
            player.setVelocityX(0);
        if (player.getY() - getY() < player.getSizeY())
            player.setVelocityY(0);
    }
}
