import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class NamesLister {

    private JFrame frame;

    private JPanel mainPanel;
    private JTextField namesTextField;
    private JTextField jackBobKatyTextField;
    private JButton submitButton;
    private JButton skipButton;

    private String namesInput;
    private boolean submitted;

    public NamesLister() {
        frame = new JFrame();
        frame.add(mainPanel);
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                //when submit is clicked
                namesInput = namesTextField.getText();
                String[] names = smartParser(namesInput);
                write(names);
                submitted = true;
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
                submitButton.setBackground(new Color(184, 149, 198));
            }
        });
        skipButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                submitButton.setBackground(new Color(200, 197, 255));
            }
        });
    }

    public void prompt() {
        frame.pack();
        frame.setVisible(true);
        while (!submitted) {
            System.out.print("");
        }
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
