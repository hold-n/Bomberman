package GameLogic.GameObjects;

import GameLogic.GameWindow;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Max on 06.06.2015.
 */
public class Explosion extends FieldObject {
    public Explosion(GameWindow window, double xpos, double ypos) {
        super(window, xpos, ypos);
    }

    @Override
    public Rectangle2D getBoundary() {
        return null;
    }

    @Override
    public void draw(GraphicsContext context) {

    }
}
