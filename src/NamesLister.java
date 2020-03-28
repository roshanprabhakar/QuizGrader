import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class NamesLister extends Window {

    private JPanel mainPanel;
    private JButton submitButton;
    private JButton skipButton;
    private JTextField inputField;

    private String namesInput;
    private boolean submitted;

    /**
     * A GUI intended to retrieve a list of student names from the user
     * List only contains first names
     */
    public NamesLister() {

        frame = new JFrame();
        frame.add(mainPanel);

        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        submitButton.setOpaque(true);
        submitButton.setBorderPainted(false);
        submitButton.setBackground(new Color(200, 197, 255, 255));

        skipButton.setOpaque(true);
        skipButton.setBorderPainted(false);
        skipButton.setBackground(new Color(200, 197, 255, 255));

        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSubmitOperations();
            }
        };
        inputField.addActionListener(action);

        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                //when submit is clicked
                performSubmitOperations();
                frame.setVisible(false);
            }
        });
        skipButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                //when skipped is clicked
                frame.setVisible(false);
                submitted = true;
            }
        });
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                submitButton.setBackground(new Color(184, 149, 198));
            }
        });
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                submitButton.setBackground(new Color(200, 197, 255));
            }
        });
        skipButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                skipButton.setBackground(new Color(184, 149, 198));
            }
        });
        skipButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                skipButton.setBackground(new Color(200, 197, 255));
            }
        });
    }

    private void performSubmitOperations() {
        namesInput = inputField.getText();
        String[] names = smartParser(namesInput);
        write(names);
        submitted = true;
    }

    public void prompt() {
        frame.pack();
        frame.setSize(new Dimension(500, frame.getHeight()));
        this.center();
        frame.setVisible(true);
        while (!submitted) {
            System.out.print("");
        }
    }

    public void center() {
        setLocation((int) Constants.screenWidth / 2 - frame.getWidth() / 2, (int) Constants.screenHeight / 2 - frame.getHeight() / 2);
    }

    private String[] smartParser(String input) {
        input.trim();
        input.replaceAll(",", " ");
        input.replaceAll(" +", " ");
        return input.split(" ");
    }

    private void write(String[] names) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("names.txt")));
            for (String name : names) {
                writer.write(name + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not find or read input file \"names.txt\"");
        }
    }
}
