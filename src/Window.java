import javax.swing.*;
import java.awt.*;

// for the sole purpose of managing positions
public class Window {

    protected JFrame frame;

    public Window() {}

    //for testing purposes
    public Window(int width, int height) {
        frame = new JFrame();
        frame.setSize(new Dimension(width, height));
    }

    public void setLocation(Point location) {
        frame.setLocation(location);
    }

    public void setLocation(int x, int y) {
        setLocation(new Point(x, y));
    }

    public int getHeight() {
        return frame.getHeight();
    }

    public int getWidth() {
        return frame.getWidth();
    }

    public void centerAt(Point p) {
        setLocation((int) (p.getX() - frame.getWidth() / 2), (int) (p.getY() - frame.getHeight() / 2));
    }

    public Point getLocation() {
        return frame.getLocation();
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public JFrame getFrame() {
        return this.frame;
    }

    public boolean isVisible() {
        return frame.isVisible();
    }
}
