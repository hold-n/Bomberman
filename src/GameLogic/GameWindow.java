package GameLogic;

import GameLogic.GameObjects.*;
import GameLogic.GameObjects.Bonuses.Bonus;
import GameLogic.GameObjects.HeaderObjects.HeaderImage;
import GameLogic.GameObjects.HeaderObjects.HeaderLabel;
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

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import static GameLogic.Config.*;

/**
* Created by Max on 03.06.2015.
*/

public class GameWindow implements Serializable {

    private transient Game game;
    private transient Scene scene;
    private transient GraphicsContext headerContext;
    private transient GraphicsContext fieldContext;

    // TODO: remove this reference
    private MapLoader loader;

    private final KeyCode exitKey = KeyCode.F10;
    private final KeyCode saveKey = KeyCode.F2;

    private final ArrayList<KeyCode> buttonCodes = new ArrayList<KeyCode>();

    // Contain all the objects on the field and the header
    private ArrayList<HeaderObject> headerObjects = new ArrayList<>();
    private ArrayList<FieldObject> toAddContainer = new ArrayList<>();
    private ArrayList<FieldObject> toRemoveContainver = new ArrayList<>();
    private ArrayList<FieldObject> objects = new ArrayList<>();

    // Separate containers for specific objects
    private ArrayList<BackgroundTile> background = new ArrayList<>();
    private ArrayList<Tile> map = new ArrayList<>();
    private ArrayList<Bonus> bonuses = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Bomb> bombs = new ArrayList<>();
    private ArrayList<Explosion> explosions = new ArrayList<>();

    private transient long timerStartTime;
    private long lifeTime;
    private PlayerType playerDied = null;

    // TODO: A lameass decision, fix
    private double timerRatio = 0.03;
    private double saveMessageRatio = 0.63;
    private double exitMessageRatio = 0.82;
    private int fontSize = 100;

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

    private transient AnimationTimer mainTimer;

    public void setGame(Game newGame) {
        game = newGame;
    }
    public Game getGame() {
        return game;
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
        loader = thisLoader;
        game = thisGame;
        initializeWindow(false);
    }

    public GameWindow(Game thisGame) {
        this(thisGame, new TestMapLoader());
    }

    /**
     * Initializes the instance of the class so that it would work correctly.
     * Called internally, only to call manually after deserialization.
     */
    public void initializeWindow(boolean deserialized) {
        game.getStage().hide();

        buttonCodes.clear();

        VBox root = new VBox();
        // -10 to removeFromField weird length and width excess
        scene = new Scene(root, FIELD_WIDTH - 10, HEADER_HEIGHT + FIELD_HEIGHT - 10);
        game.getStage().setScene(scene);

        Canvas headerCanvas = new Canvas(FIELD_WIDTH, HEADER_HEIGHT);
        headerContext = headerCanvas.getGraphicsContext2D();
        root.getChildren().add(headerCanvas);

        Font font = new Font("Verdana", HEADER_HEIGHT * 0.7);
        headerContext.setFont(font);
        headerContext.setStroke(Color.WHITE);

        Canvas fieldCanvas = new Canvas(FIELD_WIDTH, FIELD_HEIGHT);
        fieldContext = fieldCanvas.getGraphicsContext2D();
        root.getChildren().add(fieldCanvas);

        // TODO: move somewhere
        fieldContext.setFont(new Font("Verdana", fontSize));
        fieldContext.setStroke(Color.RED);
        fieldContext.setFill(Color.BLACK);

        if (!deserialized) {
            loadFieldObjects();
            loadHeaderObjects();
        }
        setHandlers();
    }

    private void initializeTimer() {
        mainTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                lifeTime = now - timerStartTime;
                if (!buttonCodes.contains(exitKey)) {
                    if (buttonCodes.contains(saveKey))
                        save();
                    toAddContainer.forEach(GameWindow.this::addFromTemp);
                    toAddContainer.clear();
                    toRemoveContainver.forEach(GameWindow.this::removeFromTemp);
                    toRemoveContainver.clear();

                    fieldContext.clearRect(0, 0, FIELD_WIDTH, FIELD_HEIGHT);
                    headerContext.clearRect(0, 0, FIELD_WIDTH, HEADER_HEIGHT);
                    if (playerDied == null) {
                        handleInput(now);
                    }
                    handleCollisions();
                    updateObjects(now);
                    drawObjects();
                } else {
                    exit();
                }
            }
        };
        mainTimer.start();
    }

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
                        player1.moveByY(false, now);
                    if (code == moveControls1.get(1))
                        player1.moveByY(true, now);
                    if (code == moveControls1.get(2))
                        player1.moveByX(false, now);
                    if (code == moveControls1.get(3))
                        player1.moveByX(true, now);
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
                        player2.moveByY(false, now);
                    if (code == moveControls2.get(1))
                        player2.moveByY(true, now);
                    if (code == moveControls2.get(2))
                        player2.moveByX(false, now);
                    if (code == moveControls2.get(3))
                        player2.moveByX(true, now);
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
        objects.forEach(GameLogic.GameObjects.FieldObject::checkCollisions);
    }

    private void drawObjects() {
        for (BackgroundTile tile : background)
            tile.draw(fieldContext);
        for (Tile tile : map)
            tile.draw(fieldContext);
        for (Bonus bonus : bonuses)
            bonus.draw(fieldContext);
        for (Bomb bomb : bombs)
            bomb.draw(fieldContext);
        for (Explosion explosion : explosions)
            explosion.draw(fieldContext);
        for (Player player : players)
            player.draw(fieldContext);
        for (HeaderObject obj : headerObjects)
            obj.draw(headerContext);
        if (playerDied != null) {
            int x = (int)(TILE_GRAPHIC_SIZE * TILES_HOR * 0.2);
            int y = (TILE_GRAPHIC_SIZE * TILES_VERT)/2 + (int)fieldContext.getFont().getSize()/2;
            Integer won = playerDied == PlayerType.PLAYER1 ? 2 : 1;
            fieldContext.strokeText("Player " + won.toString() + " won!", x, y);
            fieldContext.fillText("Player " + won.toString() + " won!", x, y);
        }
    }

    protected void addFromTemp(FieldObject obj) {
        objects.add(obj);
        if (obj instanceof BackgroundTile)
            background.add((BackgroundTile)obj);
        else if (obj instanceof Tile)
            map.add((Tile)obj);
        else if (obj instanceof Player)
            players.add((Player)obj);
        else if (obj instanceof Bonus)
            bonuses.add((Bonus)obj);
        else if (obj instanceof Explosion)
            explosions.add((Explosion)obj);
        else if (obj instanceof Bomb)
            bombs.add((Bomb)obj);
    }

    protected void removeFromTemp(FieldObject obj) {
        objects.remove(obj);
        if (obj instanceof BackgroundTile)
            background.remove(obj);
        else if (obj instanceof Tile)
            map.remove(obj);
        else if (obj instanceof Player)
            players.remove(obj);
        else if (obj instanceof Bonus)
            bonuses.remove(obj);
        else if (obj instanceof Explosion)
            explosions.remove(obj);
        else if (obj instanceof Bomb)
            bombs.remove(obj);
    }

    private void loadFieldObjects() {
        loader.loadMap(this);
        loadBackground();
    }

    private void loadBackground() {
        for (int i = 0; i < TILES_VERT; i++)
            for (int j = 0; j < TILES_HOR; j++) {
                Tile tile = new BackgroundTile(this, j*TILE_LOGICAL_SIZE, i*TILE_LOGICAL_SIZE);
                addObject(tile);
            }
    }

    private void loadHeaderObjects() {
        headerObjects.add(new HeaderImage());
        headerObjects.add(new HeaderTimer(timerRatio));
        headerObjects.add(new HeaderLabel("F2 to save", saveMessageRatio));
        headerObjects.add(new HeaderLabel("F10 to exit", exitMessageRatio));
        // TODO: add save notification
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
        initializeTimer();
    }

    public void exit() {
        mainTimer.stop();
        game.runMainMenu();
        // TODO: maybe return a score or something
    }

    public void playerDied(PlayerType type) {
        // TODO
        playerDied = type;
    }

    // TODO: needed?
    private static final long serialVersionUID = 7526472295622776147L;

    public void save() {
        try {
            URL resource = Config.class.getResource(DEFAULT_SAVE);
            File file;
            try {
                file = new File(resource.toURI());
            }
            catch (URISyntaxException e) {
                e.printStackTrace();
                return;
            }
            FileOutputStream fileStream = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileStream);
            out.writeObject(this);
            out.close();
            fileStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            // TODO: make it notify
        }
    }

    public static GameWindow load(String savePath) {
        try {
            InputStream input = GameWindow.class.getResourceAsStream(DEFAULT_SAVE);
            ObjectInputStream in = new ObjectInputStream(input);
            GameWindow result = (GameWindow)in.readObject();
            return result;
        }
        catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return  null;
        }
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        timerStartTime = System.nanoTime() - lifeTime;
    }

    public void setLoaderAndLoad(MapLoader newLoader) {
        game.reload(loader);
    }
}