package GameLogic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Max on 05.06.2015.
 */

public final class Config {
    private static final Properties properties = new Properties();

    static {
        FileInputStream in;
        try {
            in = new FileInputStream("src/config.properties");
            properties.load(in);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            // TODO: add support for no config
        }
        catch (IOException e) {
            e.printStackTrace();
            // TODO: add support for incorrect config
        }
    }

    // TODO: exception handling
    public static final String TITLE =
            properties.getProperty("title");
    public static final int FPS =
            Integer.valueOf(properties.getProperty("fps"));

    public static final int MENU_HEIGHT =
            Integer.valueOf(properties.getProperty("menu.height"));
    public static final int MENU_WIDTH =
            Integer.valueOf(properties.getProperty("menu.width"));
    public static final int HEADER_HEIGHT =
            Integer.valueOf(properties.getProperty("game.header.height"));
    public static final int TILE_GRAPHIC_SIZE =
            Integer.valueOf(properties.getProperty("tile.graphic.size"));

    public static final double TILE_LOGICAL_SIZE =
            Double.valueOf(properties.getProperty("tile.logical.size"));
    public static final double BONUS_SIZE =
            Double.valueOf(properties.getProperty("bonus.size"));
    public static final double BOMB_SIZE =
            Double.valueOf(properties.getProperty("bomb.size"));
    public static  final double BOMB_SHIFT =
            Double.valueOf(properties.getProperty("bomb.shift"));
    public static final double EXPLOSION_UNIT_SIZE =
            Double.valueOf(properties.getProperty("explosion.unit.size"));
    public static final double PLAYER_HEIGHT =
            Double.valueOf(properties.getProperty("player.height"));
    public static final double PLAYER_WIDTH =
            Double.valueOf(properties.getProperty("player.width"));
    public static final double PLAYER_VELOCITY =
            Double.valueOf(properties.getProperty("player.velocity"));
    public static final double PLAYER_VELOCITY_DELTA =
            Double.valueOf(properties.getProperty("player.velocity.delta"));
    public static final double MAX_OVERLAP =
            Double.valueOf(properties.getProperty("max.overlap"));
    public static final double STRAFE_RADIUS =
            Double.valueOf(properties.getProperty("strafe.radius"));
    public static final double STRAFE_STEP =
            Double.valueOf(properties.getProperty("strafe.step"));

    public static final int TILES_HOR =
            Integer.valueOf(properties.getProperty("tiles.horizontal"));
    public static final int TILES_VERT =
            Integer.valueOf(properties.getProperty("tiles.vertical"));

    public static final long WALK_DURATION =
            Long.valueOf(properties.getProperty("walk.animation.duration"));
    public static final long BOMB_ANIMATION_DURATION =
            Long.valueOf(properties.getProperty("bomb.animation.duration"));
    public static final long EXPLOSION_ANIMATION_DURATION =
            Long.valueOf(properties.getProperty("explosion.animation.duration"));
    public static final long EXPLOSION_DURATION =
            Long.valueOf(properties.getProperty("explosion.duration"));
    public static final long BOMB_LIFE_TIME =
            Long.valueOf(properties.getProperty("bomb.life.time"));

    public static final String HEADER_IMAGE =
            properties.getProperty("header.image");
    public static final String BACKGROUND_TILE_URL =
            properties.getProperty("tile.background");
    public static final String SOLID_TILE_URL =
            properties.getProperty("tile.solid");
    public static final String EXPLODABLE_TILE_URL =
            properties.getProperty("tile.explodable");
    public static final String PORTAL_TILE_URL =
            properties.getProperty("tile.portal");

    public static final int PLAYER_FRAMES_NUM =
            Integer.valueOf(properties.getProperty("player.frames.num"));
    public static final String PLAYER_BACK_START =
            properties.getProperty("player.back.start");
    public static final  String PLAYER_BACK_END =
            properties.getProperty("player.back.end");
    public static final String PLAYER_FRONT_START =
            properties.getProperty("player.front.start");
    public static final  String PLAYER_FRONT_END =
            properties.getProperty("player.front.end");
    public static final String PLAYER_SIDE_START =
            properties.getProperty("player.side.start");
    public static final  String PLAYER_SIDE_END =
            properties.getProperty("player.side.end");

    public static final int BOMB_FRAMES =
            Integer.valueOf(properties.getProperty("bomb.frames.num"));
    public static final  String BOMB_START =
            properties.getProperty("bomb.start");
    public static final  String BOMB_END =
            properties.getProperty("bomb.end");

    public static final int EXPLOSION_FRAMES =
            Integer.valueOf(properties.getProperty("explosion.frames.num"));
    public static final  String EXPLOSION_START =
            properties.getProperty("explosion.start");
    public static final  String EXPLOSION_END =
            properties.getProperty("explosion.end");

    public static final String DEFAULT_SAVE =
            properties.getProperty("default.save.path");

    public static final double GLRATIO =
            (double)TILE_GRAPHIC_SIZE / (double)TILE_LOGICAL_SIZE;

    public static final int FIELD_HEIGHT =
            TILE_GRAPHIC_SIZE * TILES_VERT;
    public static final int FIELD_WIDTH =
            TILE_GRAPHIC_SIZE * TILES_HOR;
    public static final double LOGICAL_HEIGHT =
            TILES_VERT * TILE_LOGICAL_SIZE;
    public static final double LOGICAL_WIDTH =
            TILES_HOR * TILE_LOGICAL_SIZE;
}