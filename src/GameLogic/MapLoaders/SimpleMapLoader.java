package GameLogic.MapLoaders;

import GameLogic.GameObjects.Tiles.ExplodableTile;
import GameLogic.GameObjects.Tiles.SolidTile;
import GameLogic.GameObjects.Tiles.Tile;
import GameLogic.GameWindow;
import GameLogic.MapLoaders.MapLoader;

import static GameLogic.Config.TILE_LOGICAL_SIZE;

/**
 * Created by Max on 07.06.2015.
 */

public class SimpleMapLoader implements MapLoader {
    @Override
    public void loadMap(GameWindow gameWindow) {
        Tile tile = new SolidTile(gameWindow, TILE_LOGICAL_SIZE, TILE_LOGICAL_SIZE);
        gameWindow.addObject(tile);
        tile = new ExplodableTile(gameWindow, 2*TILE_LOGICAL_SIZE, TILE_LOGICAL_SIZE);
        gameWindow.addObject(tile);
        tile = new SolidTile(gameWindow, 2*TILE_LOGICAL_SIZE, 3*TILE_LOGICAL_SIZE);
        gameWindow.addObject(tile);
        tile = new ExplodableTile(gameWindow, 1*TILE_LOGICAL_SIZE, 3*TILE_LOGICAL_SIZE);
        gameWindow.addObject(tile);
    }
}
