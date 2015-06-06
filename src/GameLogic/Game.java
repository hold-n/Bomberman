package GameLogic;

import Controllers.MainMenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import static GameLogic.Config.TITLE;

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
        stage.setResizable(false);

        MainMenuController.bindTo(this);
    }

    public void run() throws IOException {
        runMainMenu();
    }

    public void  runMainMenu() throws IOException {
        mainMenu = FXMLLoader.load(getClass().getResource("../Views/MainMenu.fxml"));
        Scene scene = new Scene(mainMenu);
        stage.setScene(scene);
        stage.show();
    }

    public void launchGameLoop() {
        gameWindow = new GameWindow(this);
        stage.setScene(gameWindow.getScene());
        gameWindow.run();
    }
}
