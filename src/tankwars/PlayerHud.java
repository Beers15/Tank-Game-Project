


import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Alexander Beers
 */
//Both PlayerHud classes get unneccesary copy of other player's tank
public class PlayerHud extends JPanel {

    private int playerNum;
    private final Tank playerTank;
    private boolean damagePlayer = false;
    //images needed for HUD
    //hudBackgound image url https://www.dreamstime.com/photos-images/steel-plate.html
    private final Image hudBackground = new ImageIcon("src/Tank Wars resources/hudBackground.jpg").getImage();
    private final Image lifeImage = new ImageIcon("src/Tank Wars resources/TankLivesImage.png").getImage();
    private final Image player1Image = new ImageIcon("src/Tank Wars resources/player1.png").getImage();
    private final Image player2Image = new ImageIcon("src/Tank Wars resources/player2.png").getImage();
    private final Image oneImage = new ImageIcon("src/Tank Wars resources/one.png").getImage();
    private final Image twoImage = new ImageIcon("src/Tank Wars resources/two.png").getImage();
    private final Image threeImage = new ImageIcon("src/Tank Wars resources/three.png").getImage();

    public PlayerHud(Tank playerTank) {
        this.playerTank = playerTank;
        setSize(400, 150);
        setVisible(true);
        playerNum = playerTank.getPlayerNum();
        
        setOpaque(false);     
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawHud(g);
    }

    public void drawHud(Graphics g) {
        if (damagePlayer == true) {
            playerTank.takeDamage();
            damagePlayer = false;
        }

        int playerLifeTier = playerTank.getLifeTeir();
        int playerLives = playerTank.getLives();
        int lifeDisplay = playerLifeTier;

        if(playerLifeTier == 0) {
            if(playerLives > 0) {
                playerLives--;
                playerTank.setLives(playerLives);
                playerTank.setLifeTier(12);
                playerLifeTier = 12;
            }
        }
        if(playerLifeTier >= 1) {
            lifeDisplay *= 33;
        } 
        else if(playerLives == 0) {
            System.exit(0);
        }

        g.setColor(new Color(104, 138, 45, 120));
        g.fill3DRect(17, 10, 375, 88, true);
        if(playerNum == 1) {
            g.drawImage(player1Image, 40, 6, this);
        }
        //display appropriate player label
        if(playerNum == 2) {
            g.drawImage(player2Image, 40, 6, this);
        }
        
        //show appropriate number of lives
        g.drawImage(lifeImage, 245, 25, this);
        switch (playerLives) {
            case 3:
                g.drawImage(threeImage, 350, 40, this);
                break;
            case 2:
                g.drawImage(twoImage, 350, 40, this);
                break;
            case 1:
                g.drawImage(oneImage, 350, 40, this);
                break;
            default:
                System.out.println("Player " + playerNum + " has no more lives!!!!");
                System.exit(0);
        }

        g.setColor(Color.RED);
        g.fill3DRect(25, 51, lifeDisplay / 2, 40, true);
    }
    
    public void render() {
        repaint();
    }

    public void setDamage(boolean value) {
        damagePlayer = true;
    }
}
