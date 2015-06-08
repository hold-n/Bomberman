package Controllers;

import GameLogic.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import static GameLogic.Config.MENU_HEIGHT;
import static GameLogic.Config.MENU_WIDTH;


/**
 * Created by Max on 04.06.2015.
 */

public class MainMenuController {
    private static Game game;

    public static void bindTo(Game gameToBind) {
        game = gameToBind;
    }

    @FXML
    private void runGame(ActionEvent event) {
        game.launchGameLoop();
    }

    @FXML
    private void exitGame(ActionEvent event) {
        game.exit();
    }

    @FXML
    private void about(ActionEvent actionEvent) {
        game.about();
    }

    @FXML
    private void load(ActionEvent actionEvent) {
        game.load();
    }
}