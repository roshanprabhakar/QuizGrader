import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CSVgenerator {

    private File name_to_grade;
    private File outBinary;
    private File comments;
    private File tags;

    private int numOfProblems;

    public CSVgenerator(int numOfProblems) {

        //Generate needed file container
        try {

            File outDirectory = new File(Constants.outCSV);

            name_to_grade = new File(Constants.outCSV + File.separator + "scores.csv");
            outBinary = new File(Constants.outCSV + File.separator + "BinaryScores.csv");
            comments = new File(Constants.outCSV + File.separator + "comments.csv");
            tags = new File(Constants.outCSV + File.separator + "tags.csv");

            if (!outDirectory.exists()) {outDirectory.mkdir();}

        } catch (Exception e) {
            UserInteractiveGrading.logger.log("Could not create CSV folder");
        }

        this.numOfProblems = numOfProblems;
    }

    public void organizeGradeData() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(name_to_grade));

            //the body of the file
            StringBuilder builder = new StringBuilder();

            //the first row of the file
            StringBuilder title = new StringBuilder();

            title.append("name, ");

            for (int i = 1; i <= UserInteractiveGrading.numOfProblems; i++) {
                title.append(i + " (%), ");
            }

            for (String student : UserInteractiveGrading.scores.keySet()) {
                builder.append(student + ", ");


                UserInteractiveGrading.logger.log("scores.get(student): ");
                UserInteractiveGrading.logger.log(UserInteractiveGrading.scores.get(student));
                for (int i = 1; i <= UserInteractiveGrading.numOfProblems; i++) {
                    builder.append(UserInteractiveGrading.scores.get(student).get(i).getPercent() + ", ");
                }

                builder.append(UserInteractiveGrading.totals.get(student).getPercent() + ", ");
                builder.append(UserInteractiveGrading.grades.get(student));
                builder.append("\n");
            }

            title.append("total (%), ");
            title.append("grade");

            out.write(title.toString() + "\n");
            out.write(builder.toString());

            out.close();

        } catch (Exception e) {
            UserInteractiveGrading.logger.log("Could not populate scores.csv");
        }
    }

    public void organizeBinaryData() {

        try {

            if (!outBinary.exists()) outBinary.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(outBinary));

            //first row of file
            StringBuilder title = new StringBuilder();

            //body of file
            StringBuilder body = new StringBuilder();

            UserInteractiveGrading.logger.log("answered correctly: " + UserInteractiveGrading.conceptUnderstood);

            title.append("name, ");
            for (int i = 1; i <= UserInteractiveGrading.numOfProblems; i++) {
                title.append(i + ", ");
            }
            for (String student : UserInteractiveGrading.conceptUnderstood.keySet()) {
                body.append(student + ", ");
                for (int i = 1; i <= UserInteractiveGrading.numOfProblems; i++) {
                    body.append(UserInteractiveGrading.conceptUnderstood.get(student).get(i) + ", ");
                }
                body.append("\n");
            }

            out.write(title + "\n");
            out.write(body.toString());
            out.close();

        } catch (IOException exception) {
            UserInteractiveGrading.logger.log("could not generate or populate binary reports file");
        }

    }

    public void writeComments() {
        UserInteractiveGrading.logger.log("------------- Writing Comments -------------");
        UserInteractiveGrading.logger.log(UserInteractiveGrading.comments);

        try {

            if (!comments.exists()) comments.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(comments));

            //first row
            StringBuilder title = new StringBuilder();

            //rows 1 to n
            StringBuilder body = new StringBuilder();

            title.append("student, ");

            for (int i = 1; i <= numOfProblems; i++) {
                title.append(i + ", ");
            }

            for (String student : UserInteractiveGrading.comments.keySet()) {

                UserInteractiveGrading.logger.log("student: " + student);
                body.append(student + ", ");

                for (int i = 1; i <= numOfProblems; i++) {
                    UserInteractiveGrading.logger.log("number: " + i);

                    UserInteractiveGrading.logger.log("######################");
                    UserInteractiveGrading.logger.log(UserInteractiveGrading.comments.get(student));
                    if (UserInteractiveGrading.comments.get(student).get(i).equals("")) {
                        body.append("no comment,");
                    } else {
                        body.append(UserInteractiveGrading.comments.get(student).get(i));
                    }

                    UserInteractiveGrading.logger.log("title: " + title);
                    UserInteractiveGrading.logger.log("body: " + body);
                }
                body.append("\n");
            }

            writer.write(title.toString() + "\n");
            writer.write(body.toString());
            writer.close();

        } catch (IOException e) {
            UserInteractiveGrading.logger.log("Could not generate csv for comments");
        }
    }

    public void writeTags() {

        try {

            if (!tags.exists()) tags.createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(tags));

            StringBuilder title = new StringBuilder();
            StringBuilder body = new StringBuilder();

            title.append("student, ");

            for (int i = 1; i <= numOfProblems; i++) {
                title.append(i + ", ");
            }

            title.append("\n");

            for (String student : UserInteractiveGrading.tags.keySet()) {
                body.append(student + ", ");
                for (int i = 1; i <= numOfProblems; i++) {
                    body.append(UserInteractiveGrading.tags.get(student).get(i) + ", ");
                }
                body.append("\n");
            }

            writer.write(title.toString());
            writer.write(body.toString());

            writer.close();

        } catch (IOException e) {
            UserInteractiveGrading.logger.log("Could not generate csv for tags");
        }
    }
}
