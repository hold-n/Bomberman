package GameLogic;

import GameLogic.GameObjects.Bomb;
import GameLogic.GameObjects.Bonuses.Bonus;
import GameLogic.GameObjects.Explosion;
import GameLogic.GameObjects.FieldObject;
import GameLogic.GameObjects.HeaderObjects.HeaderImage;
import GameLogic.GameObjects.HeaderObjects.HeaderObject;
import GameLogic.GameObjects.Player;
import GameLogic.GameObjects.Tiles.BackgroundTile;
import GameLogic.GameObjects.Tiles.Tile;
import GameLogic.MapLoaders.MapLoader;
import GameLogic.MapLoaders.SimpleMapLoader;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
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
    private MapLoader loader = new SimpleMapLoader();

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

    private final GraphicsContext headerContext;
    private final GraphicsContext fieldContext;

    private long timerStartTime;

    AnimationTimer mainTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            for (FieldObject obj : toAddContainer)
                addFromTemp(obj);
            toAddContainer.clear();
            for (FieldObject obj : toRemoveContainver)
                removeFromTemp(obj);
            toRemoveContainver.clear();

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
            map.remove((Tile) obj);
        if (obj instanceof Player)
            players.remove((Player) obj);
        if (obj instanceof Bonus)
            bonuses.remove((Bonus) obj);
        if (obj instanceof Explosion)
            explosions.remove((Explosion) obj);
        if (obj instanceof Bomb)
            bombs.remove((Bomb) obj);
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
    public Scene getScene() {
        return scene;
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
        addObject(new Player(this, 200, 200));
    }

    private void loadHeaderObjects() {
        headerObjects.add(new HeaderImage());
        Font font = new Font("Verdana", 16);
        headerContext.setFont(font);
        // TODO: add save, exit buttons, timer and score
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
                addObject(tile);
            }
        loader.loadMap(this);
    }

    public void run() {
        game.getStage().show();
        timerStartTime = System.nanoTime();
        mainTimer.start();
    }

    public void exit() throws IOException {
        mainTimer.stop();
        game.runMainMenu();
        // TODO: maybe return a score or something
    }

    public void save() {
        // TODO
    }

    public static GameWindow load(Game game) {
        // TODO
        return null;
    }
}