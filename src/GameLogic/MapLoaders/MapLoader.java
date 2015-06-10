package GameLogic.MapLoaders;

import GameLogic.GameWindow;

import java.io.Serializable;

/**
 * Created by Max on 07.06.2015.
 */
public interface MapLoader extends Serializable {
    public void loadMap(GameWindow gameWindow);
}
