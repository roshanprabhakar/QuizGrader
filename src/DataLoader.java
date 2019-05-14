import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    private int pages;
    private ProgressPane progressPane;

    public DataLoader(int pages) {
        this.pages = pages;
        progressPane = new ProgressPane();
        progressPane.display();
    }

    /**
     * Loading data from PDF input
     **/

    public void loadData(String multipagePDF) {

        ArrayList<BufferedImage> images = new ArrayList<>();

        PDDocument pdf = null;

        try {
            pdf = PDDocument.load(new File(Constants.pdfIn + multipagePDF));
        } catch (IOException e) {
            UserInteractiveGrader.logger.log("Couldn't load pdf");
            UserInteractiveGrader.logger.log("DID YOU ADD THE ASSETS FOLDER TO YOUR CLASS PATH IN ECLIPSE?");
            UserInteractiveGrader.logger.log(e.getStackTrace());
        }

        List<PDPage> pages = pdf.getDocumentCatalog().getAllPages();

        progressPane.setMessage("reading from scanned pdf...");

        int counter = 1;
        for (PDPage page : pages) {

            int percent = (int)((double) counter / pages.size() * 100) * 3;
            progressPane.setProgressBarPercentage(percent);

            try {
                images.add(page.convertToImage());
            } catch (IOException e) {
                UserInteractiveGrader.logger.log("problem converting to image");
                UserInteractiveGrader.logger.log(e.getStackTrace());
            }

            counter++;
        }
        progressPane.resetProgressBar();

        try {
            pdf.close();
        } catch (IOException e) {
            UserInteractiveGrader.logger.log("could not close PDF parser!");
        }

        File res = new File("src" + File.separator + "RES");
        if (!res.exists()) {
            res.mkdir();
        }

        progressPane.setMessage("Writing scanned images...");

        //write images to files inside res
        for (int i = 0; i < images.size(); i++) {

            int percent = (int)((double) i / images.size() * 100) * 3;

            try {

                progressPane.setProgressBarPercentage(percent);

                File newPage = new File(Constants.res + "page" + i + ".png");
                newPage.createNewFile();
                ImageIO.write(images.get(i), "png", newPage);

            } catch (IOException e) {

                UserInteractiveGrader.logger.log("page" + i + " unable to load");
                System.exit(0);

            }
        }

        progressPane.setMessage("converting loaded images to readable...");

        for (int i = 0; i < this.pages; i++) {

            int percent = (int)((double) i / pages.size() * 100) * 3;
            progressPane.setProgressBarPercentage(percent);

            try {
                ImageIO.write(images.get(i), "png", new File(Constants.res + "BlankTestPage" + (i + 1) + ".png"));
            } catch (IOException e) {
                UserInteractiveGrader.logger.log("Could not load startup page: (first " + this.pages + " pages)");
            }
        }

        progressPane.setMessage("Data loading complete!");
        progressPane.close();
    }

    /**
     * Sorting loaded data from RES to ScannedImageSources
     **/
    public void sortData() {

        progressPane.display();
        ArrayList<String> students = getStudentList("names.txt");

        File scannedSources = new File("src" + Constants.separator + "ScannedImageSources");
        if (!scannedSources.exists()) scannedSources.mkdir();

        File studentResDir = new File("src" + Constants.separator + "ScannedImageSources" + Constants.separator + "StudentResponses");
        if (!studentResDir.exists()) studentResDir.mkdir();

        for (String student : students) {
            File thisStudent = new File("src" + Constants.separator + "ScannedImageSources" + Constants.separator + "StudentResponses" + Constants.separator + student);
            if (!thisStudent.exists()) thisStudent.mkdir();
        }

        progressPane.setMessage("Sorting Data...");

        int pageNum = 0; //for accessing files in RES
        for (int student = 0; student < students.size(); student++) { //problem with indexing paper

            progressPane.setMessage("sorting for student: " + students.get(student));
            for (int pageInTest = 1; pageInTest <= pages; pageInTest++) {

                progressPane.setProgressBarPercentage((int)((double) pageInTest / pages * 100) * 3);

                File origin = new File(Constants.res + "page" + pageNum + ".png");
                File goal = new File(Constants.studentResponses + students.get(student) + Constants.separator + "page" + pageInTest + ".png");
                UserInteractiveGrader.logger.log(pageInTest);

                UserInteractiveGrader.logger.log("origin: " + origin.getAbsolutePath());
                UserInteractiveGrader.logger.log("goal: " + goal.getAbsolutePath());

                move(origin, goal);

                pageNum++;
            }
        }

        //moving blank test pages
        File blankTestDir = new File(Constants.imagePath + "BlankTestPages" + Constants.separator);
        if (!blankTestDir.exists()) blankTestDir.mkdir();

        System.out.println("-------------------------------------");

        progressPane.setMessage("Initializing blank test...");
        for (int i = 1; i <= pages; i++) {

            progressPane.setProgressBarPercentage((int)((double) i / pages * 100) * 3);

            File origin = new File(Constants.res + "BlankTestPage" + i + ".png");
            UserInteractiveGrader.logger.log("origin: " + origin.getAbsolutePath());
            File goal = new File(Constants.blankTest + "page" + i + ".png");
            UserInteractiveGrader.logger.log("goal: " + goal.getAbsolutePath());

            move(origin, goal);
        }

        progressPane.setMessage("Complete!");
        progressPane.close();
    }

    private ArrayList<String> getStudentList(String namesFile) {

        ArrayList<String> students = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(namesFile)));
            String student;
            while ((student = reader.readLine()) != null) {
                students.add(student.replaceAll("\n", ""));
            }
        } catch (IOException e) {
            UserInteractiveGrader.logger.log("Unknown IOException while reading from RES!");
        }

        return students;
    }

    private void move(File origin, File goal) {
        origin.renameTo(goal);
    }
}

//    public boolean sortData(String directory) {
//
//        // renaming the file and moving it to a new location
//        /*
//        Pseudocode:
//        take the first x pages and put it in the blank test directory
//        take the rest of the files and put them into StudentResponses directory
//            remember to divide it by the # of pages and make a new student for each set of x pages
//         */
//
//        //Creates Blank Test
//        File folder = new File(directory);
//
//        File[] files = folder.listFiles();
//        reverseFileOrder(files);
//
//        if (files.length % pages == 0) {
//
//            //Makes the directories
//            File ScannedImageSources = new File(Constants.imagePath);
//            ScannedImageSources.mkdir();
//            File BlankTest = new File(Constants.blankTestPath);
//            BlankTest.mkdir();
//            File StudentTests = new File(Constants.StudentResponsePath);
//            StudentTests.mkdir();
//
//            //Adds the blank test pages
//            for (int i = 0; i < pages; i++) {
//                files[i].renameTo(new File(Constants.blankTestPath + "page" + Integer.toString(i + 1) + ".jpg"));
//            }
//
//            //Makes the student directories
//            File newDirectory = new File(Constants.StudentDirectoryPath + "1");
//            for (int i = pages - 1; i < files.length; i += pages) {
//                newDirectory.mkdir();
//                int pageNumber = 1;
//                //Adds the student's pages to the student's directory
//                for (int j = i; j < i + pages; j++) {
//                    files[i].renameTo(new File(newDirectory.toString() + Constants.separator + "page" + Integer.toString(pageNumber) + ".jpg"));
//                    pageNumber++;
//                }
//                newDirectory = new File(Constants.StudentDirectoryPath + Integer.toString(pages - i + 1));
//                UserInteractiveGrader.logger.log(pages - i + 1);
//            }
//
//            new File("src" + File.separator + "RES").delete();
//            return true;
//        }
//
//        return false;
//    }

