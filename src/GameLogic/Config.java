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
    public static final int WINDOW_HEIGHT =
            Integer.valueOf(properties.getProperty("window.height"));
    public static final int WINDOW_WIDTH =
            Integer.valueOf(properties.getProperty("window.width"));
    public static final int HEADER_HEIGHT =
            Integer.valueOf(properties.getProperty("header.height"));
}