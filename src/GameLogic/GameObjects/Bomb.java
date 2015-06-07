package GameLogic.GameObjects;

import GameLogic.GameWindow;
import GameLogic.SpriteManager;
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
            Explode();
        currentSprite = SpriteManager.getBomb((int)((now - plantingTime) / BOMB_ANIMATION_DURATION));
    }

    @Override
    public void checkCollisions() {
    }

    @Override
    public boolean collides(FieldObject other) {
        return false;
    }

    public void Explode() {
        // TODO: add explosion
        gameWindow.removeObject(this);
        player.removeBomb(this);
    }
}