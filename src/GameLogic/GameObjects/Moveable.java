package GameLogic.GameObjects;

/**
 * Created by Max on 05.06.2015.
 */
public interface Moveable {
    public double getVelocityX();
    public void setVelocity(double vx, double vy);
    public double getVelocityY();
    public void updatePositiion();
    public boolean Intersects(FieldObject obj);
}
