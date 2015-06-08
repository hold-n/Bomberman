package GameLogic.GameObjects;

import GameLogic.GameWindow;
import GameLogic.SpriteManager;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static GameLogic.Config.*;

/**
 * Created by Max on 05.06.2015.
 */

public class Bomb extends FieldObject{
    private Player player;
    private long plantingTime;
    private long lifeTime = BOMB_LIFE_TIME;
    private Image currentSprite = SpriteManager.getBomb(0);

    public Bomb(GameWindow window, Player thisPlayer, double xpos, double ypos) {
        super(window, xpos, ypos);
        plantingTime = System.nanoTime();
        player = thisPlayer;
        sizeX.setValue(BOMB_SIZE);
        sizeY.setValue(BOMB_SIZE);
    }

    @Override
    public void draw(GraphicsContext context) {
            context.drawImage(currentSprite, x.getGraphic(), y.getGraphic());
    }

    @Override
    public void update(long now) {
        if (now - plantingTime > BOMB_LIFE_TIME)
            explode();
        currentSprite = SpriteManager.getBomb((int)((now - plantingTime) / BOMB_ANIMATION_DURATION));
    }

    @Override
    public void checkCollisions() {
        // TODO: stop on obstacles, explode on intersecting explosions
    }

    @Override
    public boolean collides(FieldObject other) {
        if (other instanceof Explosion)
            return other.collides(this);
        return getBoundary().intersects(other.getBoundary());
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(getX(), getY(), BOMB_SIZE, BOMB_SIZE);
    }

    @Override
    public void explode() {
        gameWindow.removeObject(this);
        player.removeBomb(this);
        Explosion explosion = new Explosion(gameWindow, this, player.getExplosionLength(), getX(), getY());
        gameWindow.addObject(explosion);
    }
}