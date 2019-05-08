import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import processing.core.PConstants;
import processing.core.PImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class DataLoader {

    private int pages;
    private int numStudents;

    public DataLoader(int pages) {
        this.pages = pages;
    }

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

        for (PDPage page : pages) {
            try {
                images.add(page.convertToImage());

            } catch (IOException e) {
                UserInteractiveGrader.logger.log("problem converting to image");
                UserInteractiveGrader.logger.log(e.getStackTrace());
            }
        }

        try {
            pdf.close();
        } catch (IOException e) {
            UserInteractiveGrader.logger.log("could not close PDF parser!");
        }

        File res = new File("src" + File.separator + "RES");
        if (!res.exists()) {res.mkdir();}

        //write images to files inside res
        for (int i = 0; i < images.size(); i++) {
            try {
                File newPage = new File(Constants.res + "page" + i + ".png");
                newPage.createNewFile();
                ImageIO.write(images.get(i), "png", newPage);
            } catch (IOException e) {
                UserInteractiveGrader.logger.log("page" + i + " unable to load");
                System.exit(0);
            }
        }

        for (int i = 0; i < this.pages; i++) {
            try {
                ImageIO.write(images.get(i), "png", new File(Constants.res + "aaBlankTestPage" + i + ".png"));
            } catch (IOException e) {
//                UserInteractiveGrading.logger.log("Could not load startup page: (first " + this.pages + " pages)");
                e.printStackTrace();
            }
        }
    }

    public boolean sortData(String directory) {

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
            for (int i = pages - 1; i < files.length; i += pages) {
                newDirectory.mkdir();
                int pageNumber = 1;
                //Adds the student's pages to the student's directory
                for (int j = i; j < i + pages; j++) {
                    files[i].renameTo(new File(newDirectory.toString() + Constants.separator + "page" + Integer.toString(pageNumber) + ".jpg"));
                    pageNumber++;
                }
                newDirectory = new File(Constants.StudentDirectoryPath + Integer.toString(pages - i + 1));
                UserInteractiveGrader.logger.log(pages - i + 1);
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

    private ArrayList<BufferedImage> convertToBufferedImageList(ArrayList<PImage> startImages) {
        ArrayList<BufferedImage> out = new ArrayList<>();
        for (PImage image : startImages) {
            out.add((BufferedImage) image.getImage());
        }
        return out;
    }

}
