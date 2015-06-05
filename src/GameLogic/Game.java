package GameLogic;

import Controllers.MainMenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import static GameLogic.Config.*;

/**
 * Created by Max on 04.06.2015.
 */

public class Game {
    private Stage stage;

    public Game(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle(TITLE);
        stage.setResizable(false);

        MainMenuController.bindTo(this);
    }

    public Stage getStage() {
        return stage;
    }

    /**
     * <p>
     *     Essentially launches the game application.
     * </p>
     * @throws IOException
     */
    public void run() throws IOException {
        runMainMenu();
    }

    /**
     * <p>
     *     Runs the main menu of the game.
     * </p>
     * @throws IOException
     */
    public void runMainMenu() throws IOException {
        Parent mainMenu = FXMLLoader.load(getClass().getResource("../Views/MainMenu.fxml"));
        Scene scene = new Scene(mainMenu, MENU_WIDTH, MENU_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * <p>
     *     Starts the game loop bypassing the infrastructure. To properly
     *     launch the application, use run().
     * </p>
     */
    public void launchGameLoop() {
        GameWorld world = new GameWorld(this);
        world.run();
    }
}
