import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class DataLoader {
    private int pages;
    private int numStudents;

    public DataLoader(int pages) {
        this.pages = pages;
    }

    public boolean loadData(String directory) {

        // renaming the file and moving it to a new location
        /*
        Pseudocode:
        take the first x pages and put it in the blank test directory
        take the rest of the files and put them into StudentResponses directory
            remember to divide it by the # of pages and make a new student for each set of x pages
         */

        //Creates Blank Test
        File folder = new File(directory);

        File[] files = folder.listFiles();
        reverseFileOrder(files);

        if (files.length % pages == 0) {

            //Makes the directories
            File ScannedImageSources = new File(Constants.imagePath);
            ScannedImageSources.mkdir();
            File BlankTest = new File(Constants.blankTestPath);
            BlankTest.mkdir();
            File StudentTests = new File(Constants.StudentResponsePath);
            StudentTests.mkdir();

            //Adds the blank test pages
            for (int i = 0; i < pages; i++) {
                files[i].renameTo(new File(Constants.blankTestPath + "page" + Integer.toString(i + 1) + ".jpg"));
            }

            //Makes the student directories
            File newDirectory = new File(Constants.StudentDirectoryPath + "1");
            for (int i = pages; i < files.length; i += pages) {
                newDirectory.mkdir();
                int pageNumber = 1;
                //Adds the student's pages to the student's directory
                for (int j = i; j < i + pages; j++) {
                    files[i].renameTo(new File(newDirectory.toString() + Constants.separator + "page" + Integer.toString(pageNumber) + ".jpg"));
                    pageNumber++;
                }
                newDirectory = new File(Constants.StudentDirectoryPath + Integer.toString(pages - i + 1));
                System.out.println(pages - i + 1);
            }

            new File("src" + File.separator + "RES").delete();
            return true;
        }

        return false;
    }

    private void reverseFileOrder(File[] files) {
        for (int i = 0; i < files.length / 2; i++) {
            File temp = files[i];
            files[i] = files[files.length - i - 1];
            files[files.length - i - 1] = temp;
        }
    }

}
