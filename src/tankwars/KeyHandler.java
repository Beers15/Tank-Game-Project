


import java.awt.event.*; 

/**
 *
 * @author Alexander Beers
 */
public class KeyHandler implements KeyListener {
  
    private Tank playerTank;

    public KeyHandler(Tank playerTank) {
        this.playerTank = playerTank;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (playerTank.getPlayerNum() == 1) {
            if (e.getKeyCode() == KeyEvent.VK_A) {
                playerTank.leftPress();
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
                playerTank.rightPress();
            }
            if (e.getKeyCode() == KeyEvent.VK_W) {
                playerTank.upPress();
            }
            if (e.getKeyCode() == KeyEvent.VK_S) {
                playerTank.downPress();
            }
        }
        if (playerTank.getPlayerNum() == 2) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                playerTank.leftPress();
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                playerTank.rightPress();
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                playerTank.upPress();
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                playerTank.downPress();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (playerTank.getPlayerNum() == 1) {
            if (e.getKeyCode() == KeyEvent.VK_A) {
                playerTank.leftRelease();
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
                playerTank.rightRelease();
            }
            if (e.getKeyCode() == KeyEvent.VK_W) {
                playerTank.upRelease();
            }
            if (e.getKeyCode() == KeyEvent.VK_S) {
                playerTank.downRelease();
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
               playerTank.addBullet(1); 
            }
        }
        if (playerTank.getPlayerNum() == 2) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                playerTank.leftRelease();
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                playerTank.rightRelease();
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                playerTank.upRelease();
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                playerTank.downRelease();
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                playerTank.addBullet(2); 
            }
        }
    }
}
