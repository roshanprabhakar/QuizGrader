import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class UserInteractiveGrader {

    //Utilities
    public static WindowManager manager;
    public static boolean closed;

    public static Report report;
    public static IndividualVisualizer iv;

    //page length of the assessment
    public static int numPages;

    //loads data from raw pdf document
    private DataLoader dataLoader;

    //project variables
    public static int submittedProblems = 0; //project level variable
    public static int logCount = 0;

    public int numOfStudents; //student directory count
    public static HashMap<String, ArrayList<AnswerField>> answerFields;
    public static int numOfProblems;

    public static ArrayList<String> menuLabels = new ArrayList<>();
    public static ArrayList<Canvas> canvii = new ArrayList<>();

    //most important structures for the program
    public static HashMap<String, HashMap<Integer, ArrayList<String>>> tags = new HashMap<>(); //student --> #:tags //send
    public static HashMap<String, HashMap<Integer, Score>> scores = new HashMap<>(); //student --> #:score //send
    public static HashMap<Integer, ArrayList<Canvas>> numberToCanvas = new HashMap<>(); //map : problem# --> ansField
    public static HashMap<String, Student> students = new HashMap<>();
    public static HashMap<String, HashMap<Integer, Integer>> conceptUnderstood = new HashMap<>(); //send
    public static HashMap<String, Score> totals = new HashMap<>(); //send
    public static HashMap<String, Character> grades = new HashMap<>(); //send
    public static HashMap<String, Double> percentages = new HashMap<>(); //send
    public static HashMap<String, HashMap<Integer, String>> comments = new HashMap<>(); //send

    public void run() throws InterruptedException {

        // ---------- SETUP ----------
        if (!fileExists("src" + Constants.separator + "ScannedImageSources")) {
            //get all student names
            new NamesLister().prompt();

            //get number of pages
            numPages = Integer.parseInt(new InputPane("How many pages in this assessment?").centered().getInput());

            //parsing pdf data, storing it in organized directories
            dataLoader = new DataLoader(numPages);
            try {
                dataLoader.loadData("QGTestData.pdf");
                dataLoader.sortData();
            } catch (NullPointerException e) {
                Constants.record("Null Pointer when loading and sorting data");
            }
        } else {
            numPages = Objects.requireNonNull(new File(Constants.blankTest).listFiles()).length;
            dataLoader = new DataLoader(numPages); //in case list of students participating is ever required
        }
        numOfStudents = Objects.requireNonNull(new File(Constants.studentResponses).listFiles()).length; //depends on dataLoader.sortData()

        //maps page names to a list of answer fields
        answerFields = loadAllAnswerFields(); //HashMap mapping page name to list of answer fields on that page

        //depends on updates to numOfProblems in order to instantiate maps with correct sizes
        this.setup();
        // ----------- END SETUP ------------

        //create the list<canvas> for the window manager
        for (int i = 1; i <= numOfProblems; i++) {

            String page = getPageForNum(i); //page problem i is located in
            AnswerField ans = getAnswerFieldForNum(i); //the corresponding answerfield on the appropriate page for problem i

            //execute data collector on all students
            for (File student : Objects.requireNonNull(new File(Constants.studentResponses).listFiles())) { //student will be the name of the student

                //object representing student's data
                Student thisStudent = new Student(new HashMap<>(), new HashMap<>(), student.getName());
                students.put(student.getName(), thisStudent);

                //the interface backend for data collection
                QGImage image = new QGImage(student.getAbsolutePath() + Constants.separator + page);
                image.resize(Constants.scaleHeight, Constants.scaleWidth);
                Canvas canvas = new Canvas(image.getRegion(ans), student.getName(), ans.getProblemNum());

                //needed for statistical analysis
                canvii.add(canvas);
                numberToCanvas.get(ans.getProblemNum()).add(canvas); //grouping data by problem number instead of the conventional by student
            }
        }

        //interface frontend for data collection
        manager = new WindowManager(new ArrayList<>(canvii));
        manager.initialize();
        manager.displayAllPositioned();

        //wait till data collection is done
        while ((numOfProblems) * numOfStudents > submittedProblems) System.out.print(""); //keep this print


        report = new Report(numOfProblems);
        report.centerAt(new Point((int) (Constants.screenWidth / 2), (int) (Constants.screenHeight / 2)));
        report.setVisible(true);

        iv = new IndividualVisualizer(tags, scores, numOfProblems);
        iv.setLocation((int) report.getLocation().getX() + report.getWidth(), (int) report.getLocation().getY());
        iv.setVisible(true);

        Burner.writeEmailExportable();
        Burner.writeSoftwareExportable();

        while (!closed) {
            System.out.print("");
        }

        System.exit(0);
    }

    private boolean fileExists(String filepath) {
        File file = new File(filepath);
        return file.exists();
    }

    private HashMap<String, ArrayList<AnswerField>> loadAllAnswerFields() throws InterruptedException {

        HashMap<String, ArrayList<AnswerField>> answers = new HashMap<>();
        File[] blankTest = new File(Constants.blankTest).listFiles();

        int num = 0;
        assert blankTest != null;
        for (File page : blankTest) {

            answers.put(page.getName(), new ArrayList<>());

            QGImage pageImage = new QGImage(Constants.blankTest + page.getName());
            pageImage.resize(Constants.scaleHeight, Constants.scaleWidth);
            pageImage.display(true);

            InputPane pane = new InputPane("How many answerfields on this page?");
            pane.centerAt(new Point((int) (((double) 3 / 5) * Constants.screenWidth), (int) ((double) 1 / 2 * Constants.screenHeight)));
            int numOfAnswerFields = Integer.parseInt(pane.getInput());

//            int numOfAnswerFields = Integer.parseInt(new InputPane("How many answerfields on this page?").centered().getInput());

            for (int i = 0; i < numOfAnswerFields; i++) {
                num++;
                answers.get(page.getName()).add(recordAnswerField(pageImage, num));
            }

            pageImage.close();
        }

        numOfProblems = num;
        return answers;
    }

    /**
     * The problem is that the answer field is being recorded with locations taken with respect to the background, not to the bufferedImage
     */
    public static AnswerField recordAnswerField(QGImage page, int problemNum) throws InterruptedException {

        //AnswerField where we append mouse locations
        AnswerField field = new AnswerField();

        //makes sure mouse is clicked
        while (!page.mouseIsPressed()) {
            Thread.sleep(10);
            continue;
        }
//        Constants.record("clicked at: " + Constants.getLocationOfMouse());

        field.setTopX((int) Constants.getLocationOfMouse().getX() - page.getFrame().getX());
        field.setTopY((int) Constants.getLocationOfMouse().getY() - page.getFrame().getY());

//        Constants.record("recorded: " + field);

        //allows time for the user to drag the mouse
        while (page.mouseIsPressed()) {
            Thread.sleep(10);
            continue;
        }
//        Constants.record("clicked at: " + Constants.getLocationOfMouse());

        field.setBottomX((int) Constants.getLocationOfMouse().getX() - page.getFrame().getX());
        field.setBottomY((int) Constants.getLocationOfMouse().getY() - page.getFrame().getY());

//        Constants.record("recorded: " + field);

        field.setHeight(Math.abs(field.getTopY() - field.getBottomY()));
        field.setWidth(Math.abs(field.getTopX() - field.getBottomX()));

//        Constants.record("recorded: " + field);

        field.setProblemNum(problemNum);

        page.drawRectangleAt(field.getTopX(), field.getTopY(), field.getBottomX(), field.getBottomY());
        Thread.sleep(100);
        page.closeRectangleView();

        return field;
    }

    private String getPageForNum(int num) {
        for (String page : answerFields.keySet()) {
            for (AnswerField ans : answerFields.get(page)) {
                if (ans.getProblemNum() == num) {
                    return page;
                }
            }
        }
        return null;
    }

    private AnswerField getAnswerFieldForNum(int num) {
        for (String page : answerFields.keySet()) {
            for (AnswerField ans : answerFields.get(page)) {
                if (ans.getProblemNum() == num) {
                    return ans;
                }
            }
        }
        return null;
    }

    /**
     * Populates all essential data structures with empty containers to be filled
     */
    private void setup() {
        for (File student : new File(Constants.studentResponses).listFiles()) {
            tags.put(student.getName(), new HashMap<>());
            for (int i = 0; i < numOfProblems; i++) {
                tags.get(student.getName()).put(i + 1, new ArrayList<>());
            }
            scores.put(student.getName(), new HashMap<>());
            comments.put(student.getName(), new HashMap<>());
            conceptUnderstood.put(student.getName(), new HashMap<>());
        }
        for (int i = 1; i <= numOfProblems; i++) {
            numberToCanvas.put(i, new ArrayList<>());
        }
    }

    public static void updateCanvi() {
        for (Canvas canvas : canvii) {
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
