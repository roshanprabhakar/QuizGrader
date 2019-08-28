import java.awt.*;
import java.util.ArrayList;

public class CanvasPositioner {

    private ArrayList<Canvas> canvii;
    private ArrayList<CanvasPosition> centers;

    private static final int numCanvi = 8;
    private static final int numRows = 2;

    public CanvasPositioner() {
        canvii = new ArrayList<>();
        centers = new ArrayList<>();

        int xinterval = (int) Constants.screenWidth / (numCanvi / numRows + 1);
        int yinterval = (int) Constants.screenHeight / (numRows + 1);

        for (int r = xinterval; r < Constants.screenHeight; r += xinterval) {
            for (int c = yinterval; c < Constants.screenWidth; c += yinterval) {
                centers.add(new CanvasPosition(new Point(r, c)));
            }
        }
    }

    public void add(Canvas canvas) {
        canvii.add(canvas);
    }

    public void initiate() {

    }
}
