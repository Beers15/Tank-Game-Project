


/**
 *
 * @author Alexander Beers
 */
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.sound.sampled.*;
import java.util.*;

public class TankWars extends JFrame implements Runnable {

    private Tank player1Tank;
    private Tank player2Tank;
    private Screen player1;
    private Screen player2;
    private Screen minimapReferenceJPanel;
    private Entity tankObservables;
    private Image titleIcon = new ImageIcon("src/Tank Wars resources/tank.png").getImage();
    private Minimap minimap;
    private Dimension screenSize = new Dimension(1100, 800);
    private Dimension arenaSize = new Dimension(1600, 800);
    private GameArena arena;
    private BufferedImage arenaImage;
    //for storing enviroment objects
    private ArrayList<Entity> objects = new ArrayList<Entity>();

    public TankWars() {
        //Initalize Tanks, their corresponding Screens
        player1Tank = new Tank(1, 50, 50, 5, 5, (short) 90);
        player2Tank = new Tank(2, arenaSize.width - 75, arenaSize.height - 75, 5, 5, (short) - 90);

        //initialize bufferedImage creating object
        arena = new GameArena(player1Tank, player2Tank, this);

        JLayeredPane layeredPane = new JLayeredPane();
        add(layeredPane);

        //initialize player screens, for splitscreen
        player1 = new Screen(1, player1Tank, player2Tank, arena.getGameArena());
        player2 = new Screen(2, player2Tank, player1Tank, arena.getGameArena());
        minimapReferenceJPanel = new Screen(3, player1Tank, player2Tank, arena.getGameArena());
        JPanel panels = new JPanel();
        panels.setLayout(new GridLayout(1, 2));
        panels.add(player1.getScreen());
        panels.add(player2.getScreen());
        panels.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
        layeredPane.add(panels, new Integer(1));

        //add and initialize minimap
        BufferedImage buf = minimapReferenceJPanel.getMapimage();
        minimap = new Minimap(minimapReferenceJPanel.getMapimage());
        minimap.setSize(300, 180);
        minimap.setLocation((((int) screenSize.getWidth() / 2) - (150)), (((int) screenSize.getHeight()) - (215)));
        layeredPane.add(minimap, new Integer(3)); //Remember to re-enable the minimap

        //initalize JFrame settings
        requestFocus();
        setVisible(true);
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("TANK WARS!");
        setIconImage(titleIcon);

        addKeyListener(getPlayerScreen(1).getKeyHandler());
        addKeyListener(getPlayerScreen(2).getKeyHandler());
        this.tankObservables = new Entity();
    }

    public static void main(String[] args) {
        TankWars game = new TankWars();
        game.setVisible(true);
        game.init();

        Clip gameMusic = null;
        AudioInputStream audIn = null;
        File soundFile = new File("src/Tank Wars resources/gameMusic.wav");

        // Get a sound clip resource.
        try {
            audIn = AudioSystem.getAudioInputStream(soundFile);
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
        /*
        try {
            gameMusic.open(audIn);
        } catch (LineUnavailableException ex) {
            System.out.println("Failure Opening Clip object");
        } catch (IOException ex) {
            System.out.println("Input Output Exception when Opening Clip Object");
        }

        gameMusic.start();
        */
        game.runGame();
    }

    private void runGame() {
        Thread gameThread = new Thread();
        gameLoop();
        gameThread.start();
    }

    private void update() {
        //get main buffered image, the render all 3 screens and check collisions
        arenaImage = arena.getGameArena();
        player1.render(arenaImage);
        player2.render(arenaImage);
        minimap.renderMap(arenaImage);
        check4Collisions();
    }

    //game loop based off of one from this tutorial: ------------------------------------------
    //https://m.youtube.com/watch?v=1gir2R7G9ws&list=PLWms45O3n--6TvZmtFHaCWRZwEqnz2MHa&index=1
    private void gameLoop() {
        long lastUpdate = System.nanoTime();
        double fps = 45.0;
        double nanoseconds = 1000000000 / fps;
        double delta = 0;
        long timer = System.currentTimeMillis();

        int frameCount = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastUpdate) / nanoseconds;
            lastUpdate = now;
            try {
                tankObservables.setChanged();
                tankObservables.notifyObservers();
                Thread.sleep(10);//Thread.sleep(1000 / 144);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }

            while (delta >= 0) {
                update();

                delta--;
            }
            frameCount++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frameCount = 0;
            }
        }
    }
    //---------------------------------------------------------------------------------

    public Screen getPlayerScreen(int playerNum) {
        if (playerNum == 1) {
            return player1;
        } else {
            return player2;
        }
    }

    //put to avoid error from IDE even though run redefined elsewhere
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void init() {

        ImageIcon dialogIcon = new ImageIcon("src/Tank Wars resources/dialogIcon.png");

        Image image = dialogIcon.getImage(); // transform it

        Image newimg = image.getScaledInstance(280, 150, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 

        dialogIcon = new ImageIcon(newimg);
        Object[] options = {"Start Game"};
        JOptionPane.showOptionDialog(this, "GET READY!", "WELCOME TO TANK BATTLE!",
                JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, dialogIcon,
                options, options[0]);
        tankObservables.addObserver(player1Tank);
        tankObservables.addObserver(player2Tank);
        player1Tank.initBounds();
        player2Tank.initBounds();
    }

    public void addEnviromentObj(Entity obj) {
        objects.add(obj);
    }

    public ArrayList<Entity> getEnviromentList() {
        return objects;
    }

    public void check4Collisions() {
        ArrayList<Entity> temp1 = player1Tank.getBullets();
        ArrayList<Entity> temp2 = player2Tank.getBullets();
        //collsion checks

        //tank on tank collision check
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).toString() == "Tank") {
                Tank t = (Tank) objects.get(i);
                if (objects.get(i).collisionChk(player1Tank)) {
                    if (t.getPlayerNum() == 2) {
                        player1Tank.collision(objects.get(i).getBounds());
                    }
                }

                if (objects.get(i).collisionChk(player2Tank)) {
                    if (t.getPlayerNum() == 1) {
                        player2Tank.collision(objects.get(i).getBounds());
                    }
                }
            }

            //check to see if tank collides with any object other than a tank 
            if (objects.get(i).collisionChk(player1Tank) && objects.get(i).toString() != "Tank") {
                player1Tank.collision(objects.get(i).getBounds());
                if (objects.get(i).toString() == "powerUp") {
                    objects.remove(i);
                    player1Tank.powerUpActive();
                }
            }

            if (objects.get(i).collisionChk(player2Tank) && objects.get(i).toString() != "Tank") {
                player2Tank.collision(objects.get(i).getBounds());
                if (objects.get(i).toString() == "powerUp") {
                    objects.remove(i);
                    player2Tank.powerUpActive();
                }
            }

            //check for bullet collisions
            //player 1 bullets
            for (int j = 0; j < temp1.size(); j++) {
                Tank t;
                if (objects.get(i).collisionChk(temp1.get(j)) && objects.get(i).toString() != "Tank") {

                    temp1.remove(j);
                    if (objects.get(i).toString() == "FragileWall") {

                        objects.remove(i);

                        // Initialize sound file for explosion
                        Clip wallExplosion = null;
                        AudioInputStream audIn = null;
                        File soundFile = new File("src/Tank Wars resources/Explosion_large.wav");
                        // Get a sound clip resource.
                        try {
                            audIn = AudioSystem.getAudioInputStream(soundFile);
                        } catch (UnsupportedAudioFileException ex) {
                            System.out.println("Unsupported Audio File");
                        } catch (IOException ex) {
                            System.out.println("Input Output Exception when assigning audio input stream object");
                        }

                        try {
                            wallExplosion = AudioSystem.getClip();
                        } catch (LineUnavailableException ex) {
                            System.out.println("Failure Assigning Clip object");
                        }

                        try {
                            wallExplosion.open(audIn);
                        } catch (LineUnavailableException ex) {
                            System.out.println("Failure Opening Clip object");
                        } catch (IOException ex) {
                            System.out.println("Input Output Exception when Opening Clip Object");
                        }
                        wallExplosion.start();
                    }

                } else if (objects.get(i).toString() == "Tank" && objects.get(i).collisionChk(temp1.get(j))) {
                    t = (Tank) objects.get(i);
                    if (t.getPlayerNum() == 2) {
                        temp1.remove(j);
                        player2Tank.takeDamage();
                         // Initialize sound file for explosion
                        Clip fireSound = null;
                        AudioInputStream audIn = null;
                        File soundFile = new File("src/Tank Wars resources/Explosion_small.wav");
                        // Get a sound clip resource.
                        try {
                            audIn = AudioSystem.getAudioInputStream(soundFile);
                        } catch (UnsupportedAudioFileException ex) {
                            System.out.println("Unsupported Audio File");
                        } catch (IOException ex) {
                            System.out.println("Input Output Exception when assigning audio input stream object");
                        }

                        try {
                            fireSound = AudioSystem.getClip();
                        } catch (LineUnavailableException ex) {
                            System.out.println("Failure Assigning Clip object");
                        }

                        try {
                            fireSound.open(audIn);
                        } catch (LineUnavailableException ex) {
                            System.out.println("Failure Opening Clip object");
                        } catch (IOException ex) {
                            System.out.println("Input Output Exception when Opening Clip Object");
                        }
                        fireSound.start();
                    
                    }
                }
            }
            //player 2 bullets      
            for (int j = 0; j < temp2.size(); j++) {
                Tank t;
                if (objects.get(i).collisionChk(temp2.get(j)) && objects.get(i).toString() != "Tank") {

                    temp2.remove(j);
                    if (objects.get(i).toString() == "FragileWall") {
                        objects.remove(i);

                        // Initialize sound file for explosion
                        Clip wallExplosion = null;
                        AudioInputStream audIn = null;
                        File soundFile = new File("src/Tank Wars resources/Explosion_large.wav");

                        try {
                            audIn = AudioSystem.getAudioInputStream(soundFile);
                            // Get a sound clip resource.
                        } catch (UnsupportedAudioFileException ex) {
                            System.out.println("Unsupported Audio File");
                        } catch (IOException ex) {
                            System.out.println("Input Output Exception when assigning audio input stream object");
                        }

                        try {
                            wallExplosion = AudioSystem.getClip();
                        } catch (LineUnavailableException ex) {
                            System.out.println("Failure Assigning Clip object");
                        }

                        try {
                            wallExplosion.open(audIn);
                        } catch (LineUnavailableException ex) {
                            System.out.println("Failure Opening Clip object");
                        } catch (IOException ex) {
                            System.out.println("Input Output Exception when Opening Clip Object");
                        }

                        wallExplosion.start();
                    }
                } else if (objects.get(i).toString() == "Tank" && objects.get(i).collisionChk(temp2.get(j))) {
                    t = (Tank) objects.get(i);
                    if (t.getPlayerNum() == 1) {
                        temp2.remove(j);
                        player1Tank.takeDamage();
                        
                       // Initialize sound file for explosion
                        Clip fireSound = null;
                        AudioInputStream audIn = null;
                        File soundFile = new File("src/Tank Wars resources/Explosion_small.wav");
                        // Get a sound clip resource.
                        try {
                            audIn = AudioSystem.getAudioInputStream(soundFile);
                        } catch (UnsupportedAudioFileException ex) {
                            System.out.println("Unsupported Audio File");
                        } catch (IOException ex) {
                            System.out.println("Input Output Exception when assigning audio input stream object");
                        }

                        try {
                            fireSound = AudioSystem.getClip();
                        } catch (LineUnavailableException ex) {
                            System.out.println("Failure Assigning Clip object");
                        }

                        try {
                            fireSound.open(audIn);
                        } catch (LineUnavailableException ex) {
                            System.out.println("Failure Opening Clip object");
                        } catch (IOException ex) {
                            System.out.println("Input Output Exception when Opening Clip Object");
                        }
                        fireSound.start();
                      
                    }
                }
            }
        }

        //update bullet locations
        for (int i = 0; i < temp1.size(); i++) {
            Bullet b = (Bullet) temp1.get(i);
            b.updateBullet();
        }

        for (int i = 0; i < temp2.size(); i++) {
            Bullet b = (Bullet) temp2.get(i);
            b.updateBullet();
        }
    }
}
