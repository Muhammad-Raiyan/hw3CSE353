import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ishmam on 4/23/2017.
 */
public class BinaryFeatureVector implements FeatureVectorStrategy {

    @Override
    public HashMap<String, Double> buildFeatureVector(HashMap<String, Double> inputMap, ArrayList<String> content){
        HashMap<String, Double> tempMap = new HashMap<>(inputMap);
        for(String currentKey: content){
            tempMap.put(currentKey, 1.0);
        }
        return tempMap;
    }
}
