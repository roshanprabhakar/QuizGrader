import com.sun.tools.internal.jxc.ap.Const;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private HashMap<String, Character> grades;
    private HashMap<String, Double> percentages;
    private ArrayList<Student> students;

    public JFrame getFrame() {
        return frame;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public Report(HashMap<String, HashMap<Integer, Score>> scores, HashMap<String, HashMap<Integer, ArrayList<String>>> tags, int numOfProblems) {

        this.scores = scores;
        this.tags = tags;
        this.grades = new HashMap<>();
        this.percentages = new HashMap<>();

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
            grades.put(student, Constants.findGrade(score));
            percentages.put(student, score.getPercent());

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
                        "line-height: 25pt; " +
                        "font-size: 12px; " +
                        "font-family: Optima; " +
                        "background-color: #bec7d6;" +
                        "}"
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
        reportWriteable += "Most common grade: " + getMostCommonGrade() + "<br><br>";
        reportWriteable += "Average percentage: " + getAveragePercent() + "<br><br>";
        reportWriteable += "Lowest scorers: " + getLowestScorers(3) + "<br><br>";
        reportWriteable += "Highest scorers: " + getHighestScoreres(3) + "<br><br>";
        reportWriteable += "Tags (most to least common): " + getOrderedTags() + "<br><br>";

        reportWriteable.replaceAll(" ", "&nbsp");

        classReportPane.setBorder(BorderFactory.createCompoundBorder(
                mainPanel.getBorder(),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        classReportPane.setEditorKit(kit);
        classReportPane.setText(reportWriteable);

//        System.out.println(grades);

        sendInformationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO Generate CSV with all necessary data
            }
        });
    }

    public void display() {
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        reportPane = new JEditorPane("text/html", "");
    }

    //I know its ugly but its not the most important thing right now
    public String getMostCommonGrade() {
        int[] grades = new int[]{0, 0, 0, 0, 0};
        for (String student : this.grades.keySet()) {
            if (this.grades.get(student) == 'A') {
                grades[4]++;
            } else if (this.grades.get(student) == 'B') {
                grades[3]++;
            } else if (this.grades.get(student) == 'C') {
                grades[2]++;
            } else if (this.grades.get(student) == 'D') {
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
        HashMap<String, Double> copy = new HashMap<>(percentages);
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
        String lowestName = "empty HashMap";
        double lowestPercent = 101;
        for (String student : percentages.keySet()) {
            if (percentages.get(student) < lowestPercent) {
                lowestName = student;
                lowestPercent = percentages.get(student);
            }
        }
        return lowestName;
    }

    public String getHighestScoreres(int n) {

        ArrayList<String> highest = new ArrayList<>();
        HashMap<String, Double> copy = new HashMap<>(percentages);
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

    public String getOrderedTags() {
        return "to implement";
    }


}
