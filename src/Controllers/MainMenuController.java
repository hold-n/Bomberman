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
        System.exit(0);
    }

    @FXML
    private Double windowHeight() { return (double) MENU_HEIGHT; }
}