package GameLogic;

import GameLogic.GameObjects.Tile;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

import static GameLogic.Config.*;

/**
* Created by Max on 03.06.2015.
*/

public class GameWindow {
    private final ArrayList<Tile> map = new ArrayList<Tile>();
    final Game game;
    final Scene scene;
    final GraphicsContext context;

    long startTime;
    AnimationTimer mainTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            context.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
            updateObjects();
            checkIntersections();
            drawObjects();
            Integer time = (int)((System.nanoTime() - startTime) / 1000000000L);
            context.fillText(time.toString(), 50, 50);
        }
    };

    private void updateObjects() {

    }

    private void drawObjects() {

    }

    private void checkIntersections() {

    }

    public Game getGame() { return game; }
    public Scene getScene() { return scene; }

    public GameWindow(Game thisGame) {
        game = thisGame;
        game.getStage().hide();

        Group root = new Group();
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        game.getStage().setScene(scene);

        Canvas fieldCanvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        context = fieldCanvas.getGraphicsContext2D();
        root.getChildren().add(fieldCanvas);
        startTime = System.nanoTime();

        // TODO: load the field
    }

    public void run() {
        game.getStage().show();
        mainTimer.start();
    }
}

