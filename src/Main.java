import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        runUserInteractiveGradingSystem();
//        DataLoader loader = new DataLoader(2);
//        loader.loadData("QGTestData.pdf");
//        loader.sortData();
    }

    /**
     * Algorithm:
     * 1. display blank test //done
     * 2. User goes through test and drags boxes on the parts where there will be answers //done
     * 3. for every question:
     *      loop through all the responses, display them side by side
     *      each should have two input boxes under them: 1 for displaying sentence comments,
     *      the other for typing a relative score for that problem
     * 4. record all data to associated students
     * 5. generate a class summary with all comments and general scores (which questions did people get wrong the most
     * 6. generate student summary, with comments and scores
     * 7. send these scores through email
     */

    public static void runUserInteractiveGradingSystem() throws InterruptedException {
        try {
            UserInteractiveGrader userInteractiveGrader = new UserInteractiveGrader();
            userInteractiveGrader.run();
        } catch (IOException e) {
            UserInteractiveGrader.logger.log("Unhandled IOException while initializing the grader!");
        }
    }
}
