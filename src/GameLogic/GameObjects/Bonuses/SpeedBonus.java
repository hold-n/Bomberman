package GameLogic.GameObjects.Bonuses;

import GameLogic.GameObjects.Player;
import GameLogic.GameWindow;
import GameLogic.SpriteManager;
import javafx.scene.image.Image;

/**
 * Created by Max on 08.06.2015.
 */
public class SpeedBonus extends Bonus {
    public SpeedBonus(GameWindow window, double xpos, double ypos) {
        super(window, xpos, ypos);
    }

    @Override
    public Image getSprite() {
        return SpriteManager.getBonusSpeed();
    }

    @Override
    public void apply(Player player) {
        player.increaseVelocity();
    }
}
