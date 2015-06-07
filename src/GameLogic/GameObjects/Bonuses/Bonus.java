package GameLogic.GameObjects.Bonuses;

import GameLogic.GameObjects.FieldObject;
import GameLogic.GameObjects.Player;
import GameLogic.GameWindow;


/**
 * Created by Max on 06.06.2015.
 */
public abstract class Bonus extends FieldObject {
    public Bonus(GameWindow window, double xpos, double ypos) {
        super(window, xpos, ypos);
        // Set size
    }

    public void apply(Player player) {}
    public void discarg(Player player) {}
    public abstract boolean isTemporary();
}
