


import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Alexander Beers
 */
public class PowerUp extends Entity {
  
    public PowerUp(int xCoordinate, int yCoordinate) {
        //maybe add another parameter for different kinds of powerups later, randomly assigned in gameArena
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
  
        try {
            image = ImageIO.read(new File("src/Tank Wars resources/PowerUp.png"));
        } catch (IOException e) {
              System.out.println(e.getMessage());
           }
    } 
  
    public String toString() {
      return "powerUp";
    }
}
  
  

