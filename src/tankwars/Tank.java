


import java.awt.geom.AffineTransform;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Tank extends Entity implements Observer {
  
    private final double  r = 2.0; 
    private int xCoordinate;
    private int yCoordinate;
    private short angle;
    private int xVelocity;
    private int yVelocity;
    private boolean goUp, goDown, goLeft, goRight; 
    private AffineTransform rotation;
    private Dimension screenSize = new Dimension(1600, 800);
    private boolean collision = false;
    private int playerNum;
    private BufferedImage tankImage;
    private int numLives = 3;
    private int lifeTier = 12;
    private Rectangle otherObjBounds = new Rectangle(0, 0, 25, 25);
    protected boolean powerUp = false;
    protected boolean isExploded = false;
    protected int count = 0;
    private Dimension arenaSize = new Dimension(1600, 800);
    protected ArrayList<Entity> playerBullets = new ArrayList<Entity>(10);
    
    public Tank(int playerNum, int xCoordinate, int yCoordinate, int xVelocity, int yVelocity, short angle) {
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.angle = angle;
        this.playerNum = playerNum;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        //for collision checking
        bounds.setLocation(xCoordinate, yCoordinate);

        if (playerNum == 1) {
            try {
                tankImage = ImageIO.read(new File("src/Tank Wars resources/tank1.png"));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            try {
                tankImage = ImageIO.read(new File("src/Tank Wars resources/tank2.png"));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //Getters
    public BufferedImage getImage() {
        return tankImage;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public int getLives() {
        return numLives;
    }

    public int getLifeTeir() {
        return lifeTier;
    }
    
    public short getAngle() {
        return angle;
    }
    //Setters
    public void setLives(int numLives) {
        this.numLives = numLives;
    }

    public void setLifeTier(int lifeTier) {
        this.lifeTier = lifeTier;
        //if player loses a life, reset position
        if(lifeTier == 0) {
            if(playerNum == 1) {
                xCoordinate = 50;
                yCoordinate = 50;
            }
            else {
                xCoordinate = arenaSize.width - 75;
                yCoordinate = arenaSize.height - 75;
            }
        }
    }

    public void setXCoordinate(int xCoordinate) {
        if (this.xCoordinate != xCoordinate) {
            this.xCoordinate = xCoordinate;
        }
    }

    public void setYCoordinate(int yCoordinate) {
        if (this.yCoordinate != yCoordinate) {
            this.yCoordinate = yCoordinate;
        }
    }

    public void setXVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }

    public void setYVelocity(int yVelocity) {
        this.yVelocity = yVelocity;
    }

    //Key responses
    public void upPress() {
        goUp = true;
    }

    public void downPress() {
        this.goDown = true;
    }

    public void rightPress() {
        this.goRight = true;
    }

    public void leftPress() {
        this.goLeft = true;
    }

    public void upRelease() {
        this.goUp = false;
    }

    public void downRelease() {
        this.goDown = false;
    }

    public void rightRelease() {
        this.goRight = false;
    }

    public void leftRelease() {
        this.goLeft = false;
    }
    
    public String toString() {
      return "Tank";
    }

    public void takeDamage() {
        int life = lifeTier;
        life--;
        setLifeTier(life);
    }

    //Tank object tracks and updates its own location info and angle 
    @Override
    public void update(Observable o, Object o1) {   
        if(this.goUp) {
            this.moveForwards();
        }
        if(this.goDown) {
            this.moveBackwards();
        }    
        if(this.goLeft) {
            this.rotateLeft();
        }
        if(this.goRight) {
            this.rotateRight();
        }
        bounds.setLocation(xCoordinate, yCoordinate);
       
       //powerUps only last a limited time, when tank moves time goes down
       count++;
       if(count % 2000 == 0)
           powerUp = false;
    }

    //Tank object contains and updates its own location info and angle thru update
    private void rotateLeft() {
        this.angle -= 3;
    }

    private void rotateRight() {
        this.angle += 3;
    }

    private void moveForwards() {
        if(!collision) {
            xVelocity = (int) Math.round(r * Math.cos(Math.toRadians(angle)));
            yVelocity = (int) Math.round(r * Math.sin(Math.toRadians(angle)));
            xCoordinate += xVelocity;
            yCoordinate += yVelocity;
            checkBorder();
        }  
        if(collision) {
            if(xCoordinate < otherObjBounds.getX())
                xCoordinate -= 2; 
            if(yCoordinate < otherObjBounds.getY() ) 
                yCoordinate -= 2; 
            if(xCoordinate > otherObjBounds.getX())
                xCoordinate += 2;             
            if(yCoordinate > otherObjBounds.getY())
                yCoordinate += 2;
            
            checkBorder();
            collision = false;   
        }
        bounds.setLocation(xCoordinate, yCoordinate);
    }

    private void moveBackwards() {
        if(!collision) {
            xVelocity = (int) Math.round(r * Math.cos(Math.toRadians(angle)));
            yVelocity = (int) Math.round(r * Math.sin(Math.toRadians(angle)));
            xCoordinate -= xVelocity;
            yCoordinate -= yVelocity;
          
            checkBorder();
        }
        if(collision) {
            if(xCoordinate < otherObjBounds.getX())
                xCoordinate -= 2; 
            if(yCoordinate < otherObjBounds.getY() ) 
                yCoordinate -= 2; 
            if(xCoordinate > otherObjBounds.getX())
                xCoordinate += 2;             
            if(yCoordinate > otherObjBounds.getY())
                yCoordinate += 2;
     
            checkBorder();
            collision = false;
        }
        bounds.setLocation(xCoordinate, yCoordinate);
    }

    //Needed in addition to outer walls since glitches through walls sometimes occur
    private void checkBorder() {
        if (xCoordinate >= 1550) {
            xCoordinate = 1550;
        }
        if (xCoordinate <= 25) {
            xCoordinate = 25;
        }
        if (yCoordinate >= 750) {
            yCoordinate = 750;
        }
        if (yCoordinate <= 25) {
            yCoordinate = 25;
        }
    }

    public void setRotation(AffineTransform rotation) {
        this.rotation = rotation;
    }

    public AffineTransform getRotation() {
        return rotation;
    }

    public void collision(Rectangle otherObjBounds) {
        this.otherObjBounds.setLocation((int)otherObjBounds.getX(), (int)otherObjBounds.getY());
        collision = true;
    }
    
    public void powerUpActive () {
         powerUp = true;
    }
    
    public ArrayList<Entity> getBullets() {
      return playerBullets;
    }
      
    public void addBullet(int playerNum) {
        Bullet b; 
        if(playerBullets.size() < 10) {
           b = new Bullet(xCoordinate, yCoordinate, angle, playerNum, powerUp);
           playerBullets.add(b);
           
           // Bullet firing sound
           Clip gameMusic = null;
           AudioInputStream audIn = null;
           File soundFile = new File("src/Tank Wars resources/Explosion_small.wav");
      
        try {
            audIn = AudioSystem.getAudioInputStream(soundFile);
            // Get a sound clip resource.
          } catch (UnsupportedAudioFileException ex) {
                System.out.println("Unsupported Audio File");
          } catch (IOException ex) {
                System.out.println("Input Output Exception when assigning audio input stream object");
            }
      
        try {
            gameMusic = AudioSystem.getClip();
        } catch (LineUnavailableException ex) {
              System.out.println("Failure Assigning Clip object");
          }
      
        try {
            gameMusic.open(audIn);
        } catch (LineUnavailableException ex) {
              System.out.println("Failure Opening Clip object");
          } catch (IOException ex) {
                System.out.println("Input Output Exception when Opening Clip Object");
            }
      
        gameMusic.start();   
       
       }
    }
}
