


import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 *
 * @author Alexander Beers
 */
public class Minimap extends JLabel {
  
    private BufferedImage mapImage;

    public Minimap(BufferedImage buf) {
        mapImage = buf;
    
        setSize(mapImage.getWidth(), mapImage.getHeight());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setOpaque(true);
        setBackground(Color.black);
      
        g.drawImage(mapImage, 0, 0, 1000, 600, 0, 0, 1600, 800, this);
    }

    public void renderMap(BufferedImage buf) {
        double scale = .3;
        //BufferedImage image = new BufferedImage(1600, 800, BufferedImage.TYPE_INT_RGB);

        int width = buf.getWidth();
        int height = buf.getHeight();
        int scaledWidth = (int) (width * scale);
        int scaledHeight = (int) (height * scale);

        BufferedImage minimapImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
        AffineTransform scaleInstance = AffineTransform.getScaleInstance(scale, scale);
        AffineTransformOp scaleOp = new AffineTransformOp(scaleInstance, AffineTransformOp.TYPE_BILINEAR);
        scaleOp.filter(buf, minimapImage);

        mapImage = minimapImage;
    }
}
