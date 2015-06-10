package GameLogic.GameObjects.HeaderObjects;

import javafx.scene.canvas.GraphicsContext;
import java.io.Serializable;
import static GameLogic.Config.*;

/**
 * Created by Max on 07.06.2015.
 */

// Sorry for header implementation
public abstract class HeaderObject implements Serializable {
    protected int x;
    protected int y = (int)(0.75 * HEADER_HEIGHT);

    public HeaderObject(double ratio) {
        if (ratio > 0 && ratio < 1)
            x = (int)(ratio * TILE_GRAPHIC_SIZE * TILES_HOR);
    }

    public HeaderObject(int xpos, int ypos) {
        x = xpos;
        y = ypos;
    }

    public abstract void draw(GraphicsContext context);
    public void update(long now) {}
}
