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
        if (effect != null) {
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
                    break;
                case FAST_EXPLOSION:
                    player.setUseTempBombLifeTime(false);
                    break;
            }
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
                break;
            case 3:
                effect = TemporaryEffect.FAST_EXPLOSION;
                player.setUseTempBombLifeTime(true);
                player.setTempBombLifeTime((int)(BOMB_LIFE_TIME * 0.5));
                break;
            default:
                effect = TemporaryEffect.DECREASE_SPEED;
                player.setTempVelocityValue(PLAYER_VELOCITY - 2*PLAYER_VELOCITY_DELTA);
                player.setUseTempVelocityValue(true);
        }
    }
}
