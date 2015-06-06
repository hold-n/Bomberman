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

    public static final int TILES_HOR =
            Integer.valueOf(properties.getProperty("tiles.horizontal"));
    public static final int TILES_VERT =
            Integer.valueOf(properties.getProperty("tiles.vertical"));
    public static final int TILE_GRAPHIC_SIZE =
            Integer.valueOf(properties.getProperty("tile.size"));
    public static final int TILE_LOGICAL_SIZE =
            Integer.valueOf(properties.getProperty("tile.logical.size"));

    public static final int FIELD_HEIGHT =
            TILE_GRAPHIC_SIZE * TILES_VERT;
    public static final int FIELD_WIDTH =
            TILE_GRAPHIC_SIZE * TILES_HOR;
    public static final int LOGICAL_HEIGHT =
            TILES_VERT * TILE_LOGICAL_SIZE;
    public static final int LOGICAL_WIDTH =
            TILES_HOR * TILE_LOGICAL_SIZE;

    public static final String BACKGROUND_TILE_URL =
            properties.getProperty("background.tile");
    public static final String SOLID_TILE_URL =
            properties.getProperty("solid.tile");
}