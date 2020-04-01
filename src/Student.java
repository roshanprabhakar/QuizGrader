import java.util.ArrayList;
import java.util.HashMap;

public class Student {

    private HashMap<Integer, ArrayList<String>> tags;
    private HashMap<Integer, Score> scores;
    private Score total;

    private String name;

    public Student(HashMap<Integer, ArrayList<String>> tags, HashMap<Integer, Score> scores, String name) {
        this.tags = tags;
        this.scores = scores;
        this.total = new Score();
        this.name = name;
    }

    public HashMap<Integer, ArrayList<String>> getTags() {
        return tags;
    }

    public void setTags(HashMap<Integer, ArrayList<String>> tags) {
        this.tags = tags;
    }

    public HashMap<Integer, Score> getScores() {
        return scores;
    }

    public void setScores(HashMap<Integer, Score> scores) {
        this.scores = scores;
    }

    public Score getTotal() {
        return total;
    }

    public void setTotal(Score total) {
        this.total = total;
    }

    public void addTag(int problemNum, String tag) {
        if (tags.get(problemNum) == null) {
            tags.put(problemNum, new ArrayList<>());
        }
        tags.get(problemNum).add(tag);
    }

    public void addScore(int problemNum, Score score) {
        if (scores.get(problemNum) == null) scores.put(problemNum, score);
        else scores.replace(problemNum, score);
        total.setEarned(total.getEarned() + score.getEarned());
        total.setPossible(total.getPossible() + score.getPossible());
        total.recalculatePercent();
    }

    public String toString() {
        return this.name + " " + total.toString();
    }
}
