import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ProgressManager {

    private ArrayList<String> students;

    public ProgressManager(ArrayList<String> students) {
        this.students = students;
    }

    //TODO in forbidden characters add "[]"

    //Parses tags structure from string
    public HashMap<String, HashMap<Integer, ArrayList<String>>> parseTags(String comments) {

        HashMap<String, HashMap<Integer, ArrayList<String>>> tags = new HashMap<>();
        for (String student : students) {

            int startOfInnerMap = comments.indexOf(student) + student.length() + 1;
            int endOfInnerMap = indexOf('}', comments, 1, startOfInnerMap);

            String studentCommentMap = comments.substring(startOfInnerMap + 1, endOfInnerMap);

            ArrayList<Integer> openBrackets = indexOf('[', studentCommentMap);
            ArrayList<Integer> closedBrackets = indexOf(']', studentCommentMap);
            assert openBrackets.size() == closedBrackets.size();

            //index + 1 corresponds with the string of tags in that location
            ArrayList<String> problemTags = new ArrayList<>();
            for (int i = 0; i < closedBrackets.size(); i++) {
                problemTags.add(studentCommentMap.substring(openBrackets.get(i) + 1, closedBrackets.get(i)));
            }

            HashMap<Integer, ArrayList<String>> innerMap = new HashMap<>();
            for (int i = 0; i < problemTags.size(); i++) {
                innerMap.put(i + 1, new ArrayList<>(Arrays.asList(problemTags.get(i).split(", "))));
            }

            tags.put(student, innerMap);
        }

        return tags;
    }

    public HashMap<String, HashMap<Integer, Score>> parseScores(String scores) {

        HashMap<String, HashMap<Integer, Score>> scoreMap = new HashMap<>();
        for (String student : students) {

            int startOfInnerMap = scores.indexOf(student) + student.length() + 1;
            int endOfInnerMap = indexOf('}', scores, 1, startOfInnerMap);

            String scoreString = scores.substring(startOfInnerMap + 1, endOfInnerMap);

            HashMap<Integer, Score> studentScoreMap = new HashMap<>();
            if (scoreString.length() != 0) {
                String[] scoreList = scores.substring(startOfInnerMap + 1, endOfInnerMap).split(", ");
                for (String score : scoreList) {
                    String[] parsedScore = score.split("=");
                    int number = Integer.parseInt(parsedScore[0]);
                    Score scoreObject = new Score(parsedScore[1]);
                    studentScoreMap.put(number, scoreObject);
                }
            }
            scoreMap.put(student, studentScoreMap);
        }
        return scoreMap;
    }

    public HashMap<String, HashMap<Integer, Integer>> parseConceptUnderstood(String conceptUnderstood) {

        HashMap<String, HashMap<Integer, Integer>> conceptUnderstoodMap = new HashMap<>();
        for (String student : students) {
            int startOfInnerMap = conceptUnderstood.indexOf(student) + student.length() + 1;
            int endOfInnerMap = indexOf('}', conceptUnderstood, 1, startOfInnerMap);

            String conceptCheckString = conceptUnderstood.substring(startOfInnerMap + 1, endOfInnerMap);

            HashMap<Integer, Integer> studentMap = new HashMap<>();
            if (conceptCheckString.length() != 0) {
                String[] list = conceptUnderstood.substring(startOfInnerMap + 1, endOfInnerMap).split(", ");
                for (String map : list) {
                    String[] conceptMap = map.split("=");
                    studentMap.put(Integer.parseInt(conceptMap[0]), Integer.parseInt(conceptMap[1]));
                }
            }
            conceptUnderstoodMap.put(student, studentMap);
        }

        return conceptUnderstoodMap;
    }

    public HashMap<String, HashMap<Integer, String>> parseComments(String comments) {

        HashMap<String, HashMap<Integer, String>> commentMap = new HashMap<>();
        for (String student : students) {
            int startOfInnerMap = comments.indexOf(student) + student.length() + 1;
            int endOfInnerMap = indexOf('}', comments, 1, startOfInnerMap);

            String commentStringCheck = comments.substring(startOfInnerMap + 1, endOfInnerMap);

            HashMap<Integer, String> studentCommentMap = new HashMap<>();
            if (commentStringCheck.length() != 0) {
                String[] list = comments.substring(startOfInnerMap + 1, endOfInnerMap).split(", ");
                for (String item : list) {
                    String[] problem = item.split("=");
                    if (problem.length == 1) {
                        problem = new String[]{problem[0], ""};
                    }
                    studentCommentMap.put(Integer.parseInt(problem[0]), problem[1]);
                }
            }
            commentMap.put(student, studentCommentMap);
        }
        return commentMap;
    }

    private int indexOf(char chr, String str, int count, int start) {
        int check = 0;
        for (int i = start; i < str.length(); i++) {
            if (str.charAt(i) == chr) check++;
            if (check == count) return i;
        }
        return -1;
    }

    private ArrayList<Integer> indexOf(char chr, String str) {
        ArrayList<Integer> out = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == chr) out.add(i);
        }
        return out;
    }

    public static void main(String[] args) {

        ArrayList<String> students = new ArrayList<>(Arrays.asList("Roshan", "Shrey"));
        String tags = "{Roshan={1=[tagstagstags]}, Shrey={1=[]}}";
        String scores = "{Roshan={1=5.0/10.0}, Shrey={}}";
        String conceptUnderstood = "{Roshan={1=1}, Shrey={}}";
        String comments = "{Roshan={1=some random comment}, Shrey={}}";

        //all parsing code works
        ProgressManager manager = new ProgressManager(students);
//        System.out.println(manager.parseTags(tags));
//        System.out.println(manager.parseScores(scores));
//        System.out.println(manager.parseConceptUnderstood(conceptUnderstood));
//        System.out.println(manager.parseComments(comments));
    }
}
