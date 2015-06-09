package GameLogic.GameObjects.Bonuses;

import GameLogic.GameWindow;
import static GameLogic.Config.*;

/**
 * Created by Max on 08.06.2015.
 */
public class TemporaryBonus extends Bonus{
    protected TemporaryEffect effect;
    protected long duration = TEMP_EFFECT_DURATION;
    protected long pickUpTime;

    public TemporaryBonus(GameWindow window, double xpos, double ypos) {
        super(window, xpos, ypos);
    }

    public long getDuration() {
        return duration;
    }

    @Override
    public void removeFromField() {
        gameWindow.removeObject(this);
        pickUpTime = System.nanoTime();
    }

    public TemporaryEffect getEffect() {
        return effect;
    }

    public long getPickUpTime() {
        return pickUpTime;
    }

}
