import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class WindowManagerTestWindow extends Window {

    private JButton submit;
    private JPanel mainPanel;

    public WindowManagerTestWindow() {
        frame.add(mainPanel);

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void submitAction() {
        frame.setVisible(false);
        Main.manager.update();
    }
}
