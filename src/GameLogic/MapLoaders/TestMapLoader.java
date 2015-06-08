package GameLogic.MapLoaders;

import GameLogic.GameObjects.Bonuses.Bonus;
import GameLogic.GameObjects.Bonuses.KickBonus;
import GameLogic.GameObjects.Bonuses.SpeedBonus;
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
        Tile tile = new SolidTile(gameWindow, TILE_LOGICAL_SIZE, TILE_LOGICAL_SIZE);
        gameWindow.addObject(tile);
        tile = new ExplodableTile(gameWindow, 2*TILE_LOGICAL_SIZE, TILE_LOGICAL_SIZE);
        gameWindow.addObject(tile);
        tile = new SolidTile(gameWindow, 2*TILE_LOGICAL_SIZE, 3*TILE_LOGICAL_SIZE);
        gameWindow.addObject(tile);
        tile = new ExplodableTile(gameWindow, 1*TILE_LOGICAL_SIZE, 3*TILE_LOGICAL_SIZE);
        gameWindow.addObject(tile);
        Bonus bonus = new SpeedBonus(gameWindow, 4*TILE_LOGICAL_SIZE, 4*TILE_LOGICAL_SIZE);
        gameWindow.addObject(bonus);
        bonus = new KickBonus(gameWindow, 5*TILE_LOGICAL_SIZE, 4*TILE_LOGICAL_SIZE);
        gameWindow.addObject(bonus);
        tile = new PortalTile(gameWindow, TILE_LOGICAL_SIZE, 6*TILE_LOGICAL_SIZE);
        gameWindow.addObject(tile);
    }
}
