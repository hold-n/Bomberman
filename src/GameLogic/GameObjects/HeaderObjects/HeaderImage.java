package GameLogic.GameObjects.HeaderObjects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import static GameLogic.Config.*;

/**
 * Created by Max on 07.06.2015.
 */
public class HeaderImage extends HeaderObject {
    private static Image image = new Image(HEADER_IMAGE, FIELD_WIDTH, HEADER_HEIGHT, false, true);

    public HeaderImage() {
        super(0, 0);
    }

    @Override
    public void draw(GraphicsContext context) {
        context.drawImage(image, 0, 0);
    }
}
