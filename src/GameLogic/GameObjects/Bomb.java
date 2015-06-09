package GameLogic.GameObjects;

import GameLogic.AnimatedSprite;
import GameLogic.GameObjects.Bonuses.Bonus;
import GameLogic.GameWindow;
import GameLogic.CollisionHandler;
import GameLogic.SpriteManager;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

import static GameLogic.Config.*;

/**
 * Created by Max on 05.06.2015.
 */

public class Bomb extends MovableObject {
    private final Player player;
    private final int length;
    private final long lifeTime;
    private AnimatedSprite sprite = new AnimatedSprite(SpriteManager::getBomb, BOMB_ANIMATION_DURATION);
    private Image currentSprite = SpriteManager.getBomb(0);

    public Bomb(GameWindow window, Player thisPlayer, double xpos, double ypos) {
        super(window, BOMB_VELOCITY, 0, xpos, ypos);
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
        if (now - lastTeleport > TELEPORT_LAG)
            teleported = false;
        if (now - creationTime > lifeTime)
            explode();
        currentSprite = sprite.getSprite(now);
    }

    @Override
    public void checkCollisions() {
        checkBorders();
        for (Player player : gameWindow.getPlayers())
            if (collides(player))
                CollisionHandler.tryStop(player, this, true);
        for (Bomb bomb : gameWindow.getBombs())
            if (collides(bomb))
                CollisionHandler.tryStop(bomb, this, false);
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
        long now = System.nanoTime();
        switch (direction) {
            case UP:
                moveByY(false, now);
                return;
            case DOWN:
                moveByY(true, now);
                return;
            case LEFT:
                moveByX(false, now);
                return;
            case RIGHT:
                moveByX(true, now);
        }
    }
}