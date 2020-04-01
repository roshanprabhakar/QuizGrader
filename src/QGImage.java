import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QGImage extends Window implements MouseListener {

    private boolean isPressed;

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

    public QGImage(BufferedImage image) {
        this.frame = new JFrame();
        this.image = image;
        frame.addMouseListener(this);
        width = image.getWidth();
        height = image.getHeight();
    }

    public void drawRectangleAt(int startX, int startY, int endX, int endY) {
        rectangle = new JFrame();
        rectangle.setLocation(new Point(startX + frame.getX(), startY + frame.getY()));
        rectangle.setSize(endX - startX, endY - startY);
        rectangle.getContentPane().setBackground(Color.GRAY);
        rectangle.getRootPane().putClientProperty("Window.alpha", new Float(0.3f));
        rectangle.setUndecorated(true);
        rectangle.setVisible(true);
    }

    public QGImage getRegion(AnswerField a) {
        return new QGImage(image.getSubimage(a.getTopX(), a.getTopY(), a.getWidth(), a.getHeight()));
    }

    public QGImage getRegion2(AnswerField a) {
        QGImage newImage = new QGImage(image.getSubimage(a.getTopX(), a.getTopY(), a.getWidth(), a.getHeight()));
//        newImage.setUndecorated(true);
        return newImage;
    }

    public void removeBorder() {
        frame.setUndecorated(true);
    }

    public ImageIcon getIcon() {
        return new ImageIcon(image);
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public void resize(int newH, int newW) {
        Image tmp = image.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        image = dimg;
    }

    public QGImage resize(int newH, int newW, boolean forPaneInout) {
        resize(newH, newW);
        return this;
    }


    public void display(boolean undecorated) {
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.setUndecorated(undecorated);
        frame.pack();
        frame.setVisible(true);
    }

    public void setUndecorated(boolean undecorated) {
        frame.setUndecorated(undecorated);
    }

    public void close() {
        frame.setVisible(false);
    }

    public void closeRectangleView() {
        rectangle.setVisible(false);
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


    public static class QGPanel extends JPanel {
        private static BufferedImage image;

        public QGPanel(BufferedImage image) {
           this.image = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
            }
        }
    }
}


