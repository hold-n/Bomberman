package GameLogic.GameObjects;

import GameLogic.GameWindow;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Shape;

/**
 * Created by Max on 05.06.2015.
 */
public class Bomb extends FieldObject{

    public Bomb(GameWindow window, double xpos, double ypos) {
        super(window, xpos, ypos);
    }

    @Override
    public void draw(GraphicsContext context) {

    }
}