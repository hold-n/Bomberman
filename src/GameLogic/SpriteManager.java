package GameLogic;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import static GameLogic.Config.*;

/**
 * Created by Max on 07.06.2015.
 */

public class SpriteManager {
    private static final Image backgroundTile =
            new Image(SpriteManager.class.getResource(BACKGROUND_TILE_URL).toExternalForm(),
                    TILE_GRAPHIC_SIZE, TILE_GRAPHIC_SIZE, true, true);
    private static final Image solidTile =
            new Image(SpriteManager.class.getResource(SOLID_TILE_URL).toExternalForm(),
                    TILE_GRAPHIC_SIZE, TILE_GRAPHIC_SIZE, true, true);
    private static final Image explodableTile =
            new Image(SpriteManager.class.getResource(EXPLODABLE_TILE_URL).toExternalForm(),
                    TILE_GRAPHIC_SIZE, TILE_GRAPHIC_SIZE, true, true);
    private static final Image portalTile =
            new Image(SpriteManager.class.getResource(PORTAL_TILE_URL).toExternalForm(),
                    TILE_GRAPHIC_SIZE, TILE_GRAPHIC_SIZE, true, true);
    private static final Image bonusBomb =
            new Image(SpriteManager.class.getResource(BONUS_BOMB).toExternalForm(),
                    (new GameValue(BONUS_SIZE).getGraphic()),
                    (new GameValue(BONUS_SIZE).getGraphic()), true, true);
    private static final Image bonuseExplosion =
            new Image(SpriteManager.class.getResource(BONUS_EXPLOSION).toExternalForm(),
                    (new GameValue(BONUS_SIZE).getGraphic()),
                    (new GameValue(BONUS_SIZE).getGraphic()), true, true);
    private static final Image bonusSpeed =
            new Image(SpriteManager.class.getResource(BONUS_SPEED).toExternalForm(),
                    (new GameValue(BONUS_SIZE).getGraphic()),
                    (new GameValue(BONUS_SIZE).getGraphic()), true, true);
    private static final Image bonusKick =
            new Image(SpriteManager.class.getResource(BONUS_KICK).toExternalForm(),
                    (new GameValue(BONUS_SIZE).getGraphic()),
                    (new GameValue(BONUS_SIZE).getGraphic()), true, true);
    private static final Image bonusDebuff =
            new Image(SpriteManager.class.getResource(BONUS_DEBUFF).toExternalForm(),
                    (new GameValue(BONUS_SIZE).getGraphic()),
                    (new GameValue(BONUS_SIZE).getGraphic()), true, true);
    private static final Image[] playerFront = new Image[PLAYER_FRAMES_NUM];
    private static final Image[] playerBack = new Image[PLAYER_FRAMES_NUM];
    private static final Image[] playerSideRight = new Image[PLAYER_FRAMES_NUM];
    private static final Image[] playerSideLeft = new Image[PLAYER_FRAMES_NUM];

    private static final Image[] playerFrontInv = new Image[PLAYER_FRAMES_NUM];
    private static final Image[] playerBackInv = new Image[PLAYER_FRAMES_NUM];
    private static final Image[] playerSideRightInv = new Image[PLAYER_FRAMES_NUM];
    private static final Image[] playerSideLeftInv = new Image[PLAYER_FRAMES_NUM];

    private static final Image[] bomb = new Image[BOMB_FRAMES];
    private static final Image[] explosion = new Image[EXPLOSION_FRAMES];

    public static Image getFlippedImage(Image image) {
        BufferedImage bufferedImage= SwingFXUtils.fromFXImage(image, null);
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-bufferedImage.getWidth(null), 0);;
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        BufferedImage newImage =
                new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
        op.filter(bufferedImage, newImage);
        return SwingFXUtils.toFXImage(newImage, null);
    }

    public static Image inverseImage(Image image) {
        BufferedImage newImage = SwingFXUtils.fromFXImage(image, null);
        for (int x = 0; x < newImage.getWidth(); x++) {
            for (int y = 0; y < newImage.getHeight(); y++) {
                int rgba = newImage.getRGB(x, y);
                Color col = new Color(rgba, true);
                if (col.getAlpha() != 0) {
                    col = new Color(255 - col.getRed(),
                            255 - col.getGreen(),
                            255 - col.getBlue());
                    newImage.setRGB(x, y, col.getRGB());
                }
            }
        }
        return SwingFXUtils.toFXImage(newImage, null);
    }

    static {
        GameValue playerHeight = new GameValue(PLAYER_HEIGHT);
        GameValue playerWidth = new GameValue(PLAYER_WIDTH);
        GameValue bombSize = new GameValue(BOMB_SIZE);
        GameValue explosionSize = new GameValue(EXPLOSION_UNIT_SIZE);
        BufferedImage buff;
        for (int i = 0; i < PLAYER_FRAMES_NUM; i++) {
            String url = PLAYER_FRONT_START + (new Integer(i)).toString() + PLAYER_FRONT_END;
            playerFront[i] =
                    new Image(SpriteManager.class.getResource(url).toExternalForm(),
                            playerWidth.getGraphic(), playerHeight.getGraphic(), true, true);
            playerFrontInv[i] = inverseImage(playerFront[i]);
        }
        for (int i = 0; i < PLAYER_FRAMES_NUM; i++) {
            String url = PLAYER_BACK_START + (new Integer(i)).toString() + PLAYER_BACK_END;
            playerBack[i] =
                    new Image(SpriteManager.class.getResource(url).toExternalForm(),
                            playerWidth.getGraphic(), playerHeight.getGraphic(), true, true);
            playerBackInv[i] =
                    inverseImage(playerBack[i]);
        }
        for (int i = 0; i < PLAYER_FRAMES_NUM; i++) {
            String url = PLAYER_SIDE_START + (new Integer(i)).toString() + PLAYER_SIDE_END;
            playerSideRight[i] =
                    new Image(SpriteManager.class.getResource(url).toExternalForm(),
                            playerWidth.getGraphic(), playerHeight.getGraphic(), true, true);
            playerSideLeft[i] = getFlippedImage(playerSideRight[i]);
            playerSideRightInv[i] = inverseImage(playerSideRight[i]);
            playerSideLeftInv[i] = inverseImage(playerSideLeft[i]);
        }
        for (int i = 0; i < BOMB_FRAMES; i++) {
            String url = BOMB_START + (new Integer(i)).toString() + BOMB_END;
            bomb[i] =
                    new Image(SpriteManager.class.getResource(url).toExternalForm(),
                            bombSize.getGraphic(), bombSize.getGraphic(), true, true);
        }

        for (int i = 0; i < EXPLOSION_FRAMES; i++) {
            String url = EXPLOSION_START + (new Integer(i)).toString() + EXPLOSION_END;
            explosion[i] =
                    new Image(SpriteManager.class.getResource(url).toExternalForm(),
                            explosionSize.getGraphic(), explosionSize.getGraphic(), true, true);
        }
    }

    public static Image getBackgroundTile() {
        return backgroundTile;
    }
    public static Image getSolidTile() {
        return solidTile;
    }
    public static Image getExplodableTile() {
        return explodableTile;
    }
    public static Image getPortalTile() {
        return portalTile;
    }
    public static Image getBonusBomb() {
        return bonusBomb;
    }
    public static Image getBonuseExplosion() {
        return bonuseExplosion;
    }
    public static Image getBonusSpeed() {
        return bonusSpeed;
    }
    public static Image getBonusKick() {
        return bonusKick;
    }
    public static Image getBonusDebuff() {
        return bonusDebuff;
    }
    public static Image getBomb(int frame) {
        return bomb[frame % BOMB_FRAMES];
    }
    public static Image getExplosion(int frame) {
        return explosion[frame % EXPLOSION_FRAMES];
    }
    public static Image getPlayerFront(int frame, boolean inversed) {
        if (inversed)
            return playerFrontInv[frame % PLAYER_FRAMES_NUM];
        return playerFront[frame % PLAYER_FRAMES_NUM];
    }
    public static Image getPlayerBack(int frame, boolean inversed) {
        if (inversed)
            return playerBackInv[frame % PLAYER_FRAMES_NUM];
        return playerBack[frame % PLAYER_FRAMES_NUM];
    }
    public static Image getPlayerRight(int frame, boolean inversed) {
        if (inversed)
            return playerSideRightInv[frame % PLAYER_FRAMES_NUM];
        return playerSideRight[frame % PLAYER_FRAMES_NUM];
    }
    public static Image getPlayerLeft(int frame, boolean inversed) {
        if (inversed)
            return playerSideLeftInv[frame % PLAYER_FRAMES_NUM];
        return playerSideLeft[frame % PLAYER_FRAMES_NUM];
    }
}
