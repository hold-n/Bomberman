package GameLogic.GameObjects.Bonuses;

import GameLogic.GameObjects.FieldObject;
import GameLogic.GameObjects.Player;
import GameLogic.GameWindow;
import javafx.geometry.Rectangle2D;

import static GameLogic.Config.*;


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
    public boolean collides(FieldObject other) {
        return getBoundary().intersects(other.getBoundary());
    }

    @Override
    public Rectangle2D getBoundary() {
        return new Rectangle2D(getX(), getY(), BONUS_SIZE, BONUS_SIZE);
    }

    public void apply(Player player) {}
    public void discard(Player player) {}
    public abstract boolean isTemporary();

    @Override
    public void explode() {
        gameWindow.removeObject(this);
    }
}
