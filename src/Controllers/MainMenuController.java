package Controllers;

import GameLogic.Game;
import javafx.event.ActionEvent;


/**
 * Created by Max on 04.06.2015.
 */

public class MainMenuController {
    private static Game game;

    public static void bindTo(Game gameToBind) {
        game = gameToBind;
    }

    public void runGame(ActionEvent event) {
        game.launchGameLoop();
    }

    public void exitGame(ActionEvent event) {
        System.exit(0);
    }
}