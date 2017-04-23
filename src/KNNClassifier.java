import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by ishmam on 4/22/2017.
 */
public class KNNClassifier extends Classifier{

    private final ArrayList<DataModel> trainingDataList;
    private int K;

    public KNNClassifier(ArrayList<DataModel> trainingDataList, int K) {
        this.trainingDataList = trainingDataList;
        this.K = K;
    }

    public int test(DataModel dataModel) {
        HashMap<Double, ArrayList<DataModel>> distanceMap = new HashMap<>();
        for(DataModel currentDataPoint : trainingDataList){
            double tempKey = getDistance(currentDataPoint.getFeaturevector(), dataModel.getFeaturevector(), Main.distanceStrategy);
            ArrayList<DataModel> tempList = new ArrayList<>();
            tempList.add(currentDataPoint);

            if(distanceMap.containsKey(tempKey)){
                tempList.addAll(distanceMap.get(tempKey));
            }
            distanceMap.put(tempKey, tempList);
        }

        //System.out.print(dataModel.isPos() + " ");
        return majorityVote(distanceMap, K) ? 1 : 0;
    }

    /*protected Double getDistance(HashMap<String, Double> currentDataPoint, HashMap<String, Double> testingDataPoint) {
        Double distance = 0.0;
        //HashMap<String, Double> testingVector = testingDataPoint.getFeaturevector();
        Set<String> testingData = testingDataPoint.keySet();

        for(String oneWord : testingData){
            if(currentDataPoint.containsKey(oneWord)){
                distance += euclideanDistance(currentDataPoint.get(oneWord), testingDataPoint.get(oneWord));
            }
        }
        return (distance);
    }*/

    /*private Double manhattanDistance(Double x, Double y) {
        return Math.abs(x-y);
    }

    private double euclideanDistance(Double x, Double y){
        double result = x - y;
        return Math.sqrt(result*result);
    }*/

    private boolean majorityVote(HashMap<Double, ArrayList<DataModel>> distanceMap, int k){
        ArrayList<Double> distanceList = new ArrayList<>(distanceMap.keySet());
        Collections.sort(distanceList);
        ArrayList<DataModel> posList = new ArrayList<>();
        ArrayList<DataModel> negList = new ArrayList<>();
        //double distance = distanceList.get(0);
        //System.out.println(distance + " " + distanceMap.get(distance).isPos());
        int k_count = 0;
        for(int i =0; i < distanceList.size(); i++){
            ArrayList<DataModel> tempList = distanceMap.get(distanceList.get(i));
            if(k_count>=k) break;
            for(DataModel currentData : tempList){
                k_count++;
                if(currentData.isPos()){
                    posList.add(currentData);
                }
                else {
                    negList.add(currentData);
                }
            }
        }
        if(posList.size()>negList.size()) return true;
        else return false;
    }
}
