package GameLogic.GameObjects.HeaderObjects;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Max on 07.06.2015.
 */
public abstract class HeaderObject {
    public abstract void draw(GraphicsContext context);
    public void update(long now) {}
}
