import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    private File log;
    private BufferedWriter writer;

    private String out;

    public Logger() {
        log = new File("LOG.txt");

        try {
            writer = new BufferedWriter(new FileWriter(log));
        } catch (IOException e) {
            System.out.println("could not configure writer for log");
        }
    }

    public void log(String string) {
        out += string + "\n";
    }

    public void log(Integer integer) {
        out += integer + "\n";
    }

    public void log(Object object) {
        log(object.toString());
    }
}
