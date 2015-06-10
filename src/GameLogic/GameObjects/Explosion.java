package GameLogic.GameObjects;

import GameLogic.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.ArrayList;

import static GameLogic.Config.*;

/**
 * Created by Max on 06.06.2015.
 */

public class Explosion extends FieldObject {
    private int explosionUnits;
    private transient AnimatedSprite sprite;
    private transient Image currentSprite;
    private Bomb bomb;
    private final ArrayList<GameValue> unitsXs = new ArrayList<>();
    private final ArrayList<GameValue> unitsYs = new ArrayList<>();

    public Explosion(GameWindow window, Bomb thisBomb, double xpos, double ypos) {
        super(window, xpos, ypos);
        creationTime = System.nanoTime();
        bomb = thisBomb;
        explosionUnits = thisBomb.getLength();
        sprite = new AnimatedSprite(SpriteManager::getExplosion, EXPLOSION_ANIMATION_DURATION);
        currentSprite = SpriteManager.getExplosion(0);
        calculateUnits(explosionUnits);
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
                result = obj instanceof Player;
            }
        }
        if (result) {
            unitsXs.add(new GameValue(x));
            unitsYs.add(new GameValue(y));
        }
        return result;
    }

    private class ExplosionUnit extends FieldObject {
        public ExplosionUnit(GameWindow window, double xpos, double ypos) {
            super(window, xpos, ypos);
            setSizeX(EXPLOSION_UNIT_SIZE);
            setSizeY(EXPLOSION_UNIT_SIZE);
        }
    }

    private boolean stops(double x, double y, FieldObject obj) {
        if (obj.getBoundary() != null) {
            if (!gameWindow.toBeDeleted(obj))
                if (CollisionHandler.collidesStatic(new ExplosionUnit(gameWindow, x, y), obj, EXPLOSION_OVERLAP)) {
                    obj.explode();
                    return (!(obj instanceof Player));
                }
            }
        return false;
    }

    @Override
    public void update(long now) {
        super.update(now);
        if (lifeTime > EXPLOSION_DURATION)
            end();
        currentSprite = sprite.getSprite(now);
    }

    @Override
    public void checkCollisions() {
        for (FieldObject obj : gameWindow.getObjects()) {
            if (!(obj instanceof Explosion) && collides(obj)) {
                obj.explode();
            }
        }
    }

    @Override
    public Image getSprite() {
        return currentSprite;
    }

    @Override
    public Rectangle2D getBoundary() {
        return new Rectangle2D(getX() - TILE_GRAPHIC_SIZE*explosionUnits, getY() - TILE_GRAPHIC_SIZE*explosionUnits,
                TILE_GRAPHIC_SIZE*(2*explosionUnits + 1), TILE_GRAPHIC_SIZE*(2*explosionUnits + 1));
    }

    // Requires other objects to call this method!
    @Override
    public boolean collides(FieldObject other) {
        if (other.getBoundary() == null)
            return false;
        boolean doesCollide = false;
        for (int i = 0; i < unitsXs.size(); i++) {
            ExplosionUnit unit = new ExplosionUnit(gameWindow, unitsXs.get(i).getLogical(), unitsYs.get(i).getLogical());
            doesCollide |= CollisionHandler.collidesStatic(unit, other, EXPLOSION_OVERLAP);
        }
        return doesCollide;
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

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        creationTime = System.nanoTime() - lifeTime;
        sprite = new AnimatedSprite(SpriteManager::getExplosion, EXPLOSION_ANIMATION_DURATION);
    }
}
