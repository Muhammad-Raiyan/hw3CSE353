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
        HashMap<Double, DataModel> distanceMap = new HashMap<>();
        for(DataModel currentDataPoint : trainingDataList){
            distanceMap.put(getDistance(currentDataPoint.getFeaturevector(), dataModel), currentDataPoint);
        }

        System.out.print(dataModel.isPos() + " ");
        return majorityVote(distanceMap, 51) ? 1 : 0;
    }

    private Double getDistance(HashMap<String, Double> currentDataPoint, DataModel testingDataModel) {
        Double distance = 0.0;
        HashMap<String, Double> testingVector = testingDataModel.getFeaturevector();

        for(String key: currentDataPoint.keySet()){
            if(testingVector.containsKey(key)){
                distance += manhattanDistance(currentDataPoint.get(key), testingVector.get(key));
            }
            else
                distance += manhattanDistance(currentDataPoint.get(key), 0.0);
        }
        return distance;
    }

    private Double manhattanDistance(Double x, Double y) {
        return Math.abs(x-y);
    }

    private double euclideanDistance(Double x, Double y){
        double result = x - y;
        return (result*result);
    }

    private boolean majorityVote(HashMap<Double, DataModel> distanceMap, int k){
        ArrayList<Double> distanceList = new ArrayList<>(distanceMap.keySet());
        Collections.sort(distanceList);
        int pos = 0, neg = 0;
        double distance = distanceList.get(0);
        System.out.println(distance + " " + distanceMap.get(distance).isPos());
        for(int i =0; i< k; i++){
            if(distanceMap.get(distanceList.get(i)).isPos()){
                pos++;
            }
            else {
                neg++;
            }
        }
        if(pos>neg) return true;
        else return false;
    }
}
