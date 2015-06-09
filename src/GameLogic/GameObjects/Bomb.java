package GameLogic.GameObjects;

import GameLogic.AnimatedSprite;
import GameLogic.GameObjects.Bonuses.Bonus;
import GameLogic.GameWindow;
import GameLogic.MovementChecker;
import GameLogic.SpriteManager;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

import static GameLogic.Config.*;

/**
 * Created by Max on 05.06.2015.
 */

public class Bomb extends FieldObject {
    private final Player player;
    private final int length;
    private final long lifeTime;
    private AnimatedSprite sprite = new AnimatedSprite(SpriteManager::getBomb, BOMB_ANIMATION_DURATION);
    private Image currentSprite = SpriteManager.getBomb(0);

    public Bomb(GameWindow window, Player thisPlayer, double xpos, double ypos) {
        super(window, xpos, ypos);
        creationTime = System.nanoTime();
        lastTime = creationTime;
        player = thisPlayer;
        length = player.getExplosionLength();
        lifeTime = player.getBombLifeTime();
        sizeX.setValue(BOMB_SIZE);
        sizeY.setValue(BOMB_SIZE);
    }

    public Player getPlayer() {
        return player;
    }
    public int getLength() {
        return length;
    }
    public long getLifeTime() {
        return lifeTime;
    }

    @Override
    public void update(long now) {
        move(now);
        lastTime = now;
        if (now - creationTime > lifeTime)
            explode();
        currentSprite = sprite.getSprite(now);
    }

    @Override
    public void checkCollisions() {
        checkBorders();
        for (Player player : gameWindow.getPlayers())
            if (collides(player))
                MovementChecker.tryStop(player, this, true);
        for (Bonus bonus : gameWindow.getBonuses())
            if (collides(bonus))
                bonus.removeFromField();
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(getX(), getY(), BOMB_SIZE, BOMB_SIZE);
    }

    @Override
    public Image getSprite() {
        return currentSprite;
    }

    @Override
    public void explode() {
        gameWindow.removeObject(this);
        player.removeBomb(this);
        Explosion explosion = new Explosion(gameWindow, this, getX(), getY());
        gameWindow.addObject(explosion);
    }

    public void kick(Direction direction) {
        // TODO
    }
}