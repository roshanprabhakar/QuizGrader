import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Constants {

    public static final String separator = File.separator;
    public static final String imagePath = "src" + separator + "ScannedImageSources" + separator;
    public static final String studentResponses = imagePath + "StudentResponses" + separator;
    public static final String blankTest = imagePath + "BlankTestPages" + separator;
    public static final String outCSV = "CSVout";
    public static final String pdfIn = "PDFInput" + separator;
    public static final String res = "src" + File.separator + "RES" + File.separator;

    public static final double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final double screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    public static final int scaleWidth = (int) (screenHeight * ((double)50/75)); //scale all images to this width
    public static final int scaleHeight = (int) screenHeight; //scale all images to this height

    public static final int labelWidth = 150;
    public static final int labelHeight = 120;

    public static char[] grades = new char[]{'F', 'F', 'F', 'F', 'F', 'F', 'D', 'C', 'B', 'A'};
    public static String[] simpGrades = new String[]{"F", "D", "C", "B", "A"};

    public static String[] toArray(ArrayList<String> arr) {
        String[] list = new String[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            list[i] = arr.get(i);
        }
        return list;
    }

    public static void removeDuplicates(String[] arr) {
        ArrayList<String> uniqueTags = new ArrayList<>();
        for (String item : arr) {
            if (!uniqueTags.contains(item)) {
                uniqueTags.add(item);
            }
        }
        arr = toArray(uniqueTags);
    }

    public static Character findGrade(Score score) {
        for (double i = 0; i < 100; i += 10) {
            if (score.getPercent() >= i && score.getPercent() < i + 10) {
                return grades[(int) (i / 10)];
            }
        }
        return 'A';
    }

    public static int mode(int[] arr) {
        int maxIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > arr[maxIndex]) maxIndex = i;
        }
        return maxIndex;
    }

    public static int max(ArrayList<Integer> arr) {
        int max = arr.get(0);
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i) > max) max = arr.get(i);
        }
        return max;
    }

    public static String generateSpaces(int num) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < num; i++) {
            out.append(" ");
        }
        return out.toString();
    }

    public static Point getLocationOfMouse() {
        int mouseX = MouseInfo.getPointerInfo().getLocation().x;
        int mouseY = MouseInfo.getPointerInfo().getLocation().y;
        return new Point(mouseX, mouseY);
    }

    public static int indexOfSimpGrades(Character chr) {
        for (int i = 0; i < simpGrades.length; i++) {
            if (simpGrades[i].equals(chr.toString())) return i;
        }
        return -1;
    }

    public static void delete(String filename) {
        File file = new File(filename);
        file.delete();
    }

    public static ArrayList<String> getLines(File inputFile) {
        ArrayList<String> out = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));

            String line;
            while ((line = reader.readLine()) != null) {
                out.add(line);
            }

        } catch (IOException e) {
            System.err.println("Could not read from input file: " + inputFile.getName());
        }
        return out;
    }

    public static void record(Object obj) {
    }
}
