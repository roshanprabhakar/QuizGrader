import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class UserInteractiveGrader {

    public static Logger logger = new Logger();

    public static final int numPages = Integer.parseInt(new InputPane("How many pages in this assessment?").centered().getInput());

//    public static final int numPages = parsePaneInput("How many pages in this assessment?", "smallLogo.png");

    public DataLoader dataLoader = new DataLoader(numPages);

    public static int submittedProblems = 0;
    public static int logCount = 0;
    private int numOfStudents;

    private HashMap<String, ArrayList<AnswerField>> ANSWER_FIELDS;
    public static int numOfProblems;

    public static ArrayList<String> menuLabels = new ArrayList<>();
    public static ArrayList<CanvasContainer> canvi = new ArrayList<>();

    //most important data structures for the program
    public static HashMap<String, HashMap<Integer, ArrayList<String>>> tags = new HashMap<>();
    public static HashMap<String, HashMap<Integer, Score>> scores = new HashMap<>();
    public static HashMap<Integer, ArrayList<CanvasContainer>> numberToCanvas = new HashMap<>(); //map : problem# --> ansField
    public static HashMap<String, Student> students = new HashMap<>();
    public static HashMap<String, HashMap<Integer, Integer>> conceptUnderstood = new HashMap<>(); //need to change the name
    public static HashMap<String, Score> totals = new HashMap<>();
    public static HashMap<String, Character> grades = new HashMap<>();
    public static HashMap<String, Double> percentages = new HashMap<>();
    public static HashMap<String, HashMap<Integer, String>> comments = new HashMap<>();

    public void run() throws InterruptedException, IOException {

        try {
            dataLoader.loadData("src" + File.separator + "RES");
        } catch (NullPointerException exception) {
        }

        numOfStudents = new File(Constants.StudentResponsePath).listFiles().length;

        ANSWER_FIELDS = loadAllAnswerFields(); //HashMap mapping page name to list of answer fields on that page

        this.setup();

        //positioning stuff
        int newX = 0;
        int newY = 0;

        boolean newLine = false;

        for (int i = 1; i <= numOfProblems; i++) {

            String page = getPageForNum(i);
            AnswerField ans = getAnswerFieldForNum(i);

            for (File student : new File(Constants.StudentResponsePath).listFiles()) { //student will be the name of the student

                conceptUnderstood.put(student.getName(), new HashMap<>());
                logger.log(conceptUnderstood);

                Student thisStudent = new Student(new HashMap<>(), new HashMap<>(), student.getName());

                QGImage image = new QGImage(student.getAbsolutePath() + Constants.separator + page);
                image.resize(Constants.scaleHeight, Constants.scaleWidth);
                CanvasContainer container = new CanvasContainer(student.getName(), image.getRegion(ans), ans.getProblemNum());

                //needed for statistical analysis
                canvi.add(container);
                students.put(student.getName(), thisStudent);
                numberToCanvas.get(ans.getProblemNum()).add(container);

                //position stuff
                if (newX + container.getWidth() + 20 > Constants.screenWidth) {
                    newX = 0;
                    newLine = true;
                }

                if (newLine) {
                    newY += container.getHeight() + 20;
                    newLine = false;
                }

                container.setLocation(newX, newY);
                newX += container.getWidth() + 20;

                container.display();
            }
        }

        while ((numOfProblems) * numOfStudents > submittedProblems) System.out.print(""); //keep this print

        new Report(scores, tags, numOfProblems).display();
        new IndividualVisualizer(tags, scores, numOfProblems).display();

        logCount++;

        while (logCount != 2) {
            System.out.print("");
        }

        logger.close();

        System.exit(0);
    }

    private HashMap<String, ArrayList<AnswerField>> loadAllAnswerFields() throws InterruptedException, IOException {

        HashMap<String, ArrayList<AnswerField>> answers = new HashMap<>();
        File[] blankTest = new File(Constants.imagePath + "BlankTest" + Constants.separator).listFiles();

        int num = 0;
        for (File page : blankTest) {

            answers.put(page.getName(), new ArrayList<>());

            QGImage pageImage = new QGImage(Constants.imagePath + "BlankTest" + Constants.separator + page.getName());
            pageImage.resize(Constants.scaleHeight, Constants.scaleWidth);
            pageImage.display();

            int numOfAnswerFields = Integer.parseInt(new InputPane("How many answerfields on this page?").centered().getInput());

//            int numOfAnswerFields = parsePaneInput("How many Answer Fields on this page", "smallLogo.png");

            for (int i = 0; i < numOfAnswerFields; i++) {
                num++;
                answers.get(page.getName()).add(recordAnswerField(pageImage, num));
            }

            pageImage.close();
        }

        numOfProblems = num;
        return answers;
    }

    private AnswerField recordAnswerField(QGImage page, int problemNum) throws InterruptedException {

        //AnswerField where we append mouse locations
        AnswerField field = new AnswerField();

        //makes sure mouse is clicked
        while (!page.mouseIsPressed()) {
            Thread.sleep(10);
            continue;
        }

        field.setTopX(Constants.getLocationOfMouse()[0]);
        field.setTopY(Constants.getLocationOfMouse()[1]);

        //allows time for the user to drag the mouse
        while (page.mouseIsPressed()) {
            Thread.sleep(10);
            continue;
        }

        field.setBottomX(Constants.getLocationOfMouse()[0]);
        field.setBottomY(Constants.getLocationOfMouse()[1]);

        field.setHeight(Math.abs(field.getTopY() - field.getBottomY()));
        field.setWidth(Math.abs(field.getTopX() - field.getBottomX()));

        field.setProblemNum(problemNum);

        page.drawRectangleAt(field.getTopX(), field.getTopY(), field.getBottomX(), field.getBottomY());
        Thread.sleep(100);
        page.closeRectangleView();

        return field;
    }

    private String getPageForNum(int num) {
        for (String page : ANSWER_FIELDS.keySet()) {
            for (AnswerField ans : ANSWER_FIELDS.get(page)) {
                if (ans.getProblemNum() == num) {
                    return page;
                }
            }
        }
        return null;
    }

    private AnswerField getAnswerFieldForNum(int num) {
        for (String page : ANSWER_FIELDS.keySet()) {
            for (AnswerField ans : ANSWER_FIELDS.get(page)) {
                if (ans.getProblemNum() == num) {
                    return ans;
                }
            }
        }
        return null;
    }

    private static Integer parsePaneInput(String inputDialog, String filepath) {

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 0, 0));

//        new JOptionPane().showInputDialog(panel, "QUIZ GRADER", JOptionPane.INFORMATION_MESSAGE, new QGImage(filepath).resize(100,120, true).getIcon(), null, null);


//        JOptionPane pane = new JOptionPane();
//        pane.setIcon(new QGImage(filepath).resize(100,120, true).getIcon());
//        pane.setBackground(new Color(0,0,0));
//        return Integer.parseInt(pane.showInputDialog(inputDialog));


        return Integer.parseInt((String) JOptionPane.showInputDialog(
                null,
                inputDialog,
                "QUIZ GRADER",
                JOptionPane.INFORMATION_MESSAGE,
                new QGImage(filepath).resize(100, 120, true).getIcon(),
                null,
                ""));
    }

    private void setup() {
        for (File student : new File(Constants.StudentResponsePath).listFiles()) {
            tags.put(student.getName(), new HashMap<>());
            for (int i = 0; i < numOfProblems; i++) {
                tags.get(student.getName()).put(i + 1, new ArrayList<>());
            }
            scores.put(student.getName(), new HashMap<>());
            comments.put(student.getName(), new HashMap<>());
        }
        for (int i = 1; i <= numOfProblems; i++) {
            numberToCanvas.put(i, new ArrayList<>());
        }
    }

    public static void updateCanvi() {
        for (CanvasContainer container : canvi) {
            for (String label : menuLabels) {
                if (!container.contains(label) && !label.contains("<o>")) {
                    container.addLabel(label);
                }
            }
        }
    }

    public static void updateScoresForProblem(int problemNum, String total) {
        for (CanvasContainer container : numberToCanvas.get(problemNum)) {
            container.setScoreField(total);
        }
    }
}
