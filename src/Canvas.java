import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Canvas extends Window {

    private JPanel mainPanel;
    private JLabel imageLabel;
    private JComboBox menu;
    private JTextField customTags;
    private JTextField score;
    private JButton submit;
    private JCheckBox conceptUnderstood;
    private JButton comment;

    private QGImage image;
    private ArrayList<String> tags;
    private Score scoreObject;

    private boolean canTagsDisappear = true;
    private boolean canScoreDisappear = true;

    private boolean answeredCorrectly = false;

    private boolean commentWritten = false;
    private boolean submitted = false;

    public Canvas(QGImage image, String name, int problemNum) {

        this.image = image;
        this.tags = new ArrayList<>();

        frame = new JFrame(name + " #" + problemNum);
        frame.add(mainPanel);
        frame.setName(name + ": " + problemNum);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Color scheme
        menu.setBackground(Color.LIGHT_GRAY);
        customTags.setBackground(Color.LIGHT_GRAY);
        score.setBackground(Color.LIGHT_GRAY);
        submit.setForeground(Color.BLACK);
        mainPanel.setBackground(Color.DARK_GRAY);
        //----------

        customTags.setText("Tags");
        score.setText("Score/Total");

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                performSubmitOperations(name, problemNum);

                //window manager operations
                frame.setVisible(false);
                UserInteractiveGrader.submittedProblems++;
                UserInteractiveGrader.manager.update();
                UserInteractiveGrader.manager.incrementClosed();
            }
        });

        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (customTags.getText().equals("Tags")) customTags.setText("");
                if (customTags.getText().equals("")) customTags.setText(menu.getSelectedItem().toString());
                else customTags.setText(customTags.getText() + ", " + menu.getSelectedItem().toString());
                menu.removeItem(menu.getSelectedItem());
            }
        });

        customTags.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (canTagsDisappear && customTags.getText().equals("Tags")) {
                    customTags.setText("");
                    canTagsDisappear = false;
                }
            }
        });

        score.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (canScoreDisappear && score.getText().equals("Score/Total")) {
                    score.setText("");
                    canScoreDisappear = false;
                }
                score.setCaretPosition(0);
            }
        });

        conceptUnderstood.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                answeredCorrectly = true;
            }
        });

        comment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ResponseDescriptor descriptor = new ResponseDescriptor(name, problemNum);
                descriptor.display();
                if (!descriptor.getText().equals("")) {
                    commentWritten = true;
                }
            }
        });

        frame.pack();
    }

    private void createUIComponents() {
        this.imageLabel = new JLabel(image.getIcon());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public Score getScoreObject() {
        return scoreObject;
    }

    public JFrame getFrame() {
        return this.frame;
    }

    public void addLabel(String label) {
        this.menu.addItem(label);
    }

    public boolean contains(String label) {
        for (int i = 0; i < menu.getItemCount(); i++) {
            if (menu.getItemAt(i).equals(label)) return true;
        }
        return false;
    }

    public boolean isAnsweredCorrectly() {
        return answeredCorrectly;
    }

    public void setScoreField(String total) {
        if (score.getText().trim().equals("Score/Total")) {
            score.setText("/" + total);
        }
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public int getWidth() {
        return frame.getWidth();
    }

    public int getHeight() {
        return frame.getHeight();
    }

    public Point getLocation() {
        return frame.getLocation();
    }

    public void performSubmitOperations(String name, int problemNum) {
        //parses tag data from canvas, stores in customTagsCollected
        if (customTags.getText().equals("Tags")) customTags.setText("");
        String[] customTagsCollected = customTags.getText().split(", ");
        Constants.removeDuplicates(customTagsCollected);

        //updates local index of tags, as well as program global index in uig
        for (String label : customTagsCollected) {
            tags.add(label);
            if (!UserInteractiveGrader.menuLabels.contains(label.toLowerCase()))
                UserInteractiveGrader.menuLabels.add(label.toLowerCase());
        }

        //updates student object map with tag info
        for (String tag : tags) {
            UserInteractiveGrader.students.get(name).addTag(problemNum, tag);
        }

        //parses score
        scoreObject = new Score(Double.parseDouble(score.getText().split("/")[0]), Double.parseDouble(score.getText().split("/")[1]));

        //adds score info to uig index of students
        UserInteractiveGrader.scores.get(name).put(problemNum, scoreObject);
        UserInteractiveGrader.students.get(name).addScore(problemNum, scoreObject);

        //updating student-specific tag info in uig
        for (String tag : tags) {
            UserInteractiveGrader.tags.get(name).get(problemNum).add(tag);
        }

        UserInteractiveGrader.updateCanvi(); //adds tags to other canvii
        UserInteractiveGrader.updateScoresForProblem(problemNum, score.getText().split("/")[1]); //changes score default text for other canvii

        //updates concept understood correctly
        if (answeredCorrectly) {
            UserInteractiveGrader.conceptUnderstood.get(name).put(problemNum, 1);
        }  else {
            UserInteractiveGrader.conceptUnderstood.get(name).put(problemNum, 0);
        }

        //in case no comments were written, adds an empty string to that student's index of comments
        if (!commentWritten) {
            UserInteractiveGrader.comments.get(name).put(problemNum, "");
        }

        submitted = true;
    }
}
