import java.awt.*;

public class CanvasPosition {

    private Point location;
    private boolean isOcuppied = false;

    public CanvasPosition(Point location) {
        this.location = location;
    }

    public void occupy() {
        this.isOcuppied = true;
    }

    public Point getLocation() {
        return location;
    }

    public boolean isOcuppied() {
        return isOcuppied;
    }
}
