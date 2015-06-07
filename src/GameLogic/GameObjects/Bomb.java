package GameLogic.GameObjects;

import GameLogic.GameWindow;
import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Max on 05.06.2015.
 */
public class Bomb extends FieldObject{
    private long plantingTime;
    private long lifeTime;

    public Bomb(GameWindow window, double xpos, double ypos) {
        super(window, xpos, ypos);
        plantingTime = System.nanoTime();
        // TODO: set size
    }

    @Override
    public void draw(GraphicsContext context) {

    }

    @Override
    public void update(long now) {

    }

    @Override
    public void checkCollisions() {

    }

    @Override
    public boolean collides(FieldObject other) {
        return false;
    }

    public void Explode() {

    }
}