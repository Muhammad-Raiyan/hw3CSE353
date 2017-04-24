import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by ishmam on 4/23/2017.
 */
public class TFIDF implements FeatureVectorStrategy {
    @Override
    public HashMap<String, Double> buildFeatureVector(HashMap<String, Double> inputMap, ArrayList<String> content) {
        String temp[] = content.stream().toArray(String[]::new);
        HashMap<String, Double> tfMap = new HashMap<>(inputMap);
        for(int i=0; i<temp.length; i++){
            tfMap.put(temp[i], tf(temp, temp[i]));
        }

        return tfMap;
    }

    private Double tf(String[] content, String s) {
        Double result = 0.0;

        for(int i=0; i<content.length; i++){
            if(s.equalsIgnoreCase(content[i])){
                result++;
            }
        }
        double bias = 1.0;
        if(result == 0.0) bias = 0;
        return bias+Math.log(result/content.length);
    }
}
