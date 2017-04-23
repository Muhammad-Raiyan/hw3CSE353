import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ishmam on 4/23/2017.
 */
public class FrequencyFeatureVector implements FeatureVectorStrategy {

    @Override
    public HashMap<String, Double> buildFeatureVector(HashMap<String, Double> inputMap, ArrayList<String> content){
        HashMap<String, Double> tempMap = new HashMap<>(inputMap);
        for(String currentKey: content){
            if(tempMap.containsKey(currentKey)){
                double temp = tempMap.get(currentKey);
                temp++;
                tempMap.put(currentKey, temp);
            }
            else{
                tempMap.put(currentKey, 0.0);
            }
        }
        return tempMap;
    }
}
