import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InputPane {

    private JFrame frame;
    private JPanel mainPanel;

    private JLabel imageLabel;
    private JTextField message;
    private JTextField inputTextField;
    private JButton submit;

    private boolean clicked;
    private String input;

    private int labelWidth = 150;
    private int labelHeight = 120;

    public InputPane(String messageDialogue) {

        clicked = false;

        frame = new JFrame("QUIZ GRADER");
        frame.add(mainPanel);
        frame.pack();

        submit.setOpaque(true);
        submit.setBorderPainted(false);
        submit.setBackground(new Color(200, 197, 255));

        message.setText(messageDialogue);

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input = inputTextField.getText();
                clicked = true;
            }
        });
        submit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                submit.setBackground(new Color(141, 140, 180));
            }
        });
        submit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                submit.setBackground(new Color(200, 197, 255));
            }
        });
    }

    public void display() {
        frame.setVisible(true);
        pack();
    }

    public String getInput() {
        display();
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

    public void pack() {

        int width = 0;
        frame.pack();

        width += labelWidth;
        for (int i = 0; i < message.getText().split("").length; i++) {
            width += message.getFont().getSize();
        }

        width *= (double) 3/4; // this ratio seems to work

        setSize(width, frame.getHeight());
    }

    public void setSize(int width, int height) {
        frame.setSize(new Dimension(width, height));
    }

    public int getWidth() {
        return frame.getWidth();
    }

    public int getHeight() {
        return frame.getHeight();
    }

    public InputPane centered() {
        pack();
        setLocation((int) Constants.screenWidth / 2 - getWidth() / 2, (int) Constants.screenHeight / 2 - getHeight() / 2);
        return this;
    }

    private void createUIComponents() {
        imageLabel = new JLabel(new QGImage("smallLogo.png").resize(labelHeight, labelWidth, true).getIcon());
    }
}
