public class Score {

    private double earned;
    private double possible;
    private double percent;

    public Score(double earned, double possible) {
        this.earned = earned;
        this.possible = possible;
        calculatePercent();
    }

    private void calculatePercent() {
        this.percent = earned / possible * 100;
    }

    public double getEarned() {
        return earned;
    }

    public void setEarned(double earned) {
        this.earned = earned;
    }

    public double getPossible() {
        return possible;
    }

    public void setPossible(double possible) {
        this.possible = possible;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}
