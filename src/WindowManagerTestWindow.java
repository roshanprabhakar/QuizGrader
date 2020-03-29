import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class WindowManagerTestWindow extends Window {

    private JButton submit;
    private JPanel mainPanel;

    public WindowManagerTestWindow(int i, int width, int height) {

        frame = new JFrame(Integer.toString(i));
        frame.add(mainPanel);
        frame.pack();
        frame.setSize(new Dimension(width, height));

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitAction();
            }
        });
    }

    private void submitAction() {
        frame.setVisible(false);
        Main.manager.update();
    }
}
