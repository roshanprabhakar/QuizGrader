import java.io.File;
import java.util.ArrayList;

public class Constants {

    public static final String separator = File.separator;
    public static final String imagePath = "src" + separator + "ScannedImageSources" + separator;
    public static final String StudentResponsePath = imagePath + "StudentResponses";
    public static final String blankTestPath = imagePath + "BlankTest" + separator;
    public static final String StudentDirectoryPath = StudentResponsePath + separator + "Student";

    public static char[] grades = new char[] {'F','F','F','F','F','F','D','C','B','A'};
    public static String[] simpGrades = new String[] {"F","D","C","B","A"};

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
        if (score.getPercent() > 90) return 'A';
        int count = 0;
        for (double i = 0; i <= 100; i += 10) {
            if (score.getPercent() <= i) {
                return Constants.grades[count];
            }
            count++;
        }
        return 'F';
    }
}
