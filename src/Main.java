import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static WindowManager manager;

    public static void main(String[] args) throws InterruptedException, IOException {
//        runUserInteractiveGradingSystem();

        String compressedCanvas = "{/Users/roshanprabhakar/IdeaProjects/QuizGrader/src/ScannedImageSources/StudentResponses/Maya/page1.png,14,249,716,484,1}{tag1,tag2,tag3}{THIS IS COMMENTS}{Score/Total}{false}{Maya:}{1}{true}{true}{false}{0}";
        Canvas canvas = new Canvas(compressedCanvas);
        canvas.setVisible(true);
    }

    /**
     * Algorithm:
     * 1. display blank test
     * 2. User goes through test and drags boxes on the parts where there will be answers
     * 3. for every question:
     * loop through all the responses, display them side by side
     * each should have two input boxes under them: 1 for displaying sentence comments,
     * the other for typing a relative score for that problem
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
