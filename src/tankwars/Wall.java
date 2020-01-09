


import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Alexander Beers
 */
public class Wall extends Entity {

    private boolean isFragile;

    public Wall(int xCoordinate, int yCoordinate, boolean isFragile) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.isFragile = isFragile;

        if (!isFragile) {
            try {
                image = ImageIO.read(new File("src/Tank Wars resources/Wall.png"));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            try {
                image = ImageIO.read(new File("src/Tank Wars resources/FragileWall.png"));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public String toString() {
        if (isFragile) {
            return "FragileWall";
        }

        return "Wall";
    }
}
