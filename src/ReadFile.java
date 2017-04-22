import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Created by ishmam on 3/17/2017.
 *
 * @author ishmam
 */

public class ReadFile {
    public ArrayList<Path> loadFileNames(Path dir){
        ArrayList<Path> fileList = new ArrayList<>();

        try(Stream<Path> paths = Files.walk(dir)) {
            paths.forEach(filePath -> {
                if(Files.isRegularFile(filePath)){
                    fileList.add(filePath);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  fileList;
    }

    public String loadFromFile(Path file){
        String fullFile = null;
        try(BufferedReader br = new BufferedReader(new FileReader(String.valueOf(file)))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while(line != null){
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            fullFile = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullFile;
    }
    public void shuffle(){

    }
}
