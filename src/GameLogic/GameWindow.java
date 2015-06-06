package GameLogic;

import GameLogic.GameObjects.Bomb;
import GameLogic.GameObjects.Bonuses.Bonus;
import GameLogic.GameObjects.Explosion;
import GameLogic.GameObjects.FieldObject;
import GameLogic.GameObjects.Player;
import GameLogic.GameObjects.Tiles.BackgroundTile;
import GameLogic.GameObjects.Tiles.SolidTile;
import GameLogic.GameObjects.Tiles.Tile;
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
//    private void drawObjects() {
//        for (Tile tile : map) {
//            tile.draw(context);
//        }
//    }
//
//    public GameWindow(Game thisGame) {
//        game = thisGame;
//        game.getStage().hide();
//
//        Group root = new Group();
//        scene = new Scene(root);
//        game.getStage().setScene(scene);
//
//        Canvas headerCanvas = new Canvas(FIELD_WIDTH, HEADER_HEIGHT);
//        headerContext = headerCanvas.getGraphicsContext2D();
//        root.getChildren().add(headerCanvas);
//
//        Canvas fieldCanvas = new Canvas(FIELD_WIDTH, FIELD_HEIGHT);
//        context = fieldCanvas.getGraphicsContext2D();
//        root.getChildren().add(fieldCanvas);
//
//        loadMap();
//        // TODO: players, creeps
//    }
//
//    private void loadMap() {
//        for (int i = 0; i < TILES_VERT; i ++)
//            for (int j = 0; j < TILES_HOR; j++) {
//                Tile tile = new BackgroundTile(j* TILE_GRAPHIC_SIZE, i* TILE_GRAPHIC_SIZE);
//                map.add(tile);
//            }
//    }

    private final Game game;
    private final Scene scene;

    // Contrains all the objects on the field
    private final ArrayList<FieldObject> objects = new ArrayList<FieldObject>();
    // Separate containers for specific objects
    private final ArrayList<Tile> map = new ArrayList<Tile>();
    private final ArrayList<Bonus> bonuses = new ArrayList<Bonus>();
    private final ArrayList<Player> players = new ArrayList<Player>();
    private final ArrayList<Bomb> bombs = new ArrayList<Bomb>();
    private final ArrayList<Explosion> explosions = new ArrayList<Explosion>();

    private final GraphicsContext headerContext;
    private final GraphicsContext fieldContext;

    private long timerStartTime;

    AnimationTimer mainTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            fieldContext.clearRect(0, 0, FIELD_WIDTH, FIELD_HEIGHT);
            headerContext.clearRect(0, 0, FIELD_WIDTH, HEADER_HEIGHT);
            updateObjects(now);
            handleCollisions();
            drawObjects();
        }
    };

    public Iterable<FieldObject> getObjects() {
        return objects;
    }

    public GameWindow(Game thisGame) {
        game = thisGame;
        game.getStage().hide();

        Group root = new Group();
        scene = new Scene(root);
        game.getStage().setScene(scene);

        Canvas headerCanvas = new Canvas(FIELD_WIDTH, HEADER_HEIGHT);
        headerContext = headerCanvas.getGraphicsContext2D();
        root.getChildren().add(headerCanvas);

        Canvas fieldCanvas = new Canvas(FIELD_WIDTH, FIELD_HEIGHT);
        fieldContext = fieldCanvas.getGraphicsContext2D();
        root.getChildren().add(fieldCanvas);

        loadObjects();
    }

    private void loadObjects() {
        loadMap();
    }

    private void loadMap() {
        for (int i = 0; i < TILES_VERT; i++)
            for (int j = 0; j < TILES_HOR; j++) {
                Tile tile = new BackgroundTile(this, j*TILE_LOGICAL_SIZE, i*TILE_LOGICAL_SIZE);
                objects.add(tile);
                map.add(tile);
            }
//        Tile tile = new SolidTile(this, TILE_LOGICAL_SIZE, TILE_LOGICAL_SIZE);
//        objects.add(tile);
//        map.add(tile);

    }

    private void updateObjects(long now) {
        for (FieldObject obj : objects)
            obj.update(now);
    }

    private void handleCollisions() {
        for (FieldObject obj : objects)
            obj.checkCollisions();
    }

    private void drawObjects() {
        for (Tile tile : map)
            tile.draw(fieldContext);
        for (Bonus bonus : bonuses)
            bonus.draw(fieldContext);
        for (Bomb bomb : bombs)
            bomb.draw(fieldContext);
        for (Player player : players)
            player.draw(fieldContext);
        for (Explosion explosion : explosions)
            explosion.draw(fieldContext);
    }

    public Scene getScene() {
        return scene;
    }


    public void run() {
        game.getStage().show();
        timerStartTime = System.nanoTime();
        mainTimer.start();
    }
}

