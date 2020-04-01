import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Each WindowManager Instance serves as a template of window positions
 * intended to make easier managing window locations
 */
public class WindowManager {

    private ArrayList<Window> windows; //list works as queue
    private ArrayList<Point> locations; //aspirational positions of the centers of all the windows of the instance
    private HashMap<Point, Window> locationMap;
    private int numClosed;

    /**
     * <p>
     * Case canvii:
     * - Each instance is fed an arraylist of equal number canvii
     * <p>
     * Case everything else:
     * - only regard the biggest of the two windows
     */
    public WindowManager(ArrayList<Window> windows) {
        this.windows = windows;
        this.locationMap = new HashMap<>();
        this.numClosed = 0;
    }

    /**
     * finds all possible locations of windows, stores locations in a list
     */
    public void initialize() {
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
        this.locations = locations;
        System.out.println(locations);

        //matches available locations with available windows
        int i = 0;
        while (i < locations.size() && i < windows.size()) {
            if (!windows.get(i).isVisible()) {
                locationMap.put(locations.get(i), windows.get(i));
                windows.get(i).centerAt(locations.get(i));
            }
            i++;
        }
    }

    //Finds row and column locations
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

    public void displayAllPositioned() {
        for (Window window : windows) {
            window.setVisible(false);
        }
        for (Point loc : locationMap.keySet()) {
            locationMap.get(loc).setVisible(true);
        }
    }

    public void incrementClosed() {
        this.numClosed++;
    }

    public void update() {
        HashMap<Point, Window> newLocations = new HashMap<>();
        int displacement = 0;
        for (int i = 0; i < locations.size(); i++) {
            Window window = locationMap.get(locations.get(i));
            window.centerAt(locations.get(i - displacement));
            newLocations.put(locations.get(i - displacement), window);
            if (!window.isVisible()) {
                displacement++;
            }
            if (i < locations.size() - 1 && locationMap.get(locations.get(i + 1)) == null)
                break; //prevents loop for traversing locations without windows
        }

        if (windows.size() - numClosed > locations.size()) {
            Point loc = locations.get(locations.size() - 1);
            newLocations.put(loc, windows.get(locations.size() + numClosed));
            newLocations.get(loc).centerAt(loc);
            newLocations.get(loc).setVisible(true);
        }

        locationMap = newLocations;
    }
}
