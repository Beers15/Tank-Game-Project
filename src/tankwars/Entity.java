


import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 *
 * @author Alexander Beers
 */
public class Entity extends Observable {

    protected BufferedImage image;
    protected int xCoordinate;
    protected int yCoordinate;
    protected short angle = 0;
    protected Rectangle bounds = new Rectangle(0, 0, 25, 25);

    public Entity() {
    }

    protected int getXCoordinate() {
        return xCoordinate;
    }

    protected int getYCoordinate() {
        return yCoordinate;
    }

    protected BufferedImage getImage() {
        return image;
    }

    @Override
    protected synchronized void setChanged() {
        super.setChanged();
    }

    //for collision checking
    public boolean collisionChk(Entity tankOrBullet) {
        return bounds.intersects(tankOrBullet.bounds);
    }

    public void initBounds() {
        bounds.setLocation(xCoordinate, yCoordinate);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public short getAngle() {
        return angle;
    }
}
