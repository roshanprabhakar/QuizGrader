import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
    public static HashMap<Integer, ArrayList<Canvas>> numberToCanvas = new HashMap<>(); //map : problem# --> ansField (ONLY ESSENTIAL IF INIT SESSION CONTINUES PAST COMPLETE CANVAS EXECUTION)
    public static HashMap<String, Student> students = new HashMap<>();
    public static HashMap<String, HashMap<Integer, Integer>> conceptUnderstood = new HashMap<>(); //send
    public static HashMap<String, Score> totals = new HashMap<>(); //send (USAGE STARTS IN REPORT)
    public static HashMap<String, Character> grades = new HashMap<>(); //send (USAGE STARTS IN REPORT)
    public static HashMap<String, Double> percentages = new HashMap<>(); //send (USAGE STARTS IN REPORT)
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

        //reads all datastructures from disk to see if session already in progress
        String[] studentNames = Constants.getStudentNames();
        ArrayList<String> lines = Constants.getLines(new File("Progress" + Constants.separator + "EssentialStructures.txt"));
        if (lines.size() == 4) {
            ProgressManager progressManager = new ProgressManager(new ArrayList<>(Arrays.asList(studentNames)));
            tags = progressManager.parseTags(lines.get(0));
            scores = progressManager.parseScores(lines.get(1));
            conceptUnderstood = progressManager.parseConceptUnderstood(lines.get(2));
            comments = progressManager.parseComments(lines.get(3));
        }
//        System.out.println(tags);
//        System.out.println(scores);
//        System.out.println(conceptUnderstood);
//        System.out.println(comments);
        // ----------- END SETUP -----------

        //creates the list<canvas> for the window manager, burns to disk if grading session not initiated, loads from disk if already initiated
        if (!Constants.containsText(Constants.getLines(new File("Progress" + Constants.separator + "Canvii.txt")))) {
            ArrayList<String> canvasStrings = new ArrayList<>();

            //progress bar on canvii loading
            ProgressPane loadingCanvasProgress = new ProgressPane();
            loadingCanvasProgress.centerAt(new Point((int)(Constants.screenWidth / 2), (int)(Constants.screenHeight / 2)));
            loadingCanvasProgress.display();

            double maxCount = numOfProblems * Objects.requireNonNull(new File(Constants.studentResponses).listFiles()).length;

            for (int i = 1; i <= numOfProblems; i++) {
                loadingCanvasProgress.setMessage("Loading program windows...");

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

                    //recording progress to disk
                    Canvas.canviiCount++; //only needed during implementation to create canvas ids
                    canvasStrings.add(canvas.toString(student.getAbsolutePath() + Constants.separator + page, ans));

                    //needed for statistical analysis
                    canvii.add(canvas);
                    numberToCanvas.get(ans.getProblemNum()).add(canvas); //grouping data by problem number instead of the conventional by student

                    //update user on loading status
                    loadingCanvasProgress.setProgressBarPercentage((int)(Canvas.canviiCount / maxCount * 100));
                    loadingCanvasProgress.setMessage("Loading program windows... " + (int)(Canvas.canviiCount / maxCount * 100) + "%");
                }
            }
            loadingCanvasProgress.close();

            Burner.write(canvasStrings, "Progress" + Constants.separator + "Canvii.txt");
        } else {

            //progress bar on canvii loading
            ProgressPane loadingCanvasProgress = new ProgressPane();
            loadingCanvasProgress.setMessage("Loading program windows...");
            loadingCanvasProgress.centerAt(new Point((int)(Constants.screenWidth / 2), (int)(Constants.screenHeight / 2)));
            loadingCanvasProgress.display();

            //Needs to add student to students, all canvas queued to canvii, map all canvii to numberToCanvas
            ArrayList<String> compressedCanvii = Constants.getLines(new File("Progress" + Constants.separator + "Canvii.txt"));
            for (int i = 0; i < compressedCanvii.size(); i++) {

                Canvas canvas = new Canvas(compressedCanvii.get(i));

                if (students.get(canvas.getStudentName()) == null) {
                    students.put(canvas.getStudentName(), new Student(new HashMap<>(), new HashMap<>(), canvas.getStudentName()));
                }

                canvii.add(canvas);
                numberToCanvas.get(canvas.getProblemNum()).add(canvas);

                //update user on loading status
                loadingCanvasProgress.setProgressBarPercentage((int)(i / (double) compressedCanvii.size() * 100));
                loadingCanvasProgress.setMessage("Loading program windows... " + (int)(i / (double) compressedCanvii.size() * 100) + "%");
            }
            loadingCanvasProgress.close();
        }

        //interface frontend for data collection
        manager = new WindowManager(new ArrayList<>(canvii));
        manager.initialize();
        manager.displayAllPositioned();

        //wait till data collection is done
        while (canvii.size() > submittedProblems) System.out.print("");

//        System.out.println(tags);
//        System.out.println(scores);
//        System.out.println(conceptUnderstood);
//        System.out.println(comments);

        report = new Report(numOfProblems);
        report.frame.pack();
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

    //assumes pdf pages have been parsed and organized
    private HashMap<String, ArrayList<AnswerField>> loadAllAnswerFields() throws InterruptedException {

        ArrayList<String> answerFields = Constants.getLines(new File("Progress/Answerfields.txt"));
        if (Constants.containsText(answerFields)) {
            return parseAnswerFieldsFromDisk();
        }

        HashMap<String, ArrayList<AnswerField>> answers = new HashMap<>();
        File[] blankTest = new File(Constants.blankTest).listFiles();
        ArrayList<String> diskInfo = new ArrayList<>();

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
                AnswerField field = recordAnswerField(pageImage, num);
                answers.get(page.getName()).add(field);
                diskInfo.add(field.compressedString() + "," + page.getName());
            }

            pageImage.close();
        }

        numOfProblems = num;
        Burner.write(diskInfo, "Progress/Answerfields.txt");
        return answers;
    }

    public HashMap<String, ArrayList<AnswerField>> parseAnswerFieldsFromDisk() {
        ArrayList<String> data = Constants.getLines(new File("Progress/Answerfields.txt"));
        HashMap<String, ArrayList<AnswerField>> map = new HashMap<>();
        int num = 0;
        for (String line : data) {
            String[] parsed = line.split(",");
            AnswerField field = new AnswerField(
                    Integer.parseInt(parsed[0]),
                    Integer.parseInt(parsed[1]),
                    Integer.parseInt(parsed[2]),
                    Integer.parseInt(parsed[3]),
                    Integer.parseInt(parsed[4])
            );
            if (map.get(parsed[5]) == null) {
                map.put(parsed[5], new ArrayList<>());
            }
            map.get(parsed[5]).add(field);
            num++;
        }
        numOfProblems = num;
        return map;
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

    public static Canvas getCanvasForID(int id) {
        for (Canvas canvas : canvii) {
            if (canvas.getID() == id) {
                return canvas;
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

    public static void hardBurnCanvasData() {
        ArrayList<String> compressedCanvii = Constants.getLines(new File("Progress" + Constants.separator + "Canvii.txt"));
        ArrayList<String> newCanvii = new ArrayList<>();
        for (String s : compressedCanvii) {

            String[] canvasString = s.substring(1, s.length() - 1).split("}\\{");

            int id = Integer.parseInt(canvasString[10]);
            Canvas canvas = UserInteractiveGrader.getCanvasForID(id);

            String newString = canvas.toString(canvasString[0].split(",")[0], new AnswerField(canvasString[0].substring(canvasString[0].indexOf(",") + 1)));

            newCanvii.add(newString);
        }
        Burner.write(newCanvii, "Progress" + Constants.separator + "Canvii.txt");
    }
}
