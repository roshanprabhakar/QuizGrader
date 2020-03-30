import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static WindowManager manager;

    public static void main(String[] args) throws InterruptedException {
        runUserInteractiveGradingSystem();

//        ArrayList<Window> windows = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            windows.add(new WindowManagerTestWindow(i, 300 + (int)(Math.random() * 300), 400 + (int)(Math.random() * 400)));
//        }
//
//        manager = new WindowManager(windows);
//        manager.initialize();
//        manager.displayAllPositioned();
//        Thread.sleep(100000);
    }

    /**
     * Algorithm:
     * 1. display blank test
     * 2. User goes through test and drags boxes on the parts where there will be answers
     * 3. for every question:
     *      loop through all the responses, display them side by side
     *      each should have two input boxes under them: 1 for displaying sentence comments,
     *      the other for typing a relative score for that problem
     * 4. record all data to associated students
     * 5. generate a class summary with all comments and general scores (which questions did people get wrong the most)
     * 6. generate student summary, with comments and scores
     * 7. send these scores through email
     */

    public static void runUserInteractiveGradingSystem() throws InterruptedException {
        UserInteractiveGrader userInteractiveGrader = new UserInteractiveGrader();
        userInteractiveGrader.run();
    }
}
