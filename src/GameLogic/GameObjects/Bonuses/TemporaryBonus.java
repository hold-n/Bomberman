package GameLogic.GameObjects.Bonuses;

import GameLogic.GameWindow;

/**
 * Created by Max on 08.06.2015.
 */
public class TemporaryBonus extends Bonus{

    public TemporaryBonus(GameWindow window, double xpos, double ypos) {
        super(window, xpos, ypos);
    }

    @Override
    public boolean isTemporary() {
        return true;
    }
}
