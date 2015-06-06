package GameLogic.GameObjects;

import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * Created by Max on 06.06.2015.
 */
public class SpriteManager {
    SpriteManager instance = new SpriteManager();
    private static ArrayList<Image> sprites = new ArrayList<Image>();

    private SpriteManager() {

    }

    public SpriteManager getInstance() {
        return instance;
    }
}
