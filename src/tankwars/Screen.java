


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;

public class Screen {
  
    private Tank playerTank;
    private Tank enemyTank;
    private PlayerHud playerHud;
    private int playerNum;
    private final Dimension screenSize = new Dimension(1200, 800);
    private BufferedImage arena; 

    private JPanel screen = new JPanel(true) {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            //configures screen so that it stops banning near the border
            int xScreenCoord = (int)(playerTank.getXCoordinate() * 1.45);
            int yScreenCoord = playerTank.getYCoordinate() * 2;
            if(playerTank.getXCoordinate() >= 1395) {
                xScreenCoord = (int)(1395 * 1.45);
            }                  
            if(playerTank.getXCoordinate() <= 165) {
                xScreenCoord = (int)(165 * 1.45);
            }
            if (playerTank.getYCoordinate() >= 620) {
                yScreenCoord = 620 * 2;
            }
            if(playerTank.getYCoordinate() <= 200) {
               yScreenCoord = 200 * 2;
            }

            if(playerNum != 3)
                g.drawImage(arena, (int)(screenSize.getWidth() / 5 - (xScreenCoord)), (int)screenSize.getHeight()/2-(yScreenCoord), 3200, 1600, this);       
            else
                g.drawImage(arena, 0, 0, this);              
        }
    };

    //By passing both tanks we are accepting rendering both tanks on all screens - could probably be more efficient 
    public Screen(int playerNum, Tank tank1, Tank tank2, BufferedImage arena) {
        screen.setLayout(null);
        Border outline = new LineBorder(Color.BLACK, 4);
        screen.setBorder(outline);
        screen.setBackground(Color.BLACK);  
        screen.setVisible(true);
        int panelWidth = (int) (.75 * screenSize.width) / 2;
        int panelHeight = (int) (.75 * screenSize.height) - 10;
        screen.setSize(panelWidth, panelHeight);

        //Player Setup
        this.playerNum = playerNum;//for HUD
        playerTank = tank1;
        enemyTank = tank2;
        if (playerNum != 3) {
            playerHud = new PlayerHud(playerTank);
            screen.add(playerHud);
            if(playerNum == 1)
                playerHud.setLocation(3, screenSize.height - 145);
            else
                playerHud.setLocation(140, screenSize.height - 145);
        }
    }
    
    public void render(BufferedImage buf) {
        this.arena = buf;
        screen.repaint();
    }

    public Tank getPlayerTank() {
        return playerTank;
    }

    public PlayerHud getPlayerHud() {
        return playerHud;
    }

    public KeyHandler getKeyHandler() {
        KeyHandler keyHandler = new KeyHandler(playerTank);

        return keyHandler;
    }

    public JPanel getScreen() {
        return screen;
    }

    public int getWidth() {
        return screen.getWidth();
    }

    public int getHeight() {
        return screen.getHeight();
    }

    public BufferedImage getMapimage() {
        double scale = .3;
        BufferedImage image = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        try {
            screen.paint(g);
        } catch (Exception e) {
        }

        int width = image.getWidth();
        int height = image.getHeight();
        int scaledWidth = (int) (width * scale);
        int scaledHeight = (int) (height * scale);

        BufferedImage minimapImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
        AffineTransform scaleInstance = AffineTransform.getScaleInstance(scale, scale);
        AffineTransformOp scaleOp = new AffineTransformOp(scaleInstance, AffineTransformOp.TYPE_BILINEAR);
        scaleOp.filter(image, minimapImage);

        return minimapImage;
    }
}