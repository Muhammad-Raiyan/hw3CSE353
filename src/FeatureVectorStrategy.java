import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ishmam on 4/23/2017.
 */
public interface FeatureVectorStrategy {
    public HashMap<String, Double> buildFeatureVector(HashMap<String, Double> inputMap, ArrayList<String> content);
}
