import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Canvas {

    private JFrame frame;

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

    public Canvas(QGImage image, String name, int problemNum) {

        this.image = image;
        this.tags = new ArrayList<>();

        frame = new JFrame(name + " #" + problemNum);
        frame.add(mainPanel);

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

                if (customTags.getText().equals("Tags")) customTags.setText("");
                String[] customTagsCollected = customTags.getText().split(", ");

                Constants.removeDuplicates(customTagsCollected);

                for (String label : customTagsCollected) {
                    tags.add(label);
                    if (!UserInteractiveGrading.menuLabels.contains(label.toLowerCase()))
                        UserInteractiveGrading.menuLabels.add(label.toLowerCase());
                }

                //deal with
                scoreObject = new Score(Double.parseDouble(score.getText().split("/")[0]), Double.parseDouble(score.getText().split("/")[1]));

                UserInteractiveGrading.scores.get(name).put(problemNum, scoreObject);
                UserInteractiveGrading.students.get(name).addScore(problemNum, scoreObject);

                frame.setVisible(false);

                for (String tag : tags) {
                    UserInteractiveGrading.tags.get(name).get(problemNum).add(tag);
                }

                UserInteractiveGrading.updateCanvi();
                UserInteractiveGrading.updateScoresForProblem(problemNum, score.getText().split("/")[1]);

                UserInteractiveGrading.submittedProblems++;
                
                for (String tag : tags) {
                    UserInteractiveGrading.students.get(name).addTag(problemNum, tag);    
                }

                if (answeredCorrectly) {
                    UserInteractiveGrading.conceptUnderstood.get(name).put(problemNum, 1);
                }  else {
                    UserInteractiveGrading.conceptUnderstood.get(name).put(problemNum, 0);
                }

                if (!commentWritten) {
                    UserInteractiveGrading.comments.get(name).put(problemNum, "");
                }

                UserInteractiveGrading.logger.log("Comment written: ");
                UserInteractiveGrading.logger.log(UserInteractiveGrading.comments.get(name).get(problemNum));
                UserInteractiveGrading.logger.log("tags (specified): ");
                UserInteractiveGrading.logger.log(UserInteractiveGrading.tags);
                UserInteractiveGrading.logger.log("scores: ");
                UserInteractiveGrading.logger.log(UserInteractiveGrading.scores);
                UserInteractiveGrading.logger.log("all tags: ");
                UserInteractiveGrading.logger.log(UserInteractiveGrading.menuLabels);
                UserInteractiveGrading.logger.log("number of submitted problems: ");
                UserInteractiveGrading.logger.log(UserInteractiveGrading.submittedProblems);
                UserInteractiveGrading.logger.log(UserInteractiveGrading.conceptUnderstood);
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
}