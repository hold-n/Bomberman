package GameLogic;

import static GameLogic.Config.*;

/**
 * Created by Max on 06.06.2015.
 */

public class GameValue {
    protected double logicalValue;
    protected int graphicValue;
    protected static final double glRatio =
            (double)TILE_GRAPHIC_SIZE / (double)TILE_LOGICAL_SIZE;

    public GameValue(double value) {
        logicalValue = value;
        graphicValue = (int)(value * glRatio);
    }

    public void setValue(double value) {
        logicalValue = value;
        graphicValue = (int)(value * glRatio);
    }

    public double getLogical() {
        return logicalValue;
    }

    public int getGraphic() {
        return graphicValue;
    }
}
