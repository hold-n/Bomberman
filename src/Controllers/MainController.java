package Controllers; // hello

import GameLogic.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Created by Max on 04.06.2015.
 */

public class MainController {
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
    private void closeAbout(ActionEvent actionEvent) {
        game.closeAbout();
    }

    @FXML
    private void load(ActionEvent actionEvent) {
        game.load();
    }
}