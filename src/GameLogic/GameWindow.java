package GameLogic;

import GameLogic.GameObjects.*;
import GameLogic.GameObjects.Bonuses.Bonus;
import GameLogic.GameObjects.HeaderObjects.HeaderImage;
import GameLogic.GameObjects.HeaderObjects.HeaderObject;
import GameLogic.GameObjects.HeaderObjects.HeaderTimer;
import GameLogic.GameObjects.Tiles.BackgroundTile;
import GameLogic.GameObjects.Tiles.Tile;
import GameLogic.MapLoaders.MapLoader;
import GameLogic.MapLoaders.TestMapLoader;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;

import static GameLogic.Config.*;

/**
* Created by Max on 03.06.2015.
*/

public class GameWindow {

    private final Game game;
    private final Scene scene;
    private final GraphicsContext headerContext;
    private final GraphicsContext fieldContext;

    private MapLoader loader = new TestMapLoader();

    private final KeyCode exitKey = KeyCode.F10;

    private final ArrayList<KeyCode> buttonCodes = new ArrayList<KeyCode>();

    // Contain all the objects on the field and the header
    private final ArrayList<HeaderObject> headerObjects = new ArrayList<HeaderObject>();
    private final ArrayList<FieldObject> toAddContainer = new ArrayList<FieldObject>();
    private final ArrayList<FieldObject> toRemoveContainver = new ArrayList<FieldObject>();
    private final ArrayList<FieldObject> objects = new ArrayList<FieldObject>();

    // Separate containers for specific objects
    private final ArrayList<Tile> map = new ArrayList<Tile>();
    private final ArrayList<Bonus> bonuses = new ArrayList<Bonus>();
    private final ArrayList<Player> players = new ArrayList<Player>();
    private final ArrayList<Bomb> bombs = new ArrayList<Bomb>();
    private final ArrayList<Explosion> explosions = new ArrayList<Explosion>();

    private long timerStartTime;

    // Controls: up, down, left, right
    private static final ArrayList<KeyCode> moveControls1 = new ArrayList<KeyCode>() {{
        add(KeyCode.UP);
        add(KeyCode.DOWN);
        add(KeyCode.LEFT);
        add(KeyCode.RIGHT);
    }};
    private static final KeyCode plantKey1 = KeyCode.SPACE;
    private static final ArrayList<KeyCode> moveControls2 = new ArrayList<KeyCode>() {{
        add(KeyCode.W);
        add(KeyCode.S);
        add(KeyCode.A);
        add(KeyCode.D);
    }};
    private static final KeyCode plantKey2 = KeyCode.SHIFT;

    AnimationTimer mainTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (!buttonCodes.contains(exitKey)) {
                for (FieldObject obj : toAddContainer)
                    addFromTemp(obj);
                toAddContainer.clear();
                for (FieldObject obj : toRemoveContainver)
                    removeFromTemp(obj);
                toRemoveContainver.clear();

                fieldContext.clearRect(0, 0, FIELD_WIDTH, FIELD_HEIGHT);
                headerContext.clearRect(0, 0, FIELD_WIDTH, HEADER_HEIGHT);
                handleInput(now);
                handleCollisions();
                updateObjects(now);
                drawObjects();
            }
            else {
                exit();
            }
        }
    };

    private void handleInput(long now) {
        Player player1 = null;
        Player player2 = null;
        for (Player player : getPlayers()) {
            if (player.getType() == PlayerType.PLAYER1)
                player1 = player;
            if (player.getType() == PlayerType.PLAYER2)
                player2 = player;
        }

        boolean walks;
        if (player1 != null) {
            if (buttonCodes.contains(plantKey1))
                player1.putBomb();
            walks = false;
            for (KeyCode code : buttonCodes) {
                if (moveControls1.contains(code)) {
                    if (code == moveControls1.get(0))
                        player1.walkByY(false, now);
                    if (code == moveControls1.get(1))
                        player1.walkByY(true, now);
                    if (code == moveControls1.get(2))
                        player1.walkByX(false, now);
                    if (code == moveControls1.get(3))
                        player1.walkByX(true, now);
                    walks = true;
                }
            }
            if (!walks)
                player1.stop();
        }

        if (player2 != null) {
            if (buttonCodes.contains(plantKey2))
                player2.putBomb();
            walks = false;
            for (KeyCode code : buttonCodes) {
                if (moveControls2.contains(code)) {
                    if (code == moveControls2.get(0))
                        player2.walkByY(false, now);
                    if (code == moveControls2.get(1))
                        player2.walkByY(true, now);
                    if (code == moveControls2.get(2))
                        player2.walkByX(false, now);
                    if (code == moveControls2.get(3))
                        player2.walkByX(true, now);
                    walks = true;
                }
            }
            if (!walks)
                player2.stop();
        }
    }

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
        for (FieldObject obj : objects)
            obj.draw(fieldContext);
        for (HeaderObject obj : headerObjects)
            obj.draw(headerContext);
    }

    protected void addFromTemp(FieldObject obj) {
        objects.add(obj);
        if (obj instanceof Tile)
            map.add((Tile)obj);
        if (obj instanceof Player)
            players.add((Player)obj);
        if (obj instanceof Bonus)
            bonuses.add((Bonus)obj);
        if (obj instanceof Explosion)
            explosions.add((Explosion)obj);
        if (obj instanceof Bomb)
            bombs.add((Bomb)obj);
    }

    protected void removeFromTemp(FieldObject obj) {
        objects.remove(obj);
        if (obj instanceof Tile)
            map.remove(obj);
        if (obj instanceof Player)
            players.remove(obj);
        if (obj instanceof Bonus)
            bonuses.remove(obj);
        if (obj instanceof Explosion)
            explosions.remove(obj);
        if (obj instanceof Bomb)
            bombs.remove(obj);
    }

    public Scene getScene() {
        return scene;
    }

    public void addObject(FieldObject obj) {
        toAddContainer.add(obj);
    }
    public void removeObject(FieldObject obj) {
        toRemoveContainver.add(obj);
    }

    public Iterable<KeyCode> getCodes() {
        return buttonCodes;
    }

    public Iterable<FieldObject> getObjects() {
        return objects;
    }
    public boolean toBeDeleted(FieldObject object) {
        return toRemoveContainver.contains(object);
    }
    public Iterable<Tile> getMap() {
        return map;
    }
    public Iterable<Player> getPlayers() {
        return players;
    }
    public Iterable<Bonus> getBonuses() {
        return bonuses;
    }
    public Iterable<Bomb> getBombs() {
        return bombs;
    }
    public Iterable<Explosion> getExplosions() {
        return explosions;
    }

    public GameWindow(Game thisGame, MapLoader thisLoader) {
        this(thisGame);
        loader = thisLoader;
    }

    public GameWindow(Game thisGame) {
        game = thisGame;
        game.getStage().hide();

        VBox root = new VBox();
        // -10 to removeFromField weird length and width excess
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
        loadBackground();
        // TODO: load players and creeps according to the map
        addObject(new Player(this, PlayerType.PLAYER1, 200, 200));
        addObject(new Player(this, PlayerType.PLAYER2, 300, 200));
    }

    private void loadBackground() {
        for (int i = 0; i < TILES_VERT; i++)
            for (int j = 0; j < TILES_HOR; j++) {
                Tile tile = new BackgroundTile(this, j*TILE_LOGICAL_SIZE, i*TILE_LOGICAL_SIZE);
                addObject(tile);
            }
        loader.loadMap(this);
    }

    private void loadHeaderObjects() {
        headerObjects.add(new HeaderImage());
        Font font = new Font("Verdana", HEADER_HEIGHT * 0.7);
        headerContext.setFont(font);
        headerContext.setStroke(Color.WHITE);
        headerObjects.add(new HeaderTimer(40, (int)(HEADER_HEIGHT * 0.77)));
        // TODO: add save and exit labels, timer
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

    public void run() {
        game.getStage().show();
        Game.centerStage(game.getStage());
        timerStartTime = System.nanoTime();
        mainTimer.start();
    }

    public void exit() {
        mainTimer.stop();
        try {
            game.runMainMenu();
        }
        catch (IOException e) {
            game.exit();
        }
        // TODO: maybe return a score or something
    }

    public void save() {
        // TODO
    }

    public static GameWindow load(Game game, String savePath) {
        // TODO
        return null;
    }
}