import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Canvas extends Window {

    private JPanel mainPanel;
    private JLabel imageLabel;
    private JComboBox<String> menu;
    private JTextField customTags;
    private JTextField score;
    private JButton submit;
    private JCheckBox conceptUnderstood;
    private JButton comment;

    private ActionListener menuListener;

    private QGImage image;
    private ArrayList<String> tags;
    private Score scoreObject;

    private boolean canTagsDisappear = true;
    private boolean canScoreDisappear = true;

    private boolean answeredCorrectly = false;

    private boolean commentWritten = false;
    private boolean submitted = false;

    private int ID;
    private String studentName;
    private int problemNum;

    public static int canviiCount = 0;

    public Canvas(String canvas) {
        this(
                new QGImage(canvas.substring(1, canvas.length() - 1).split("}\\{")[0].substring(0, canvas.substring(1, canvas.length() - 1).split("}\\{")[0].indexOf(",")))
                        .resize(Constants.scaleHeight, Constants.scaleWidth, true).getRegion(new AnswerField(canvas.substring(1, canvas.length() - 1).split("}\\{")[0].substring(canvas.substring(1, canvas.length() - 1).split("}\\{")[0].indexOf(",") + 1))),
                canvas.substring(1, canvas.length() - 1).split("}\\{")[5].substring(0, canvas.substring(1, canvas.length() - 1).split("}\\{")[5].length() - 1),
                Integer.parseInt(canvas.substring(1, canvas.length() - 1).split("}\\{")[6]),
                Integer.parseInt(canvas.substring(1, canvas.length() - 1).split("}\\{")[10]),
                canvas.substring(1, canvas.length() - 1).split("}\\{")[1].split(","),
                canvas.substring(1, canvas.length() - 1).split("}\\{")[2],
                canvas.substring(1, canvas.length() - 1).split("}\\{")[3],
                Boolean.parseBoolean(canvas.substring(1, canvas.length() - 1).split("}\\{")[9]),
                Boolean.parseBoolean(canvas.substring(1, canvas.length() - 1).split("}\\{")[4])
        );
    }

    //order: img, menuItems, tags, score, concept understood, name, problemNum, canTagsDisappear, canScoreDisappear, commentWritten, id
    private Canvas(QGImage image, String name, int problemNum, int ID, String[] menuItems, String tags, String score, boolean commentWritten, boolean conceptUnderstood) {

        this(image, name, problemNum);
        this.ID = ID;

        menu.removeActionListener(menuListener);
        for (String item : menuItems) {
            menu.addItem(item);
        }
        menu.addActionListener(menuListener);

        customTags.setText(tags);
        if (!tags.equals("Tags")) canTagsDisappear = false;

        this.score.setText(score);
        if (!score.equals("Score/Total")) canScoreDisappear = false;

        this.commentWritten = commentWritten;

        this.answeredCorrectly = conceptUnderstood;
        if (answeredCorrectly) {
            this.conceptUnderstood.setSelected(true);
        }
    }

    public Canvas(QGImage image, String name, int problemNum) {

        this.ID = canviiCount * 2;

        this.image = image;
        this.tags = new ArrayList<>();
        this.studentName = name;
        this.problemNum = problemNum;

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

        menuListener = e -> {
            if (customTags.getText().equals("Tags")) customTags.setText("");
            if (customTags.getText().equals("")) customTags.setText(menu.getSelectedItem().toString());
            else customTags.setText(customTags.getText() + ", " + menu.getSelectedItem().toString());
            menu.removeItem(menu.getSelectedItem());
        };
        menu.addActionListener(menuListener);

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

    public String getStudentName() {
        return studentName;
    }

    public int getProblemNum() {
        return problemNum;
    }

    public int getID() {
        return ID;
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
//        UserInteractiveGrader.students.get(name).addScore(problemNum, scoreObject);

        //updating student-specific tag info in uig
        for (String tag : tags) {
            UserInteractiveGrader.tags.get(name).get(problemNum).add(tag);
        }

        UserInteractiveGrader.updateCanvi(); //adds tags to other canvii
        UserInteractiveGrader.updateScoresForProblem(problemNum, score.getText().split("/")[1]); //changes score default text for other canvii

        //updates concept understood correctly
        if (answeredCorrectly) {
            UserInteractiveGrader.conceptUnderstood.get(name).put(problemNum, 1);
        } else {
            UserInteractiveGrader.conceptUnderstood.get(name).put(problemNum, 0);
        }

        //in case no comments were written, adds an empty string to that student's index of comments
        if (!commentWritten) {
            UserInteractiveGrader.comments.get(name).put(problemNum, "");
        }

        submitted = true;
        removeFromRecords(this.ID);
        UserInteractiveGrader.hardBurnCanvasData();
    }


    //order: img, menuItems, tags, score, concept understood, name, problemNum, canTagsDisappear, canScoreDisappear, commentWritten
    //filepath: absolute path
    public String toString(String filepath, AnswerField field) {
        String img = "{" + filepath + "," + field.compressedString() + "}";
        StringBuilder menuItems = new StringBuilder("{");
        for (int i = 0; i < menu.getItemCount(); i++) {
            menuItems.append(menu.getItemAt(i));
            if (i < menu.getItemCount() - 1) menuItems.append(",");
        }
        menuItems.append("}");
        String tags = "{" + customTags.getText() + "}";
        String score = "{" + this.score.getText() + "}";
        String conceptUnderstood = "{" + this.answeredCorrectly + "}";
        String name = "{" + this.frame.getName().split(" ")[0] + "}";
//        System.out.println(frame.getName());
        String problemNum = "{" + frame.getName().split(" ")[1] + "}";
        String canTagsDisappear = "{" + this.canTagsDisappear + "}";
        String canScoreDisappear = "{" + this.canScoreDisappear + "}";
        String commentWritten = "{" + this.commentWritten + "}";
        String id = "{" + this.ID + "}";
        return img + menuItems + tags + score + conceptUnderstood + name + problemNum + canTagsDisappear + canScoreDisappear + commentWritten + id;
    }

    public static void removeFromRecords(int id) {
        ArrayList<String> canvii = Constants.getLines(new File("Progress" + Constants.separator + "Canvii.txt"));
        for (int i = 0; i < canvii.size(); i++) {
            String canvas = canvii.get(i);
            if (Integer.parseInt(canvas.substring(1, canvas.length() - 1).split("}\\{")[10]) == id) {
                canvii.remove(i);
                break;
            }
        }
        Burner.write(canvii, "Progress" + Constants.separator + "Canvii.txt");
    }
}
