package GameLogic.GameObjects.Bonuses;

import GameLogic.GameObjects.FieldObject;
import GameLogic.GameObjects.Player;
import GameLogic.GameWindow;
import javafx.geometry.Rectangle2D;

import java.util.Random;

import static GameLogic.Config.BONUS_SIZE;


/**
 * Created by Max on 06.06.2015.
 */

public abstract class Bonus extends FieldObject {
    public Bonus(GameWindow window, double xpos, double ypos) {
        super(window, xpos, ypos);
        setSizeX(BONUS_SIZE);
        setSizeY(BONUS_SIZE);
    }

    @Override
    public Rectangle2D getBoundary() {
        return new Rectangle2D(getX(), getY(), BONUS_SIZE, BONUS_SIZE);
    }

    public void apply(Player player) {
    }

    public void discard(Player player) {
    }

    public boolean isTemporary() {
        return false;
    }

    @Override
    public void explode() {
        remove();
    }

    public void remove() {
        gameWindow.removeObject(this);
    }

    public static Bonus getRandomBonus(GameWindow window, double x, double y) {
        Random random = new Random(System.nanoTime());
        switch (random.nextInt(6)) {
            case 1:
                return new BombBonus(window, x, y);
            case 2:
                return new ExplosionBonus(window, x, y);
            case 3:
                return new KickBonus(window, x, y);
            case 4:
                return new DebuffBonus(window, x, y);
            case 5:
                return new SpeedBonus(window, x, y);
        }
        return null;
    }
}