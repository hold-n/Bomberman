import GameLogic.Game;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Game game = new Game(primaryStage);
        game.run();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
