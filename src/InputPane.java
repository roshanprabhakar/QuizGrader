import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputPane {

    private JFrame frame;
    private JPanel mainPanel;

    private JLabel imageLabel;
    private JTextField message;
    private JTextField inputTextField;
    private JButton submit;

    private boolean clicked;
    private String input;

    public InputPane(String messageDialogue) {

        clicked = false;

        frame = new JFrame("QUIZ GRADER");
        frame.setSize(new Dimension(620, 230));
        frame.add(mainPanel);

        message.setText(messageDialogue);

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input = inputTextField.getText();
                clicked = true;
            }
        });
    }

    public JFrame display() {
        frame.setVisible(true);
        return frame;
    }

    public String getInput() {
        JFrame frame = display();
        while (!clicked) {
            System.out.print("");
        }
        frame.setVisible(false);
        return input;
    }

    public InputPane setLocation(int x, int y) {
        frame.setLocation(new Point(x, y));
        return this;
    }

    public int getWidth() {
        return frame.getWidth();
    }

    public int getHeight() {
        return frame.getHeight();
    }

    public InputPane centered() {
        setLocation((int) Constants.screenWidth / 2 - getWidth() / 2, (int) Constants.screenHeight / 2 - getHeight() / 2);
        return this;
    }

    private void createUIComponents() {
        imageLabel = new JLabel(new QGImage("smallLogo.png").resize(200,250, true).getIcon());
    }
}
