package GameLogic.GameObjects.HeaderObjects;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Max on 07.06.2015.
 */
public class HeaderCounter  extends HeaderObject {
    int count = 0;

    public HeaderCounter(int xpos, int ypos) {
        super(xpos, ypos);
    }

    @Override
    public void draw(GraphicsContext context) {

    }
}
