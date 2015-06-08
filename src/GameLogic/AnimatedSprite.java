package GameLogic;

import javafx.scene.image.Image;

/**
 * Created by Max on 08.06.2015.
 */
public class AnimatedSprite {
    private SpriteSource source;
    private long startTime;
    private long duration;

    public AnimatedSprite(SpriteSource spriteSource, long frameDuration) {
        source = spriteSource;
        startTime = System.nanoTime();
        duration = frameDuration;
    }

    public Image getSprite(long now) {
        return source.getSprite((int)((now - startTime)/duration));
    }

    public void setStart(long time) {
        startTime = time;
    }
}
