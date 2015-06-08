package GameLogic.GameObjects.Bonuses;

import GameLogic.GameObjects.Player;
import GameLogic.GameWindow;
import GameLogic.SpriteManager;
import javafx.scene.image.Image;

/**
 * Created by Max on 08.06.2015.
 */
public class DebuffBonus extends TemporaryBonus {
    private boolean decreasesSpeed = false;
    private boolean noBombs = false;
    private boolean shortExplosion = false;

    public DebuffBonus(GameWindow window, double xpos, double ypos) {
        super(window, xpos, ypos);
    }

    @Override
    public Image getSprite() {
        return SpriteManager.getBonusDebuff();
    }

    @Override
    public void apply(Player player) {
        // TODO
    }
}
