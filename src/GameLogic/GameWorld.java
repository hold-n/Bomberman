package GameLogic;

import javafx.scene.Group;
import javafx.scene.Scene;
import static GameLogic.Config.*;

/**
 * Created by Max on 03.06.2015.
 */

public class GameWorld {
    Game game;

    public GameWorld(Game thisGame) {
        game = thisGame;

        Group root = new Group();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        game.getStage().setScene(scene);
    }

    public void run() {
        // TODO: implement the game loop
    }
}
