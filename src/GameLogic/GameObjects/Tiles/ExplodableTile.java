package GameLogic.GameObjects.Tiles;

import GameLogic.GameObjects.Bonuses.Bonus;
import GameLogic.GameWindow;
import GameLogic.SpriteManager;
import javafx.scene.image.Image;
import java.util.Random;
import static GameLogic.Config.*;

/**
 * Created by Max on 07.06.2015.
 */

public class ExplodableTile extends ImpassableTile {
    private static Random random = new Random(System.nanoTime());

    public ExplodableTile(GameWindow thisWindow, double xpos, double ypos) {
        super(thisWindow, xpos, ypos);
    }

    @Override
    protected Image getSprite() {
        return SpriteManager.getExplodableTile();
    }

    @Override
    public void explode() {
        if (random.nextDouble() < BONUS_CHANCE) {
            double offset = 0;
            if (TILE_LOGICAL_SIZE > BONUS_SIZE) {
                offset = (TILE_LOGICAL_SIZE - BONUS_SIZE)/2;
            }
            Bonus bonus = Bonus.getRandomBonus(gameWindow, getX() + offset, getY() + offset);
            if (bonus != null) {
                gameWindow.addObject(bonus);
            }
        }
        gameWindow.removeObject(this);
    }
}
