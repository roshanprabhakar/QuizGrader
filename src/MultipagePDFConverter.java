import java.io.File;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class MultipagePDFConverter {

    private File unparsedPDF;
    private ArrayList<File> seperatedPNG;

    public MultipagePDFConverter(String filepath) {
        unparsedPDF = new File(filepath);
        seperatedPNG = new ArrayList<>();
    }

    public ArrayList<File> getSeperatedPNG() {
        return seperatedPNG;
    }

    public void instantiatePNG() {
//        instantiate files defined by the class arraylist
    }
}
