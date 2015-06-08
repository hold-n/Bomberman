package GameLogic;

import static GameLogic.Config.*;

/**
 * Created by Max on 06.06.2015.
 */

public class GameValue {
    protected double logicalValue;
    protected int graphicValue;

    public GameValue() {
        logicalValue = 0;
        graphicValue = 0;
    }

    public GameValue(double value) {
        logicalValue = value;
        graphicValue = (int)(value * GLRATIO);
    }

    public void setValue(double value) {
        logicalValue = value;
        graphicValue = (int)(value * GLRATIO);
    }

    public void add(double value) {
        logicalValue += value;
        graphicValue = (int)(logicalValue * GLRATIO);
    }

    public double getLogical() {
        return logicalValue;
    }

    public int getGraphic() {
        return graphicValue;
    }
}
