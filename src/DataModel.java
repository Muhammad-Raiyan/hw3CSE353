import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ishmam on 3/18/2017.
 *
 * @author ishmam
 */
public class DataModel {
    private Path path;
    private boolean isPos;
    private boolean isTestData;
    private ArrayList<String> content;
    private HashMap<String, Double> featurevector;

    public DataModel(Path path, boolean isPos) {
        this.path = path;
        this.isPos = isPos;
    }

    public HashMap<String, Double> getFeaturevector() {
        return featurevector;
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public Path getPath() {
        return path;
    }

    public void setFeaturevector(HashMap<String, Double> featurevector) {
        this.featurevector = featurevector;
    }

    public void setContent(ArrayList<String> content) {
        this.content = content;
    }

    public void setPath(Path file) {
        this.path = file;
    }

    public boolean isPos() {
        return isPos;
    }

    public boolean isTestData() {
        return isTestData;
    }

    public boolean isTrainingData() {
        return !isTestData;
    }

    public void setTestData(boolean testData) {
        isTestData = testData;
    }

}
