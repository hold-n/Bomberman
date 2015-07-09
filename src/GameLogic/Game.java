package GameLogic;

import Controllers.MainController;
import GameLogic.MapLoaders.FileMapLoader;
import GameLogic.MapLoaders.MapLoader;
import GameLogic.MapLoaders.TestMapLoader;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import static GameLogic.Config.*;

/**
 * Created by Max on 04.06.2015.
 */

public class Game implements Serializable {

    private StageStyle style;
    private transient Stage stage;
    private transient Stage tempStage;
    private transient Parent mainMenu;
    private GameWindow gameWindow;

    public Stage getStage() {
        return stage;
    }

    public Game(Stage primaryStage) {
        style = primaryStage.getStyle();
        stage = primaryStage;

        initialize();
    }

    public void initialize() {
        // TODO: allow resizing and track window size
        stage.setTitle(TITLE);
        stage.setResizable(false);
        MainController.bindTo(this);
        getStage().getIcons().add(new Image(FAVICON, 32, 32, false, true));
    }

    public void run() throws IOException {
        runMainMenu();
    }

    public void  runMainMenu() {
        try {
            URL resourse = Game.class.getResource("/Views/MainMenu.fxml");
            mainMenu = FXMLLoader.load(resourse);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Scene scene = new Scene(mainMenu);
        stage.setScene(scene);
        stage.show();
        centerStage(stage);
    }

    public static void centerStage(Stage stageToCenter) {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stageToCenter.setX((primScreenBounds.getWidth() - stageToCenter.getWidth()) / 2);
        stageToCenter.setY((primScreenBounds.getHeight() - stageToCenter.getHeight()) / 2);
    }

    public void about() {
        Parent aboutMenu;
        URL resource = Game.class.getResource("/Views/About.fxml");
        try {
            aboutMenu = FXMLLoader.load(resource);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        tempStage = new Stage();
        tempStage.setResizable(false);
        tempStage.setTitle("About");
        tempStage.getIcons().add(new Image(FAVICON, 32, 32, false, true));
        Scene scene = new Scene(aboutMenu);
        tempStage.setScene(scene);
        tempStage.show();
        centerStage(tempStage);
    }

    public void closeAbout() {
        tempStage.close();
    }

    public void launchGameLoop() {
        gameWindow = new GameWindow(this, new FileMapLoader());
        stage.setScene(gameWindow.getScene());
        gameWindow.run();
    }

    public void reload(MapLoader loader) {
        gameWindow = new GameWindow(this, loader);
        stage.setScene(gameWindow.getScene());
        gameWindow.run();
    }

    public void load() {
        gameWindow = GameWindow.load(DEFAULT_SAVE);

        if (gameWindow != null) {
            gameWindow.setGame(this);
            gameWindow.initializeWindow(true);
            gameWindow.run();
        }
    }

    public void exit() {
        System.exit(0);
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        stage = new Stage(style);
    }
}
