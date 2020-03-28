import javax.swing.*;
import java.awt.*;

public class ProgressPane extends Window {

    private JTextField header;
    private JProgressBar progressBar;
    private JPanel mainPanel;

    public ProgressPane() {

        frame = new JFrame();
        frame.add(mainPanel);
        frame.setPreferredSize(new Dimension(400, 100));

        progressBar.setMinimum(0);
        progressBar.setMaximum(300);

        progressBar.setPreferredSize(new Dimension(380, progressBar.getHeight()));

        header.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
        frame.pack();

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
        center(); //needs to be called after pack()
        frame.setVisible(true);
    }

    private void center() {
        setLocation((int) Constants.screenWidth / 2 - frame.getWidth() / 2, (int) Constants.screenHeight / 2 - frame.getHeight() / 2);
    }

    public void close() {
        frame.setVisible(false);
    }

    public void appendMessage(String message) {
        header.setText(header.getText() + message);
    }

}
