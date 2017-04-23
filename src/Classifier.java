import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by ishmam on 4/23/2017.
 */
public abstract class Classifier {

    protected Double manhattanDistance(Double x, Double y) {
        return Math.abs(x-y);
    }

    protected double euclideanDistance(Double x, Double y){
        double result = x - y;
        return Math.sqrt(result*result);
    }

    protected Double getDistance(HashMap<String, Double> currentDataPoint, HashMap<String, Double> testingDataPoint) {
        Double distance = 0.0;
        //HashMap<String, Double> testingVector = testingDataPoint.getFeaturevector();
        Set<String> testingData = testingDataPoint.keySet();

        for(String oneWord : testingData){
            if(currentDataPoint.containsKey(oneWord)){
                distance += euclideanDistance(currentDataPoint.get(oneWord), testingDataPoint.get(oneWord));
            }
        }
        return (distance);
    }


    public abstract int test(DataModel dataModel);
}
