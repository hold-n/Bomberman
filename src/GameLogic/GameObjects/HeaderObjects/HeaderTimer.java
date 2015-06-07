package GameLogic.GameObjects.HeaderObjects;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Max on 07.06.2015.
 */
public class HeaderTimer extends HeaderObject {
    private long startTime;
    private Long currentValue = 0L;
    private long x;
    private long y;

    public HeaderTimer(long start, int xpos, int ypos) {
        super(xpos, ypos);
        startTime = start;
    }
    @Override
    public void update(long now) {
        currentValue = (System.nanoTime() - startTime) / 1000000000;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.fillText(currentValue.toString(), x, y);
    }
}
