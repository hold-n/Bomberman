package GameLogic.GameObjects;

import GameLogic.GameValue;
import GameLogic.GameWindow;
import GameLogic.SpriteManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

import static GameLogic.Config.*;

/**
 * Created by Max on 06.06.2015.
 */
public class Explosion extends FieldObject {
    private long explosionStart;
    private int explosionUnits;
    private Image currentSprite;
    private Bomb bomb;
    private final ArrayList<GameValue> unitsXs = new ArrayList<GameValue>();
    private final ArrayList<GameValue> unitsYs = new ArrayList<GameValue>();

    public Explosion(GameWindow window, Bomb thisBomb, int units, double xpos, double ypos) {
        super(window, xpos, ypos);
        explosionStart = System.nanoTime();
        bomb = thisBomb;
        explosionUnits = units;
        calculateUnits(units);
    }

    private void calculateUnits(int units) {
        unitsXs.add(new GameValue(getX()));
        unitsYs.add(new GameValue(getY()));
        for (int i = 1; i <= explosionUnits; i++) {
            if (!tryAddFlame(getX(), getY() - i*EXPLOSION_UNIT_SIZE))
                break;
        }
        for (int i = 1; i <= explosionUnits; i++) {
            if (!tryAddFlame(getX(), getY() + i*EXPLOSION_UNIT_SIZE))
                break;
        }
        for (int i = 1; i <= explosionUnits; i++) {
            if (!tryAddFlame(getX() - i*EXPLOSION_UNIT_SIZE, getY()))
                break;
        }
        for (int i = 1; i <= explosionUnits; i++) {
            if (!tryAddFlame(getX() + i*EXPLOSION_UNIT_SIZE, getY()))
                break;
        }
    }

    private boolean tryAddFlame(double x, double y) {
        boolean result = true;
        for (FieldObject obj : gameWindow.getObjects()) {
            if (stops(x, y, obj)) {
                result = false;
            }
        }
        if (result) {
            unitsXs.add(new GameValue(x));
            unitsYs.add(new GameValue(y));
        }
        return result;
    }

    private boolean stops(double x, double y, FieldObject obj) {
        if (flameTouches(x, y, obj)) {
            obj.explode();
            return (!(obj instanceof Player));
        }
        return false;
    }

    protected boolean flameTouches(double x, double y, FieldObject obj) {
        if (gameWindow.toBeDeleted(obj))
            return false;
        if (obj.getBoundary() != null) {
            double deltaX = x - obj.getX();
            double deltaY = y - obj.getY();
            double overlapX = deltaX > 0 ? obj.getSizeX() - deltaX : EXPLOSION_UNIT_SIZE + deltaX;
            double overlapY = deltaY > 0 ? obj.getSizeY() - deltaY : EXPLOSION_UNIT_SIZE + deltaY;
            return overlapX > MAX_OVERLAP && overlapY > MAX_OVERLAP;
        }
        return false;
    }

    @Override
    public void update(long now) {
        if (now - explosionStart > EXPLOSION_DURATION)
            end();
        currentSprite = SpriteManager.
                getExplosion((int) ((now - explosionStart) / EXPLOSION_ANIMATION_DURATION));
    }

    @Override
    public Image getSprite() {
        return currentSprite;
    }

    // Requires other objects to call this method!
    @Override
    public boolean collides(FieldObject other) {
        for (int i = 0; i < unitsXs.size(); i++)
            if (flameTouches(unitsXs.get(i).getLogical(), unitsYs.get(i).getLogical(), other))
                return true;
        return false;
    }

    @Override
    public void draw(GraphicsContext context) {
        for (int i = 0; i < unitsXs.size(); i++) {
            context.drawImage(currentSprite, unitsXs.get(i).getGraphic(), unitsYs.get(i).getGraphic());
        }
    }

    public void end() {
        gameWindow.removeObject(this);
    }
}
