import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QGImage implements MouseListener {

    private boolean isPressed;

    private JFrame frame; //for the mouse listener
    private JFrame rectangle; //rectangle view finder
    private BufferedImage image; //contents of the image

    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public QGImage(String name) {
        try {
            this.frame = new JFrame();
            this.image = ImageIO.read(new File(name));
            frame.addMouseListener(this);
            width = image.getWidth();
            height = image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param image
     */
    public QGImage(BufferedImage image) {
        this.frame = new JFrame();
        this.image = image;
        frame.addMouseListener(this);
        width = image.getWidth();
        height = image.getHeight();
    }

    /**
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    public void drawRectangleAt(int startX, int startY, int endX, int endY) {
        rectangle = new JFrame();
        rectangle.setLocation(new Point(startX, startY));
        rectangle.setSize(endX - startX, endY - startY);
        rectangle.getContentPane().setBackground(Color.GRAY);
        rectangle.getRootPane().putClientProperty("Window.alpha", new Float(0.3f));
        rectangle.setUndecorated(true);
        rectangle.setVisible(true);
    }

    /**
     * @param a
     * @return
     */
    public QGImage getRegion(AnswerField a) {

        BufferedImage section = new BufferedImage(a.getWidth(), a.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int r = a.getTopY(); r < a.getBottomY(); r++) {
            for (int c = a.getTopX(); c < a.getBottomX(); c++) {
                section.setRGB(c - a.getTopX(), r - a.getTopY(), image.getRGB(c, r));
            }
        }

        return new QGImage(section);
    }

    public ImageIcon getIcon() {
        return new ImageIcon(image);
    }

    /**
     * @param newH
     * @param newW
     */
    public void resize(int newH, int newW) {
        Image tmp = image.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        image = dimg;
    }

    public void display() {
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);
    }

    public void close() {
        frame.setVisible(false);
    }

    public void closeRectangleView() {
        rectangle.setVisible(false);
    }

    public void setPosition(Point p) {
        frame.setLocation(p);
    }

    // --- Mouse Listener Interface Methods ---
    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        isPressed = true;
    }

    public void mouseReleased(MouseEvent e) {
        isPressed = false;
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public boolean mouseIsPressed() {
        return isPressed;
    }


}


