package GameLogic;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import static GameLogic.Config.*;

/**
 * Created by Max on 07.06.2015.
 */

public class SpriteManager {
    private static final Image backgroundTile =
            new Image(BACKGROUND_TILE_URL, TILE_GRAPHIC_SIZE, TILE_GRAPHIC_SIZE, true, true);
    private static final Image solidTile =
            new Image(SOLID_TILE_URL, TILE_GRAPHIC_SIZE, TILE_GRAPHIC_SIZE, true, true);
    private static final Image explodableTile =
            new Image(EXPLODABLE_TILE_URL, TILE_GRAPHIC_SIZE, TILE_GRAPHIC_SIZE, true, true);
    private static final Image portalTile =
            new Image(PORTAL_TILE_URL, TILE_GRAPHIC_SIZE, TILE_GRAPHIC_SIZE, true, true);
    private static final Image bonusBomb =
            new Image(BONUS_BOMB, (new GameValue(BONUS_SIZE).getGraphic()),
                    (new GameValue(BONUS_SIZE).getGraphic()), true, true);
    private static final Image bonuseExplosion =
            new Image(BONUS_EXPLOSION, (new GameValue(BONUS_SIZE).getGraphic()),
                    (new GameValue(BONUS_SIZE).getGraphic()), true, true);
    private static final Image bonusSpeed =
            new Image(BONUS_SPEED, (new GameValue(BONUS_SIZE).getGraphic()),
                    (new GameValue(BONUS_SIZE).getGraphic()), true, true);
    private static final Image bonusKick =
            new Image(BONUS_KICK, (new GameValue(BONUS_SIZE).getGraphic()),
                    (new GameValue(BONUS_SIZE).getGraphic()), true, true);
    private static final Image bonusDebuff =
            new Image(BONUS_DEBUFF, (new GameValue(BONUS_SIZE).getGraphic()),
                    (new GameValue(BONUS_SIZE).getGraphic()), true, true);
    private static final Image[] playerFront = new Image[PLAYER_FRAMES_NUM];
    private static final Image[] playerBack = new Image[PLAYER_FRAMES_NUM];
    private static final Image[] playerSideRight = new Image[PLAYER_FRAMES_NUM];
    private static final Image[] playerSideLeft = new Image[PLAYER_FRAMES_NUM];
    private static final Image[] bomb = new Image[BOMB_FRAMES];
    private static final Image[] explosion = new Image[EXPLOSION_FRAMES];

    public static BufferedImage getFlippedImage(BufferedImage bufferedImage, int angle) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-bufferedImage.getWidth(null), 0);;
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        BufferedImage newImage =
                new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
        op.filter(bufferedImage, newImage);
        return newImage;
    }

    static {
        GameValue playerHeight = new GameValue(PLAYER_HEIGHT);
        GameValue playerWidth = new GameValue(PLAYER_WIDTH);
        GameValue bombSize = new GameValue(BOMB_SIZE);
        GameValue explosionSize = new GameValue(EXPLOSION_UNIT_SIZE);
        for (int i = 0; i < PLAYER_FRAMES_NUM; i++) {
            String url = PLAYER_FRONT_START + (new Integer(i)).toString() + PLAYER_FRONT_END;
            playerFront[i] =
                    new Image(url, playerWidth.getGraphic(), playerHeight.getGraphic(), true, true);
        }
        for (int i = 0; i < PLAYER_FRAMES_NUM; i++) {
            String url = PLAYER_BACK_START + (new Integer(i)).toString() + PLAYER_BACK_END;
            playerBack[i] =
                    new Image(url, playerWidth.getGraphic(), playerHeight.getGraphic(), true, true);
        }
        for (int i = 0; i < PLAYER_FRAMES_NUM; i++) {
            String url = PLAYER_SIDE_START + (new Integer(i)).toString() + PLAYER_SIDE_END;
            playerSideRight[i] =
                    new Image(url, playerWidth.getGraphic(), playerHeight.getGraphic(), true, true);

            BufferedImage buffImg= SwingFXUtils.fromFXImage(playerSideRight[i], null);
            buffImg = getFlippedImage(buffImg, 2);
            playerSideLeft[i] = SwingFXUtils.toFXImage(buffImg, null);
        }
        for (int i = 0; i < BOMB_FRAMES; i++) {
            String url = BOMB_START + (new Integer(i)).toString() + BOMB_END;
            bomb[i] =
                    new Image(url, bombSize.getGraphic(), bombSize.getGraphic(), true, true);
        }

        for (int i = 0; i < EXPLOSION_FRAMES; i++) {
            String url = EXPLOSION_START + (new Integer(i)).toString() + EXPLOSION_END;
            explosion[i] =
                    new Image(url, explosionSize.getGraphic(), explosionSize.getGraphic(), true, true);
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
    public static Image getPlayerFront(int frame) {
        return playerFront[frame % PLAYER_FRAMES_NUM];
    }
    public static Image getPlayerBack(int frame) {
        return playerBack[frame % PLAYER_FRAMES_NUM];
    }
    public static Image getPlayerRight(int frame) {
        return playerSideRight[frame % PLAYER_FRAMES_NUM];
    }
    public static Image getPlayerLeft(int frame) {
        return playerSideLeft[frame % PLAYER_FRAMES_NUM];
    }
}
