import java.io.*;
import java.util.ArrayList;

public class Cleaner {

    public static void eraseTrailingCommas() {
        try {

           File outCSV = new File(Constants.outCSV);
           BufferedWriter writer;

           for (File outFile : outCSV.listFiles()) {
               System.out.println(outFile.getName());
               writer = new BufferedWriter(new FileWriter(outFile));
               ArrayList<String> lines = Constants.getLines(outFile);
               for (String line : lines) {
                   writer.write(removeTrailingComma(line));
                   writer.write("\n");
               }
               writer.close();
           }


        } catch (IOException e) {
            System.err.println("Could not format generated CSV data");
            e.printStackTrace();
        }
    }

    private static String removeTrailingComma(String line) {
        StringBuilder out = new StringBuilder();
        boolean met = false;
        for (int i = line.length() - 1; i >= 0; i++) {
            if (line.charAt(i) != ',') {
                met = true;
            }
            if (met) {
                out.append(line.charAt(i));
            }
        }
        return out.toString();
    }
}
