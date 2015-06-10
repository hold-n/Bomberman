package GameLogic;

import Controllers.MainMenuController;
import GameLogic.MapLoaders.FileLoader;
import GameLogic.MapLoaders.MapLoader;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.Serializable;

import static GameLogic.Config.*;

/**
 * Created by Max on 04.06.2015.
 */

public class Game implements Serializable {

    private StageStyle style;
    private transient Stage stage;
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
        MainMenuController.bindTo(this);
        getStage().getIcons().add(new Image(FAVICON, 32, 32, false, true));
    }

    public void run() throws IOException {
        runMainMenu();
    }

    public void  runMainMenu() throws IOException {
        mainMenu = FXMLLoader.load(getClass().getResource("../Views/MainMenu.fxml"));
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
        // TODO
    }

    public void launchGameLoop() {
        gameWindow = new GameWindow(this, new FileLoader());
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
