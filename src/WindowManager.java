import jdk.internal.net.http.ResponseTimerEvent;

import java.awt.*;
import java.util.ArrayList;

/**
 * Each WindowManager Instance serves as a template of window positions
 * intended to make easier managing window locations
 */
public class WindowManager {

    private int numWindows;

    private ArrayList<Window> windows;
    private ArrayList<Point> centerPositions; //aspirational positions of the centers of all the windows of the instance

    /**
     * @param windows
     * @param numWindows, the configuration of this instance
     *                  <p>
     *                  Case canvii:
     *                  - Each instance is fed an arraylist of equal number canvii
     *                  <p>
     *                  Case everything else:
     *                  - only regard the biggest of the two windows
     */
    public WindowManager(ArrayList<Window> windows, int numWindows) {
        this.windows = windows;
        this.numWindows = numWindows;
    }

    /**
     * finds all possible locations of windows
     */
    public void configure() {
        Dimension component = largestWindow();

        int numRows = (int) ((Constants.screenHeight - (Constants.screenHeight % component.getHeight())) / component.getHeight());
        ArrayList<Integer> rowIndices = new ArrayList<>();
        int rowHeight = (int) (Constants.screenHeight / numRows);
        extracted(numRows, rowHeight, rowIndices);

        int numCols = (int) ((Constants.screenWidth - (Constants.screenWidth % component.getWidth())) / component.getWidth());
        ArrayList<Integer> colIndices = new ArrayList<>();
        int colWidth = (int) (Constants.screenWidth / numCols);
        extracted(numCols, colWidth, colIndices);

        ArrayList<Point> locations = new ArrayList<>();
        for (int r = 0; r < rowIndices.size(); r++) {
            for (int c = 0; c < colIndices.size(); c++) {
                locations.add(new Point(colIndices.get(c), rowIndices.get(r)));
            }
        }
    }

    //helper for configure
    private void extracted(int numDivisions, int spaceWidth, ArrayList<Integer> indexList) {
        int index = 0;
        for (int i = 0; i < numDivisions; i++) {
            int increment = spaceWidth;
            if (i == 0) increment /= 2;
            index += increment;
            indexList.add(index);
        }
    }

    private Dimension largestWindow() {
        return new Dimension(largestWidth(), largestHeight());
    }

    private int largestHeight() {
        int largest = 0;
        for (Window window : windows) {
            if (window.getHeight() > largest) largest = window.getHeight();
        }
        return largest;
    }

    private int largestWidth() {
        int largest = 0;
        for (Window window : windows) {
            if (window.getWidth() > largest) largest = window.getWidth();
        }
        return largest;
    }

}
