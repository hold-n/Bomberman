package GameLogic;

import Controllers.MainMenuController;
import GameLogic.MapLoaders.FileLoader;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;

import static GameLogic.Config.*;

/**
 * Created by Max on 04.06.2015.
 */

public class Game {

    Stage stage;
    Parent mainMenu;
    GameWindow gameWindow;

    public Stage getStage() {
        return stage;
    }

    public Game(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle(TITLE);
        // TODO: allow resizing and track window size
        stage.setResizable(false);

        MainMenuController.bindTo(this);
    }

    public void run() throws IOException {
        getStage().getIcons().add(new Image(FAVICON, 32, 32, false, true));
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

    public void load() {
        gameWindow = GameWindow.load(this, DEFAULT_SAVE);
        if (gameWindow != null) {
            stage.setScene(gameWindow.getScene());
            gameWindow.run();
        }
    }

    public void exit() {
        System.exit(0);
    }
}
