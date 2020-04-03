import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Burner {

    /**
     * To Write:
     *
     * Problem Specific:
     * - tags
     * - comments
     * - scores
     * - concept understood
     *
     * Student Specific
     * - grades
     * - percentages
     * - totals
     */
    public static void writeAll() {
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
        write(report);
    }

    public static void write(ArrayList<String> report) {
        File data = new File("Data/out.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(data));
            for (String studentReport : report) {
                writer.write(studentReport + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}