import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/*
    Helper class to read a file
*/
public class FileReader {

    // constructor
    public FileReader() {}

    public String read(String path) throws IOException {
        // read all lines from a file into a list of strings separated by newlines
        List<String> lines = Files.readAllLines(Paths.get(path));
        StringBuilder cleanString = new StringBuilder();
        for (int i=0; i<lines.size(); i++) {
            if (lines.get(i).charAt(0) == '>') continue;
            cleanString.append(lines.get(i).toUpperCase());
        }
        // return a single string joining the list of strings
        return cleanString.toString();
    }

}