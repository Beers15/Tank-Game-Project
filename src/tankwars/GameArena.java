


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import java.util.ArrayList;

/**
 *
 * @author Alexander Beers
 */
public class GameArena extends JComponent {
  
    private TankWars game;
    private Tank player1Tank;
    private Tank player2Tank;
    private Dimension screenSize = new Dimension(2200, 800);
    private BufferedImage backgroundImage;
  

    public GameArena(Tank player1Tank, Tank player2Tank, TankWars game) {
        this.game = game;
        this.player1Tank = player1Tank;
        this.player2Tank = player2Tank;
        
        init();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        drawTanks(g);
        drawBullets(g);
      

        for(Entity e : game.getEnviromentList()) { 
            if(e.toString() != "Tank")
               g.drawImage(e.getImage(), e.getXCoordinate(), e.getYCoordinate(), this);     
        }
    }

    public BufferedImage getGameArena() {
        BufferedImage bi = new BufferedImage((int) screenSize.getWidth(), (int) screenSize.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();

        paintComponent(g);
        
        return bi;
    }

    public void drawBackground(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, this);
    }

    public void drawTanks(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(player1Tank.getXCoordinate(), player1Tank.getYCoordinate());
        rotation.rotate(Math.toRadians(player1Tank.getAngle()), player1Tank.getImage().getWidth() / 2, player1Tank.getImage().getHeight() / 2);
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(player1Tank.getImage(), rotation, null);

        rotation = AffineTransform.getTranslateInstance(player2Tank.getXCoordinate(), player2Tank.getYCoordinate());
        rotation.rotate(Math.toRadians(player2Tank.getAngle()), player2Tank.getImage().getWidth() / 2, player2Tank.getImage().getHeight() / 2);
        g2D = (Graphics2D) g;
        g2D.drawImage(player2Tank.getImage(), rotation, null);
    }
    
    public void drawBullets(Graphics g) {
        //draw bullets from arraylist for each player, at the correct angle
        ArrayList<Entity> temp = player1Tank.getBullets();
        for(int i = 0; i < player1Tank.getBullets().size(); i++) {
            AffineTransform rotation = AffineTransform.getTranslateInstance(temp.get(i).getXCoordinate(), temp.get(i).getYCoordinate());
            rotation.rotate(Math.toRadians(temp.get(i).getAngle()), 6, 4);
            Graphics2D g2D = (Graphics2D) g;
            g2D.drawImage(temp.get(i).getImage(), rotation, null);
        }
        temp = player2Tank.getBullets();
        for(int i = 0; i < player2Tank.getBullets().size(); i++) {
            AffineTransform rotation = AffineTransform.getTranslateInstance(temp.get(i).getXCoordinate(), temp.get(i).getYCoordinate());
            rotation.rotate(Math.toRadians(temp.get(i).getAngle()), 6, 4);
            Graphics2D g2D = (Graphics2D) g;
            g2D.drawImage(temp.get(i).getImage(), rotation, null);
        }
    }
    
    public void init() {
        try {
            backgroundImage = ImageIO.read(new File("src/Tank Wars resources/preTiledBackground.png"));
        } catch (IOException e) {
          System.out.println("Background Image not found...");
        }
        
        //load level from text file
        String line;
        File arenaFile = null;
        BufferedReader buf = null;
        int row = 0;

        try {
            arenaFile = new File("src/tankwars/arenaLoad.txt");
            buf = new BufferedReader(new FileReader(arenaFile));
         
            line = buf.readLine();
        
            while(line != null) {
                for(int column = 0; column < line.length(); column++) { 
                    char objNum = line.charAt(column); 
            
                    switch(objNum) {
                        //normal walls
                        case '1':
                            Wall w = new Wall((column * 25), (row * 25), false);
                            w.initBounds();
                            game.addEnviromentObj(w);
                            break;
                        //fragile walls
                        case '2':
                            Wall fw = new Wall((column * 25), (row * 25), true);
                            fw.initBounds();
                            game.addEnviromentObj(fw);
                            break;
                        //power ups
                        case '3':
                            PowerUp p = new PowerUp((column * 25), (row * 25));
                            p.initBounds();
                            game.addEnviromentObj(p); 
                            break;    

                       default:
                           break;
                    }
                }
                row++;           
                line = buf.readLine();
            }             
        } catch (IOException e) {
            e.printStackTrace();
          } 
    
        game.addEnviromentObj(player1Tank);
        game.addEnviromentObj(player2Tank);           
    }
}
