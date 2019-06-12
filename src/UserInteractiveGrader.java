import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class UserInteractiveGrader {

    public static final int numPages = Integer.parseInt(new InputPane("How many pages in this assessment?").centered().getInput());

    public static Logger logger = new Logger();
    public DataLoader dataLoader = new DataLoader(numPages);

    public static int submittedProblems = 0;
    public static int logCount = 0;

    public int numOfStudents;
    public static HashMap<String, ArrayList<AnswerField>> ANSWER_FIELDS;
    public static int numOfProblems;

    public static ArrayList<String> menuLabels = new ArrayList<>();
    public static ArrayList<Canvas> canvi = new ArrayList<>();

    //most important structures for the program
    public static HashMap<String, HashMap<Integer, ArrayList<String>>> tags = new HashMap<>();
    public static HashMap<String, HashMap<Integer, Score>> scores = new HashMap<>();
    public static HashMap<Integer, ArrayList<Canvas>> numberToCanvas = new HashMap<>(); //map : problem# --> ansField
    public static HashMap<String, Student> students = new HashMap<>();
    public static HashMap<String, HashMap<Integer, Integer>> conceptUnderstood = new HashMap<>(); //need to change the name
    public static HashMap<String, Score> totals = new HashMap<>();
    public static HashMap<String, Character> grades = new HashMap<>();
    public static HashMap<String, Double> percentages = new HashMap<>();
    public static HashMap<String, HashMap<Integer, String>> comments = new HashMap<>();

    public void run() throws InterruptedException, IOException {

        new NamesLister().prompt();

        if (!fileExists("src/ScannedImageSources")) {
            try {
                dataLoader.loadData("QGTestData.pdf");
                dataLoader.sortData();
            } catch (NullPointerException e){
                System.out.println("Null Pointer when loading and sorting data");
            }
        }

        dataLoader.sortData();

        numOfStudents = new File(Constants.studentResponses).listFiles().length;
        ANSWER_FIELDS = loadAllAnswerFields(); //HashMap mapping page name to list of answer fields on that page

        this.setup();

        //positioning stuff
        int newX = 0;
        int newY = 0;

        boolean newLine = false;

        for (int i = 1; i <= numOfProblems; i++) {

            String page = getPageForNum(i);
            AnswerField ans = getAnswerFieldForNum(i);

            for (File student : new File(Constants.studentResponses).listFiles()) { //student will be the name of the student

                conceptUnderstood.put(student.getName(), new HashMap<>());
                logger.log(conceptUnderstood);

                Student thisStudent = new Student(new HashMap<>(), new HashMap<>(), student.getName());

                QGImage image = new QGImage(student.getAbsolutePath() + Constants.separator + page);
                image.resize(Constants.scaleHeight, Constants.scaleWidth);
                Canvas canvas = new Canvas(image.getRegion(ans), student.getName(), ans.getProblemNum());

                //needed for statistical analysis
                canvi.add(canvas);
                students.put(student.getName(), thisStudent);
                numberToCanvas.get(ans.getProblemNum()).add(canvas);
                
                //position stuff
                if (newX + canvas.getWidth() + 20 > Constants.screenWidth) {
                    newX = 0;
                    newLine = true;
                }

                if (newLine) {
                    newY += canvas.getHeight() + 20;
                    newLine = false;
                }

                canvas.setLocation(newX, newY);
                newX += canvas.getWidth() + 20;

                canvas.display();
            }
        }

        while ((numOfProblems) * numOfStudents > submittedProblems) System.out.print(""); //keep this print

        new Report(numOfProblems).display();
        new IndividualVisualizer(tags, scores, numOfProblems).display();

        logCount++;

        while (logCount != 2) {
            System.out.print("");
        }

        logger.close();

        //clean up data collection
        Cleaner.eraseTrailingCommas();

        System.exit(0);
    }

    private boolean fileExists(String filepath) {
        File file = new File(filepath);
        return file.exists();
    }

    private HashMap<String, ArrayList<AnswerField>> loadAllAnswerFields() throws InterruptedException, IOException {

        HashMap<String, ArrayList<AnswerField>> answers = new HashMap<>();
        File[] blankTest = new File(Constants.blankTest).listFiles();

        int num = 0;
        for (File page : blankTest) {

            answers.put(page.getName(), new ArrayList<>());

            QGImage pageImage = new QGImage(Constants.blankTest + page.getName());
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

    private void setup() {
        for (File student : new File(Constants.studentResponses).listFiles()) {
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
        for (Canvas canvas : canvi) {
            for (String label : menuLabels) {
                if (!canvas.contains(label) && !label.contains("<o>")) {
                    canvas.addLabel(label);
                }
            }
        }
    }

    public static void updateScoresForProblem(int problemNum, String total) {
        for (Canvas container : numberToCanvas.get(problemNum)) {
            container.setScoreField(total);
        }
    }
}
