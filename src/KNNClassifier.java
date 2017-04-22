import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by ishmam on 4/22/2017.
 */
public class KNNClassifier {

    private final ArrayList<DataModel> trainingDataList;
    private int K;

    public KNNClassifier(ArrayList<DataModel> trainingDataList, int K) {
        this.trainingDataList = trainingDataList;
        this.K = K;
    }

    public int test(DataModel dataModel) {
        ArrayList<Double> distanceList = new ArrayList<>();
        HashMap<Double, DataModel> distanceMap = new HashMap<>();
        for(DataModel currentDataPoint : trainingDataList){
            distanceMap.put(getDistance(currentDataPoint.getFeaturevector(), dataModel), currentDataPoint);
        }
        distanceList = new ArrayList<>(distanceMap.keySet());
        Collections.sort(distanceList);
        double distance = distanceList.get(0);
        //System.out.println(distance + " " + distanceMap.get(distance).isPos() + " " + dataModel.isPos());
        return distanceMap.get(distance).isPos() ? 1 : 0;
    }

    private Double getDistance(HashMap<String, Double> currentDataPoint, DataModel testingDataModel) {
        Double distance = 0.0;
        HashMap<String, Double> testingVector = testingDataModel.getFeaturevector();
        ArrayList<String> testingData = testingDataModel.getContent();

        for(String oneWord : testingData){
            if(currentDataPoint.containsKey(oneWord)){
                distance += manhattanDistance(currentDataPoint.get(oneWord), testingVector.get(oneWord));
            }
        }
        return distance;
    }

    private Double manhattanDistance(Double x, Double y) {
        return Math.abs(x-y);
    }
}
