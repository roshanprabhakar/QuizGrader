import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import static javax.swing.BorderFactory.createEmptyBorder;

public class Report extends Window {

    private JTextField title;
    private JTextField classReport;

    private JPanel mainPanel;

    private JButton sendInformationButton;
    private JScrollPane scroller;

    private JEditorPane classReportPane;
    private JEditorPane totalReportPane;

    private int numOfProblems;


    public JPanel getMainPanel() {
        return mainPanel;
    }

    public Report(int numOfProblems) {

        frame = new JFrame("Class Report");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 700));

        removeBorder(totalReportPane);
        removeBorder(classReportPane);

        this.numOfProblems = numOfProblems;

        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet styleSheet = kit.getStyleSheet();

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

        totalReportPane.setEditorKit(kit);
        totalReportPane.setText(getScoreReport());

        classReportPane.setEditorKit(kit);
        classReportPane.setText(getClassReport());

        scroller.getVerticalScrollBar().setUnitIncrement(2);

        frame.pack();
        frame.add(mainPanel);

        sendInformationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                CSVgenerator generator = new CSVgenerator(numOfProblems);

                generator.organizeBinaryData();
                generator.organizeGradeData();
                generator.writeComments();
                generator.writeTags();


                UserInteractiveGrader.logCount++;
            }
        });
    }

    private void createUIComponents() {
        totalReportPane = new JEditorPane("text/html", "");
        removeBorder(scroller);
        scroller.getVerticalScrollBar().setUnitIncrement(1);
    }

    private void removeBorder(JComponent component) {
        component.setBorder(BorderFactory.createCompoundBorder(
                mainPanel.getBorder(),
                createEmptyBorder(5, 10, 5, 10))
        );
    }

    private String getClassReport() {
        StringBuilder reportWriteable = new StringBuilder();

        //all methods return html formatted strings
        reportWriteable.append("<p>");
        reportWriteable.append("Most common grade: " + getMostCommonGrade() + "<br>");
        reportWriteable.append("Average percentage: " + getAveragePercent() + "%<br>");

        reportWriteable.append("Lowest scorers: " + getLowestScorers(3) + "<br>");
        reportWriteable.append("Highest scorers: " + getHighestScoreres(3) + "<br>");

        //recursion
        reportWriteable.append("Tags (most to least common): " + catchOrderedTags(3) + "<br>");

        return reportWriteable.toString();//.replaceAll(" ", "&nbsp");
    }

    private String getScoreReport() {

        StringBuilder writeable = new StringBuilder();

        for (String student : UserInteractiveGrader.scores.keySet()) {

            int suggestedTotal = 0;
            int suggestedEarned = 0;

            writeable.append("<p>");
            writeable.append("<b>" + student + "</b>" + ":   ");

            for (Integer i = 1; i <= numOfProblems; i++) {

                writeable.append(i + ": " + UserInteractiveGrader.scores.get(student).get(i) + "  ");

                suggestedEarned += UserInteractiveGrader.scores.get(student).get(i).getEarned();
                suggestedTotal += UserInteractiveGrader.scores.get(student).get(i).getPossible();

            }

            Score score = new Score(suggestedEarned, suggestedTotal);
            UserInteractiveGrader.grades.put(student, Constants.findGrade(score));
            UserInteractiveGrader.percentages.put(student, score.getPercent());
            UserInteractiveGrader.totals.put(student, new Score(suggestedEarned, suggestedTotal));

            writeable.append("    total: " + score.toString() + ", " + Constants.findGrade(score) + "<br>");
            writeable.append("</p>");
            writeable.append("<br>");
        }

        return writeable.toString();
    }


    private String catchOrderedTags(int n) {
        if (n == 0) return "";
        try {
            return getOrderedTags(n);
        } catch (IndexOutOfBoundsException e) {
            return catchOrderedTags(n - 1);
        }
    }

    private String getOrderedTags(int n) {

        StringBuilder out = new StringBuilder();
        ArrayList<String> tagsCopy = new ArrayList<>(UserInteractiveGrader.menuLabels);

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

    public void display() {
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public String getMostCommonGrade() {
        int[] grades = new int[5];
        for (String student : UserInteractiveGrader.grades.keySet()) {

            System.err.println(Constants.indexOfSimpGrades(UserInteractiveGrader.grades.get(student)));
            System.err.println(UserInteractiveGrader.grades.get(student));

            grades[Constants.indexOfSimpGrades(UserInteractiveGrader.grades.get(student))]++;
        }
        return Constants.simpGrades[Constants.mode(grades)];
    }

    public double getAveragePercent() {

        double totalPercent = 0;
        int numStudents = UserInteractiveGrader.students.size();
        for (String student : UserInteractiveGrader.students.keySet()) {
            totalPercent += UserInteractiveGrader.students.get(student).getTotal().getPercent();

            System.err.println(totalPercent);
        }

        return ((int) (totalPercent / numStudents) * 100) / 100;
    }

    public String getLowestScorers(int n) {

        ArrayList<String> lowest = new ArrayList<>();
        HashMap<String, Double> copy = new HashMap<>(UserInteractiveGrader.percentages);
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

        System.err.println("-------- lowest scores ----------");
        System.err.println(percentages);

        String lowestName = "";
        double lowestPercent = 101;
        for (String student : percentages.keySet()) {

            System.err.println(student);

            if (percentages.get(student) < lowestPercent) {
                lowestName = student;
                lowestPercent = percentages.get(student);
            }
        }
        return lowestName;
    }

    public String getHighestScoreres(int n) {

        ArrayList<String> highest = new ArrayList<>();
        HashMap<String, Double> copy = new HashMap<>(UserInteractiveGrader.percentages);
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
        String highestName = "";
        double highestPercentage = -1;
        for (String student : percentages.keySet()) {
            if (percentages.get(student) > highestPercentage) {
                highestName = student;
                highestPercentage = percentages.get(student);
            }
        }
        return highestName;
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
