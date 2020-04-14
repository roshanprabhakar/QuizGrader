public class AnswerField {

    private int topX;
    private int bottomX;
    private int topY;
    private int bottomY;

    private int width;
    private int height;

    private int problemNum;

    public AnswerField(int topX, int topY, int bottomX, int bottomY, int problemNum) {
        this.topX = topX;
        this.bottomX = bottomX;
        this.topY = topY;
        this.bottomY = bottomY;

        this.height = Math.abs(topY - bottomY);
        this.width = Math.abs(topX - bottomX);

        this.problemNum = problemNum;
    }

    public AnswerField(String compressedField) {
        this(
                Integer.parseInt(compressedField.split(",")[0]),
                Integer.parseInt(compressedField.split(",")[1]),
                Integer.parseInt(compressedField.split(",")[2]),
                Integer.parseInt(compressedField.split(",")[3]),
                Integer.parseInt(compressedField.split(",")[4])
        );
    }

    public AnswerField() {
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setProblemNum(int problemNum) {
        this.problemNum = problemNum;
    }

    public void setTopX(int topX) {
        this.topX = topX;
    }

    public void setBottomX(int bottomX) {
        this.bottomX = bottomX;
    }

    public void setTopY(int topY) {
        this.topY = topY;
    }

    public void setBottomY(int bottomY) {
        this.bottomY = bottomY;
    }

    public int getProblemNum() {
        return this.problemNum;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTopX() {
        return topX;
    }

    public int getBottomX() {
        return bottomX;
    }

    public int getTopY() {
        return topY;
    }

    public int getBottomY() {
        return bottomY;
    }

    @Override
    public String toString() {
        return "topX: " + topX +" topY: " + topY + " bottomX: " + bottomX + " bottomY: " + bottomY
                + " height: " + height + " width: " + width + " num: " + problemNum;
    }

    public String compressedString() {
        return topX + "," + topY + "," + bottomX + "," + bottomY + "," + problemNum;
    }
}
