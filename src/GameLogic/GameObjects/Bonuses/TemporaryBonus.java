package GameLogic.GameObjects.Bonuses;

import GameLogic.GameWindow;
import static GameLogic.Config.*;

/**
 * Created by Max on 08.06.2015.
 */
public class TemporaryBonus extends Bonus{
    protected TemporaryEffect effect;
    protected long duration = TEMP_EFFECT_DURATION;

    public TemporaryBonus(GameWindow window, double xpos, double ypos) {
        super(window, xpos, ypos);
    }

    public long getDuration() {
        return duration;
    }

    public TemporaryEffect getEffect() {
        return effect;
    }

    @Override
    public boolean isTemporary() {
        return true;
    }
}
