package GameLogic.GameObjects.HeaderObjects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Text;

/**
 * Created by Max on 07.06.2015.
 */
public class HeaderTimer extends HeaderObject {
    private long startTime;
    private Long currentValue;

    public HeaderTimer(int xpos, int ypos) {
        super(xpos, ypos);
        startTime = System.nanoTime();
    }
    @Override
    public void update(long now) {
        currentValue = (now - startTime) / 1000000000L;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.strokeText(currentValue.toString(), x, y);
    }
}
