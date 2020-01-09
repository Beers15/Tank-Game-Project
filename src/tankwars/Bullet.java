


import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Alexander Beers
 */
public class Bullet extends Entity {

    private int xVelocity;
    private int yVelocity;
    private int playerNum; //to mark which tank shot the bullet

    public Bullet(int xCoordinate, int yCoordinate, short angle, int playerNum, boolean powerUp) {
      
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.playerNum = playerNum;
        this.angle = angle;

        bounds.setLocation(xCoordinate, yCoordinate);
        bounds.setSize(12, 8); //smaller hitbox than tanks

        if (!powerUp) {
            try {
                //12px by 8px
                image = ImageIO.read(new File("src/Tank Wars resources/shell.png"));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            try {
                //23px by 22px
                image = ImageIO.read(new File("src/Tank Wars resources/shell2.png"));
                bounds.setSize(23, 22);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public short getAngle() {
        return angle;
    }

    public void updateBullet() {
        xVelocity = (int) Math.round(8 * Math.cos(Math.toRadians(angle)));
        yVelocity = (int) Math.round(8 * Math.sin(Math.toRadians(angle)));
        xCoordinate += xVelocity;
        yCoordinate += yVelocity;
        bounds.setLocation(xCoordinate, yCoordinate);
    }

    public int getPlayerNum() {
        return playerNum;
    }

}
