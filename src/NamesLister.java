import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Arrays;

public class NamesLister extends Window {

    private JPanel mainPanel;
    private JButton submitButton;
    private JButton skipButton;
    private JTextField inputField;
    private JTextField namesField;

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

        namesField.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        submitButton.setOpaque(true);
        submitButton.setBorderPainted(false);
        submitButton.setBackground(new Color(200, 197, 255, 255));

        skipButton.setOpaque(true);
        skipButton.setBorderPainted(false);
        skipButton.setBackground(new Color(200, 197, 255, 255));

        inputField.setBorder(new LineBorder(new Color(200, 197, 255, 255), 5));

        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSubmitOperations();
                frame.setVisible(false);
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

    public void prompt() {
        frame.pack();
        frame.setSize(new Dimension(500, frame.getHeight()));
        this.centerAt(new Point((int) (Constants.screenWidth / 2), (int) (Constants.screenHeight / 2)));
        frame.setVisible(true);
        while (!submitted) {
            System.out.print("");
        }
    }

    private void performSubmitOperations() {
        namesInput = inputField.getText();
        String[] names = smartParser(namesInput);
        write(names);
        submitted = true;
    }


    private String[] smartParser(String input) {
        String[] names = input.split(",");
        for (int i = 0; i < names.length; i++) {
            names[i] = names[i].trim();
        }
        return names;
    }

    private void write(String[] names) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("names.txt")));
            for (int i = 0; i < names.length; i++) {
                writer.write(names[i]);
                if (i != names.length - 1) writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not find or read input file \"names.txt\"");
        }
    }
}
