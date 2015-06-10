package GameLogic.GameObjects.HeaderObjects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * Created by Max on 07.06.2015.
 */
public class HeaderTimer extends HeaderObject {
    private transient long startTime;
    private long lifeTime;
    private Long currentValue;

    public HeaderTimer(double ratio) {
        super(ratio);
        startTime = System.nanoTime();
    }

    @Override
    public void update(long now) {
        lifeTime = now - startTime;
        currentValue = lifeTime / 1000000000L;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.strokeText(currentValue.toString(), x, y);
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        startTime = System.nanoTime() - lifeTime;
    }
}
