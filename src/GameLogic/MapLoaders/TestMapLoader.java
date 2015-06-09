package GameLogic.MapLoaders;

import GameLogic.GameObjects.Bonuses.*;
import GameLogic.GameObjects.Player;
import GameLogic.GameObjects.PlayerType;
import GameLogic.GameObjects.Tiles.ExplodableTile;
import GameLogic.GameObjects.Tiles.PortalTile;
import GameLogic.GameObjects.Tiles.SolidTile;
import GameLogic.GameObjects.Tiles.Tile;
import GameLogic.GameWindow;

import static GameLogic.Config.TILE_LOGICAL_SIZE;

/**
 * Created by Max on 07.06.2015.
 */

public class TestMapLoader implements MapLoader {
    @Override
    public void loadMap(GameWindow gameWindow) {
        gameWindow.addObject(new SolidTile(gameWindow, TILE_LOGICAL_SIZE, TILE_LOGICAL_SIZE));
        gameWindow.addObject(new ExplodableTile(gameWindow, 2*TILE_LOGICAL_SIZE, TILE_LOGICAL_SIZE));
        gameWindow.addObject(new SolidTile(gameWindow, 2*TILE_LOGICAL_SIZE, 3*TILE_LOGICAL_SIZE));
        gameWindow.addObject(new ExplodableTile(gameWindow, 1*TILE_LOGICAL_SIZE, 3*TILE_LOGICAL_SIZE));

        gameWindow.addObject(new PortalTile(gameWindow, TILE_LOGICAL_SIZE, 6*TILE_LOGICAL_SIZE));

        gameWindow.addObject(new BombBonus(gameWindow, 3*TILE_LOGICAL_SIZE, 5*TILE_LOGICAL_SIZE));
        gameWindow.addObject(new DebuffBonus(gameWindow, 10*TILE_LOGICAL_SIZE, 2*TILE_LOGICAL_SIZE));
        gameWindow.addObject(new ExplosionBonus(gameWindow, 3*TILE_LOGICAL_SIZE,6*TILE_LOGICAL_SIZE));
        gameWindow.addObject(new SpeedBonus(gameWindow, 4*TILE_LOGICAL_SIZE, 4*TILE_LOGICAL_SIZE));
        gameWindow.addObject(new KickBonus(gameWindow, 5*TILE_LOGICAL_SIZE, 4*TILE_LOGICAL_SIZE));

        gameWindow.addObject(new Player(gameWindow, PlayerType.PLAYER1, 200, 200));
        gameWindow. addObject(new Player(gameWindow, PlayerType.PLAYER2, 300, 200));
    }
}
