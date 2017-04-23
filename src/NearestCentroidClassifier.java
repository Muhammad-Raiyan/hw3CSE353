import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by ishmam on 4/23/2017.
 */
public class NearestCentroidClassifier extends Classifier{
    private final ArrayList<DataModel> trainingDataList;
    HashMap<String, Double> posCentroid, negCentroid;

    public NearestCentroidClassifier(ArrayList<DataModel> trainingDataList) {
        this.trainingDataList = trainingDataList;
    }

    public void train(Preprocess preprocess){

        ArrayList<DataModel> positiveDataPoints = new ArrayList<>();
        ArrayList<DataModel> negativeDataPoints = new ArrayList<>();
        positiveDataPoints = (ArrayList<DataModel>) trainingDataList.stream().filter(DataModel::isPos).collect(Collectors.toList());
        negativeDataPoints = (ArrayList<DataModel>) trainingDataList.stream().filter(it -> !it.isPos()).collect(Collectors.toList());
        System.out.print("Progress positive Centroid: ");
        posCentroid = findCentroid(positiveDataPoints);
        posCentroid = preprocess.normalize(posCentroid);
        System.out.print("Progress negative Centroid: ");
        negCentroid = findCentroid(negativeDataPoints);
        negCentroid = preprocess.normalize(negCentroid);
    }

    private HashMap<String, Double> findCentroid(ArrayList<DataModel> dataPoints) {
        HashMap<String, Double> centroid = new HashMap<>();

        int iteration = 0;
        for(DataModel currentData : dataPoints){
            HashMap<String, Double> currentFeatureVector = currentData.getFeaturevector();
            Set<String> keys = currentFeatureVector.keySet();
            for(String key : keys){
                if(centroid.containsKey(key)){
                    double temp = currentFeatureVector.get(key) + centroid.get(key);
                    centroid.put(key, temp);
                }
                else {
                    centroid.put(key, currentFeatureVector.get(key));
                }
            }
            iteration++;
            if(iteration%80==0) System.out.print("* ");
        }
        final double num_elements = centroid.keySet().size();
        for (String key : centroid.keySet()) {
            centroid.put(key, centroid.get(key) / num_elements);
        }
        return centroid;
    }

    public int test(DataModel testingData){
        double dPositive, dNegative;
        dPositive = Math.abs(getDistance(posCentroid, testingData.getFeaturevector(), Main.distanceStrategy));
        dNegative = Math.abs(getDistance(negCentroid, testingData.getFeaturevector(), Main.distanceStrategy));
        if (dPositive < dNegative)
            return 1;
        else
            return 0;
    }


}
