package GameLogic.MapLoaders;

import GameLogic.GameObjects.Player;
import GameLogic.GameObjects.PlayerType;
import GameLogic.GameObjects.Tiles.ExplodableTile;
import GameLogic.GameObjects.Tiles.PortalTile;
import GameLogic.GameObjects.Tiles.SolidTile;
import GameLogic.GameWindow;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import static GameLogic.Config.*;

/**
 * Created by Max on 09.06.2015.
 */
public class FileMapLoader implements MapLoader {
    @Override
    public void loadMap(GameWindow gameWindow) {
        try{
            InputStream reader = FileMapLoader.class.getResourceAsStream(MAP1);

            int portalCount = -1;
            char sign;
            int count;
            Player player1 = null;
            Player player2 = null;
            PortalTile[] portals = new PortalTile[TILES_HOR * TILES_VERT];
            for (int i = 0; i < TILES_VERT; i++) {
                for (int j = 0; j < TILES_HOR; j++) {
                    sign = (char) reader.read();
                    switch (sign) {
                        case 's':
                        case 'S':
                            gameWindow.addObject(new SolidTile(gameWindow, j*TILE_LOGICAL_SIZE, i*TILE_LOGICAL_SIZE));
                            break;
                        case 'e':
                        case 'E':
                            gameWindow.addObject(new ExplodableTile(gameWindow, j*TILE_LOGICAL_SIZE, i*TILE_LOGICAL_SIZE));
                            break;
                        case 'p':
                        case 'P':
                            count = Character.getNumericValue(reader.read());
                            if (count > portalCount) portalCount = count;
                            portals[count] = new PortalTile(gameWindow, j*TILE_LOGICAL_SIZE, i*TILE_LOGICAL_SIZE);
                            gameWindow.addObject(portals[count]);
                            break;
                        case 'r':
                        case 'R':
                            count = Character.getNumericValue(reader.read());
                            if (count == 2)
                                player2 =
                                        new Player(gameWindow, PlayerType.PLAYER2, j * TILE_LOGICAL_SIZE, i * TILE_LOGICAL_SIZE);
                            else
                                player1 =
                                        new Player(gameWindow, PlayerType.PLAYER1, j * TILE_LOGICAL_SIZE, i * TILE_LOGICAL_SIZE);
                            break;
                    }
                }
                // skipping line break
                reader.read();
                reader.read();
            }
            if (player1 != null)
                gameWindow.addObject(player1);
            if (player2 != null)
                gameWindow.addObject(player2);
            for (int i = 0; i < portalCount; i++)
                portals[i].setDestination(portals[i + 1].getX(), portals[i + 1].getY());
            portals[portalCount].setDestination(portals[0].getX(), portals[0].getY());
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
