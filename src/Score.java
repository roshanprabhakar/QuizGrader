public class Score {

    private double earned;
    private double possible;
    private double percent;

    public Score(double earned, double possible) {
        this.earned = earned;
        this.possible = possible;
        calculatePercent();
    }

    public Score(String score) {
        int slashIndex = score.indexOf("/");
        earned = Double.parseDouble(score.substring(0, slashIndex));
        possible = Double.parseDouble(score.substring(slashIndex + 1));
        calculatePercent();
    }

    public Score() {}

    public Score(double percent) {this.percent = percent;}

    private void calculatePercent() {
        this.percent = earned / possible * 100;
    }


    public double getEarned() {
        return earned;
    }

    public void setEarned(double earned) {
        this.earned = earned;
        try {
            calculatePercent();
        } catch (NullPointerException e) {}
    }

    public double getPossible() {
        return possible;
    }

    public void setPossible(double possible) {
        this.possible = possible;
        try {
            calculatePercent();
        } catch (NullPointerException e) {}
    }

    public double getPercent() {
        recalculatePercent();
        return percent;
    }

    public double getParsedPercent() {
        return Math.round(getPercent() * 10.0) / (double) 10;
    }

    public void recalculatePercent() {
        calculatePercent();
    }

    @Override
    public String toString() {
        return earned + "/" + possible;
    }
}
