package GameLogic.GameObjects.Tiles;

import GameLogic.GameObjects.FieldObject;
import GameLogic.GameObjects.Player;
import GameLogic.GameWindow;
import GameLogic.SpriteManager;
import javafx.scene.image.Image;

/**
 * Created by Max on 07.06.2015.
 */

public class ExplodableTile extends Tile{
    public ExplodableTile(GameWindow thisWindow, double xpos, double ypos) {
        super(thisWindow, xpos, ypos);
    }

    @Override
    protected Image getSprite() {
        return SpriteManager.getExplodableTile();
    }

    @Override
    public void effect(Player player) {
        if (player.getX() - getX() < player.getSizeX())
            player.setVelocityX(0);
        if (player.getY() - getY() < player.getSizeY())
            player.setVelocityY(0);
    }

    @Override
    public boolean collides(FieldObject other) {
        return getBoundary().intersects(other.getBoundary());
    }
}
