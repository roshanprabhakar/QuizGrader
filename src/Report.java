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
            writeable += "<b>" + student + "</b>" + ": ";
            for (int i = 1; i <= numOfProblems; i++) {
                suggestedEarned += scores.get(student).get(i).getEarned();
                suggestedTotal += scores.get(student).get(i).getPossible();
                writeable += i + ":    " + scores.get(student).get(i).toString() + ", ";

            }

            Score score = new Score(suggestedEarned, suggestedTotal);
            grades.put(student, Constants.findGrade(score));

            writeable += "  total: " + score.toString() + ", " + Constants.findGrade(score) + "<br>";
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
        reportWriteable += "Most common grade: " + getMostCommonGrade() + "<br>";
        reportWriteable += "Average percentage: " + getAveragePercent() + "%<br>";
        reportWriteable += "Lowest scorers: " + getLowestScorers() + "<br>";
        reportWriteable += "Highest scorers: " + getHighestScoreres() + "<br>";
        reportWriteable += "Tags (most to least common):x " + getOrderedTags() + "<br>";

        classReportPane.setBorder(BorderFactory.createCompoundBorder(
                mainPanel.getBorder(),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        classReportPane.setEditorKit(kit);
        classReportPane.setText(reportWriteable);

        System.out.println(grades);

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

    public String getLowestScorers() {
        return "to implement";
    }

    public String getHighestScoreres() {
        return "to implement";
    }

    public String getOrderedTags() {
        return "to implement";
    }




}
