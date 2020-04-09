public class Score {

    private double earned;
    private double possible;
    private double percent;

    public Score(double earned, double possible) {
        this.earned = earned;
        this.possible = possible;
        calculatePercent();
    }

    public Score() { }

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



    public void recalculatePercent() {
        calculatePercent();
    }

    @Override
    public String toString() {
        return earned + "/" + possible;
    }
}
