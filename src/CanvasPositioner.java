import java.awt.*;
import java.util.ArrayList;

public class CanvasPositioner {

    private ArrayList<Canvas> canvii;
    private ArrayList<CanvasPosition> centers;

    private static final int numCanvi = 8;
    private static final int numRows = 2;

    private int rows, cols;

    public CanvasPositioner() {
        canvii = new ArrayList<>();
        centers = new ArrayList<>();

        int xinterval = (int) Constants.screenWidth / (numCanvi / numRows + 1);
        int yinterval = (int) Constants.screenHeight / (numRows + 1);

        rows = (int) Constants.screenHeight/xinterval;
        cols = (int) Constants.screenWidth/yinterval;

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
        boolean[] submitTracker = new boolean[canvii.size()];



        for (int i = 0; i < submitTracker.length; i++) {
            submitTracker[i] = false;
        }

        for (int i = 0; i < numCanvi; i++) {
            CanvasPosition center = centers.get(i);
            double xPos = center.getLocation().getX();
            double yPos = center.getLocation().getY()
            canvii.get(i).setLocation((int) xPos,(int) yPos);
        }
    }
}
