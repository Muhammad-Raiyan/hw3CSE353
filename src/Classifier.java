import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by ishmam on 4/23/2017.
 */
public abstract class Classifier {

    protected Double getDistance(HashMap<String, Double> currentDataPoint, HashMap<String, Double> testingDataPoint, DistanceStrategy distanceStrategy) {
        Double distance = 0.0;
        Set<String> testingData = testingDataPoint.keySet();

        for(String oneWord : testingData){
            if(currentDataPoint.containsKey(oneWord)){
                //distance += euclideanDistance(currentDataPoint.get(oneWord), testingDataPoint.get(oneWord));
                distance += distanceStrategy.findDistance(currentDataPoint.get(oneWord), testingDataPoint.get(oneWord));
            }
        }
        return (distance);
    }


    public abstract int test(DataModel dataModel);
}
