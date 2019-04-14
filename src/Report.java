import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Report {

    private JFrame frame;

    private JPanel mainPanel;
    private JTextField title;
    private JButton sendInformationButton;
    private JEditorPane reportPane;
    private JTextField classReport;
    private JEditorPane classReportPane;

    private HashMap<String, HashMap<Integer, Score>> scores;
    private HashMap<String, HashMap<Integer, ArrayList<String>>> tags;

    private HashMap<String, HashMap<Integer, Integer>> binary; //only needs to be accessed in this class

    public JFrame getFrame() {
        return frame;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public Report(HashMap<String, HashMap<Integer, Score>> scores, HashMap<String, HashMap<Integer, ArrayList<String>>> tags, int numOfProblems) {

        this.scores = scores;
        this.tags = tags;
        this.binary = new HashMap<>();

        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet styleSheet = kit.getStyleSheet();

        frame = new JFrame("Class Report");
        frame.setPreferredSize(new Dimension(800, 700));

        reportPane.setEditable(false);

        String writeable = "";

        for (String student : scores.keySet()) {

            int suggestedTotal = 0;
            int suggestedEarned = 0;

            writeable += "<p>";
            writeable += "<b>" + student + "</b>" + ":   ";

            for (Integer i = 1; i <= numOfProblems; i++) {

                writeable += i + ": " + scores.get(student).get(i) + "  ";

                suggestedEarned += scores.get(student).get(i).getEarned();
                suggestedTotal += scores.get(student).get(i).getPossible();

            }

            Score score = new Score(suggestedEarned, suggestedTotal);
            UserInteractiveGrading.grades.put(student, Constants.findGrade(score));
            UserInteractiveGrading.percentages.put(student, score.getPercent());
            UserInteractiveGrading.totals.put(student, new Score(suggestedEarned, suggestedTotal));

            writeable += ("    total: " + score.toString() + ", " + Constants.findGrade(score) + "<br>");
            writeable += "</p>";
            writeable += "<br>";
        }


        styleSheet.addRule(
                "p {" +
                        "width: 60em; " +
                        "margin: 0 auto; " +
                        "max-width: 100%; " +
                        "line-height: 1.5; " +
                        "line-height: 25pt; " +
                        "font-size: 12px; " +
                        "font-family: Optima; " +
                        "background-color: #bec7d6;" +
                        "padding: 10px;" + "}"
        );

        reportPane.setBorder(BorderFactory.createCompoundBorder(
                mainPanel.getBorder(),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));


        reportPane.setEditorKit(kit);
        reportPane.setText(writeable);

        HTMLEditorKit reportHtml = new HTMLEditorKit();
        StyleSheet reportStyles = reportHtml.getStyleSheet();

        String reportWriteable = "";

        //all methods return html formatted strings
        reportWriteable += "<p>";
        reportWriteable += "Most common grade: " + getMostCommonGrade() + "<br>";
        reportWriteable += "Average percentage: " + getAveragePercent() + "%<br>";

        reportWriteable += "Lowest scorers: " + getLowestScorers(3) + "<br>";
        reportWriteable += "Highest scorers: " + getHighestScoreres(3) + "<br>";

        //recursion
        reportWriteable += "Tags (most to least common): " + catchOrderedTags(3) + "<br>";

        reportWriteable.replaceAll(" ", "&nbsp");

        classReportPane.setBorder(BorderFactory.createCompoundBorder(
                mainPanel.getBorder(),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        classReportPane.setEditorKit(kit);
        classReportPane.setText(reportWriteable);

        sendInformationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CSVgenerator generator = new CSVgenerator();
                generator.organizeBinaryData();
                generator.organizeGradeData();
            }
        });
    }

    private String catchOrderedTags(int n) {
        if (n == 0) return "";
        try {
            return getOrderedTags(n);
        } catch (IndexOutOfBoundsException e) {
            return catchOrderedTags(n - 1);
        }
    }

    public void display() {
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        reportPane = new JEditorPane("text/html", "");
    }

    public String getMostCommonGrade() {
        int[] grades = new int[]{0, 0, 0, 0, 0};
        for (String student : UserInteractiveGrading.grades.keySet()) {
            if (UserInteractiveGrading.grades.get(student) == 'A') {
                grades[4]++;
            } else if (UserInteractiveGrading.grades.get(student) == 'B') {
                grades[3]++;
            } else if (UserInteractiveGrading.grades.get(student) == 'C') {
                grades[2]++;
            } else if (UserInteractiveGrading.grades.get(student) == 'D') {
                grades[1]++;
            } else {
                grades[0]++;
            }
        }
        return Constants.simpGrades[Constants.mode(grades)];
    }

    public double getAveragePercent() {

        double totalPercent = 0;
        int numStudents = UserInteractiveGrading.students.size();
        for (String student : UserInteractiveGrading.students.keySet()) {
            totalPercent += UserInteractiveGrading.students.get(student).getTotal().getPercent();
            System.out.println(totalPercent);
        }

        return ((int) (totalPercent / numStudents) * 100) / 100;
    }

    public String getLowestScorers(int n) {

        ArrayList<String> lowest = new ArrayList<>();
        HashMap<String, Double> copy = new HashMap<>(UserInteractiveGrading.percentages);
        for (int i = 0; i < n; i++) {
            lowest.add(lowestScore(copy));
            copy.remove(lowestScore(copy));
        }

        String out = "";
        for (int i = 0; i < lowest.size(); i++) {
            if (i != lowest.size() - 1) {
                out += lowest.get(i) + ", ";
            } else {
                out += lowest.get(i);
            }
        }

        return out;
    }

    private String lowestScore(HashMap<String, Double> percentages) {
        System.out.println("##################################");
        System.out.println(percentages);
        String lowestName = "empty HashMap";
        double lowestPercent = 101;
        for (String student : percentages.keySet()) {
            System.out.println(student);
            if (percentages.get(student) < lowestPercent) {
                lowestName = student;
                lowestPercent = percentages.get(student);
            }
        }
        return lowestName;
    }

    public String getHighestScoreres(int n) {

        ArrayList<String> highest = new ArrayList<>();
        HashMap<String, Double> copy = new HashMap<>(UserInteractiveGrading.percentages);
        for (int i = 0; i < n; i++) {
            highest.add(highestScore(copy));
            copy.remove(highestScore(copy));
        }

        String out = "";
        for (int i = 0; i < highest.size(); i++) {
            if (i != highest.size() - 1) {
                out += highest.get(i) + ", ";
            } else {
                out += highest.get(i);
            }
        }

        return out;
    }

    private String highestScore(HashMap<String, Double> percentages) {
        String highestName = "empty hashmap";
        double highestPercentage = -1;
        for (String student : percentages.keySet()) {
            if (percentages.get(student) > highestPercentage) {
                highestName = student;
                highestPercentage = percentages.get(student);
            }
        }
        return highestName;
    }

    public String getOrderedTags(int n) {

        StringBuilder out = new StringBuilder();
        ArrayList<String> tagsCopy = new ArrayList<>(UserInteractiveGrading.menuLabels);

        for (int i = 0; i < n; i++) {
            if (i < n - 1) {
                out.append(mostFrequent(tagsCopy));
                tagsCopy.remove(mostFrequent(tagsCopy));
                if (i != n - 1) {
                    out.append(", ");
                }
            } else {
                out.append(mostFrequent(tagsCopy));
                tagsCopy.remove(mostFrequent(tagsCopy));
            }
        }

        return out.toString();
    }

    private String mostFrequent(ArrayList<String> labels) {
        ArrayList<String> uniqueLabels = getUniqueLabels(labels);
        int[] count = new int[uniqueLabels.size()];
        for (String label : labels) {
            count[uniqueLabels.indexOf(label)]++;
        }
        return uniqueLabels.get(Constants.mode(count));
    }

    private ArrayList<String> getUniqueLabels(ArrayList<String> labels) {
        ArrayList<String> uniqueLabels = new ArrayList<>();
        for (String label : labels) {
            if (!uniqueLabels.contains(label)) {
                uniqueLabels.add(label);
            }
        }
        return uniqueLabels;
    }


}
