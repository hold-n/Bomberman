package GameLogic.GameObjects.Bonuses;

import GameLogic.GameObjects.Player;
import GameLogic.GameWindow;
import GameLogic.SpriteManager;
import javafx.scene.image.Image;

import java.util.Random;
import static GameLogic.Config.*;

/**
 * Created by Max on 08.06.2015.
 */
public class DebuffBonus extends TemporaryBonus {

    public DebuffBonus(GameWindow window, double xpos, double ypos) {
        super(window, xpos, ypos);
    }

    @Override
    public Image getSprite() {
        return SpriteManager.getBonusDebuff();
    }

    @Override
    public void discard(Player player) {
        switch (effect) {
            case NO_BOMBS:
                player.setUseTempMaxBombCount(false);
                break;
            case SHORT_EXPLOSION:
                player.setUseTempExplosionLength(false);
                break;
            case BOMB_SPAWN:
                player.setBombSpawn(false);
                break;
            case DECREASE_SPEED:
                player.setUseTempVelocityValue(false);
        }
    }

    @Override
    public void apply(Player player) {
        Random random = new Random(System.nanoTime());
        switch (random.nextInt(TemporaryEffect.values().length)) {
            case 0:
                effect = TemporaryEffect.NO_BOMBS;
                player.setTempMaxBombCount(0);
                player.setUseTempMaxBombCount(true);
                break;
            case 1:
                effect = TemporaryEffect.SHORT_EXPLOSION;
                player.setTempExplosionLength(1);
                player.setUseTempExplosionLength(true);
                break;
            case 2:
                effect = TemporaryEffect.BOMB_SPAWN;
                player.setBombSpawn(true);
            default:
                effect = TemporaryEffect.DECREASE_SPEED;
                player.setTempVelocityValue(PLAYER_VELOCITY - PLAYER_VELOCITY_DELTA);
                player.setUseTempVelocityValue(true);
        }
    }
}
