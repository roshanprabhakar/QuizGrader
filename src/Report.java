import javax.swing.*;
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

    public JFrame getFrame() {
        return frame;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public Report(HashMap<String, HashMap<Integer, Score>> scores, int numOfProblems) {

        frame = new JFrame("Class Report");

        String writeable = "";
        for (String student : scores.keySet()) {
            int suggestedTotal = 0;
            int suggestedEarned = 0;
            writeable += "<b>" + student + "</b>" + ": " + "<br>";
            writeable += "\n" + "       ";
            for (int i = 1; i <= numOfProblems; i++) {
                suggestedEarned += scores.get(student).get(i).getEarned();
                suggestedTotal += scores.get(student).get(i).getPossible();
                writeable += i + ": " + scores.get(student).get(i).toString() + "<br>";
            }
            Score score = new Score(suggestedEarned, suggestedTotal);
            writeable += "  total: " + score.toString() + ", " + findGrade(score) + "<br><br>";
        }

        reportPane.setBorder(BorderFactory.createCompoundBorder(
                mainPanel.getBorder(),
                BorderFactory.createEmptyBorder(5, 15, 15, 15)));

        reportPane.setText(writeable);


        sendInformationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO Update this code
            }
        });
    }

    public void display() {
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private Character findGrade(Score score) {
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

    private void createUIComponents() {
        reportPane = new JEditorPane("text/html", "");
    }
}
