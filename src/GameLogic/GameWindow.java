package GameLogic;

import GameLogic.GameObjects.*;
import GameLogic.GameObjects.Bonuses.Bonus;
import GameLogic.GameObjects.HeaderObjects.HeaderImage;
import GameLogic.GameObjects.HeaderObjects.HeaderObject;
import GameLogic.GameObjects.HeaderObjects.HeaderTimer;
import GameLogic.GameObjects.Tiles.BackgroundTile;
import GameLogic.GameObjects.Tiles.SolidTile;
import GameLogic.GameObjects.Tiles.Tile;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import static GameLogic.Config.*;

/**
* Created by Max on 03.06.2015.
*/

public class GameWindow {

    private final Game game;
    private final Scene scene;

    private final ArrayList<KeyCode> buttonCodes = new ArrayList<KeyCode>();
    // Contain all the objects on the field and the header
    private final ArrayList<HeaderObject> headerObjects = new ArrayList<HeaderObject>();
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

    private void updateObjects(long now) {
        for (FieldObject obj : objects)
            obj.update(now);
        for (HeaderObject obj : headerObjects)
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
        for (HeaderObject obj : headerObjects)
            obj.draw(headerContext);
    }

    public void addTile(Tile tile) {
        objects.add(tile);
        map.add(tile);
    }
    public void removeTile(Tile tile) {
        objects.remove(tile);
        map.add(tile);
    }
    public void addBonus(Bonus bonus) {
        objects.add(bonus);
        bonuses.add(bonus);
    }
    public void removeBonus(Bonus bonus) {
        objects.remove(bonus);
        bonuses.add(bonus);
    }
    public void addPlayer(Player player) {
        objects.add(player);
        players.add(player);
    }
    public void removePlayer(Player player) {
        objects.remove(player);
        players.add(player);
    }
    public void addBomb(Bomb bomb) {
        objects.add(bomb);
        bombs.add(bomb);
    }
    public void removeBomb(Bomb bomb) {
        objects.remove(bomb);
        bombs.add(bomb);
    }
    public void addExplosion(Explosion explosion) {
        objects.add(explosion);
        explosions.add(explosion);
    }
    public void removeExplosion(Explosion explosion) {
        objects.remove(explosion);
        explosions.add(explosion);
    }
    public Iterable<FieldObject> getObjects() {
        return objects;
    }
    public Iterable<KeyCode> getCodes() {
        return buttonCodes;
    }
    public Scene getScene() {
        return scene;
    }

    public GameWindow(Game thisGame) {
        game = thisGame;
        game.getStage().hide();

        VBox root = new VBox();
        scene = new Scene(root, FIELD_WIDTH - 10, HEADER_HEIGHT + FIELD_HEIGHT - 10);
        game.getStage().setScene(scene);

        Canvas headerCanvas = new Canvas(FIELD_WIDTH, HEADER_HEIGHT);
        headerContext = headerCanvas.getGraphicsContext2D();
        root.getChildren().add(headerCanvas);

        Canvas fieldCanvas = new Canvas(FIELD_WIDTH, FIELD_HEIGHT);
        fieldContext = fieldCanvas.getGraphicsContext2D();
        root.getChildren().add(fieldCanvas);

        loadFieldObjects();
        loadHeaderObjects();
        setHandlers();
    }

    private void loadFieldObjects() {
        loadMap();
        addPlayer(new Player(this, 200, 200));
    }

    private void loadHeaderObjects() {
        headerObjects.add(new HeaderImage());
        // TODO: set proper font
    }

    private void setHandlers() {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();
                if (!buttonCodes.contains(code))
                    buttonCodes.add(code);
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();
                buttonCodes.remove(code);
            }
        });
    }

    private void loadMap() {
        for (int i = 0; i < TILES_VERT; i++)
            for (int j = 0; j < TILES_HOR; j++) {
                Tile tile = new BackgroundTile(this, j*TILE_LOGICAL_SIZE, i*TILE_LOGICAL_SIZE);
                addTile(tile);
            }
        // TODO: load a proper map
        Tile tile = new SolidTile(this, TILE_LOGICAL_SIZE, TILE_LOGICAL_SIZE);
        addTile(tile);
    }

    public void run() {
        game.getStage().show();
        timerStartTime = System.nanoTime();
        mainTimer.start();
    }
}