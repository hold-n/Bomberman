package GameLogic.GameObjects.HeaderObjects;

import javafx.scene.canvas.GraphicsContext;
import static GameLogic.Config.*;

/**
 * Created by Max on 07.06.2015.
 */
public class HeaderLabel extends HeaderObject{
    private String toWrite;

    public HeaderLabel(String text, double ratio) {
        super(ratio);
        toWrite = text;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.strokeText(toWrite, x, y);
    }
}
