import javax.swing.*;
import java.awt.*;

public class ProgressPane {

    private JFrame frame;

    private JTextField header;
    private JProgressBar progressBar;
    private JPanel mainPanel;

    public ProgressPane() {

        frame = new JFrame();
        frame.add(mainPanel);
        frame.setPreferredSize(new Dimension(400, 100));

        progressBar.setMinimum(0);
        progressBar.setMaximum(300);
    }

    public void setMessage(String message) {
        header.setText(message);
    }

    public void clearHeader() {
        header.setText("");
    }

    public void resetProgressBar() {
        progressBar.setValue(0);
    }

    public void setProgressBarPercentage(int percent) {
        progressBar.setValue(percent);
    }

    public void display() {
        frame.pack();
        frame.setVisible(true);
    }
}
