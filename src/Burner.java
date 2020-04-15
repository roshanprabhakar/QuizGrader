import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Burner {

    /**
     * To Write:
     * <p>
     * Problem Specific:
     * - tags
     * - comments
     * - scores
     * - concept understood
     * <p>
     * Student Specific
     * - grades
     * - percentages
     * - totals
     */
    public static void writeEmailExportable() {
        ArrayList<String> report = new ArrayList<>();
        for (String student : UserInteractiveGrader.students.keySet()) {

            StringBuilder studentReport = new StringBuilder();

            HashMap<Integer, ArrayList<String>> tags = UserInteractiveGrader.tags.get(student);
            HashMap<Integer, String> comments = UserInteractiveGrader.comments.get(student);
            HashMap<Integer, Score> scores = UserInteractiveGrader.scores.get(student);
            HashMap<Integer, Integer> conceptUnderstood = UserInteractiveGrader.conceptUnderstood.get(student);

//            System.out.println(tags);
//            System.out.println(comments);
//            System.out.println(scores);
//            System.out.println(conceptUnderstood);

            Character grade = UserInteractiveGrader.grades.get(student);
            Score total = UserInteractiveGrader.totals.get(student);
            Double percentage = UserInteractiveGrader.percentages.get(student);

            studentReport.append("\n");
            studentReport.append(student).append("\n");

            for (int i = 1; i <= UserInteractiveGrader.numOfProblems; i++) {
                studentReport.append("Problem ").append(i).append(": ").append("\n");
                studentReport.append("Tags: ");
                for (int j = 0; j < tags.get(i).size(); j++) {
                    studentReport.append(tags.get(i).get(j));
                    if (j != tags.get(i).size() - 1) studentReport.append(", ");
                }
                studentReport.append("\n");
                studentReport.append("Comment: ");
                studentReport.append(comments.get(i)).append("\n");
                studentReport.append("Score: ");
                studentReport.append(scores.get(i)).append("\n");
                studentReport.append("Main Concept Understood: ");
                if (conceptUnderstood.get(i) == 1) {
                    studentReport.append("yes");
                } else studentReport.append("no");
                studentReport.append("\n");
            }

            studentReport.append("Assessment Summary: \n");
            studentReport.append("Total Grade: ").append(grade).append("\n");
            studentReport.append("Total Points Earned: ").append(total).append("\n");
            studentReport.append("Grade Percent: ").append(percentage).append("\n");

            report.add(studentReport.toString());
        }
        write(report, "Data/EmailExportable.txt");
    }

    public static void writeSoftwareExportable() {
        ArrayList<String> csvFormatted = new ArrayList<>();

        StringBuilder title = new StringBuilder("name, ");
        for (int i = 1; i <= UserInteractiveGrader.numOfProblems; i++) {
            title.append(i + ", ");
        }
        title.append("total");

        csvFormatted.add(title.toString());

        for (String student : UserInteractiveGrader.students.keySet()) {

            StringBuilder studentReport = new StringBuilder();
            HashMap<Integer, Score> scores = UserInteractiveGrader.scores.get(student);
            Score total = UserInteractiveGrader.totals.get(student);

            studentReport.append(student).append(", ");
            for (int i = 1; i <= UserInteractiveGrader.numOfProblems; i++) {
                studentReport.append(scores.get(i).getPercent()).append(", ");
            }
            studentReport.append(total.getParsedPercent());

            csvFormatted.add(studentReport.toString());
        }
        write(csvFormatted, "Data/SoftwareExportable.csv");
    }

    public static void write(ArrayList<String> report, String filepath) {
        File data = new File(filepath);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(data));
            for (int i = 0; i < report.size(); i++) {
                writer.write(report.get(i));
                if (i < report.size() - 1) writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearFile(String filepath) {
        Burner.write(new ArrayList<>(), filepath);
    }

    public static void deleteFolder(File folder) {
        deleteAllFilesFromTree(folder);
        deleteAllFoldersFromTree(folder);
    }

    public static void deleteAllFoldersFromTree(File folder) {
        File[] containedFiles = folder.listFiles();
        if (containedFiles.length == 0) folder.delete();
        else {
            for (File file : containedFiles) {
                deleteAllFoldersFromTree(file);
            }
        }
        folder.delete();
    }

    public static void deleteAllFilesFromTree(File folder) {
        File[] containedFiles = folder.listFiles();
        for (File file : containedFiles) {
            if (file.isFile()) file.delete();
            else {
                deleteAllFilesFromTree(file);
            }
        }
    }

    private static int countDirs(File[] files) {
        int i = 0;
        for (File file : files) {
            if (file.isDirectory()) i++;
        }
        return i;
    }

    private static int countFiles(File[] files) {
        int i = 0;
        for (File file : files) {
            if (file.isFile()) i++;
        }
        return i;
    }
}
