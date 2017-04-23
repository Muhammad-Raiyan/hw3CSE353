import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by ishmam on 3/24/2017.
 *
 * @author ishmam
 */
public class Preprocess {
    private ArrayList<DataModel> fileList;

    public Preprocess(ArrayList<DataModel> fileList) {
        this.fileList = fileList;
    }

    public void runCrossValidation(int N){

        int[] begin = {0, 400, 800, 1200, 1600};
        int[] end = {399, 799, 1199, 1599, 1999};

        // set all files as training data
        for(DataModel x: fileList){
            x.setTestData(false);
        }

        // change selected ones as testing data
        for(int i=begin[N]; i<=end[N]; i++){
            DataModel dm = fileList.get(i);
            dm.setTestData(true);
            dm.setFeaturevector(null);
            fileList.set(i, dm);
        }
    }

    public HashMap<String, Double> buildFeatureVector(HashMap<String, Double> inputMap, ArrayList<String> content, FeatureVectorStrategy featureVectorStrategy){
        return featureVectorStrategy.buildFeatureVector(inputMap, content);
    }

    /*public HashMap<String, Double> buildBinaryFeatureVector(HashMap<String, Double> inputMap, ArrayList<String> content){
        HashMap<String, Double> tempMap = new HashMap<>(inputMap);
        for(String currentKey: content){
            tempMap.put(currentKey, 1.0);
        }
        return tempMap;
    }

    public HashMap<String, Double> buildFrequencyFeatureVector(HashMap<String, Double> inputMap, ArrayList<String> content){
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
    }*/

    /*public HashMap<String, Double> normalize(HashMap<String, Double> inputVector) {
        Set<String> keySet = inputVector.keySet();
        double mean = findMean(inputVector, keySet);
        double sd = findSD(inputVector, mean, keySet);

        for(String key: keySet){
            if(inputVector.get(key)==0.0)
                continue;
            double z = 0.0;
            double x = inputVector.get(key);
            z = (x - mean) / sd;
            inputVector.put(key, z);
        }
        return inputVector;
    }*/

    public HashMap<String, Double> normalize(HashMap<String, Double> inputVector){

        Set<String> keySet = inputVector.keySet();
        double norm = vectorNorm(inputVector);

        for(String key: keySet){
            if(inputVector.get(key)==0.0)
                continue;
            double x = inputVector.get(key)/norm;
            inputVector.put(key, x);
        }
        return inputVector;
    }


    private double findMean(HashMap<String, Double> inputVector, Set<String> keySet) {
        double result = 0.0;
        for(String key: keySet){
            result += inputVector.get(key);
        }
        return result/keySet.size();
    }

    private double findSD(HashMap<String, Double> inputVector, double mean, Set<String> keySet) {
        double result = 0.0;
        for(String key: keySet){
            double diff = inputVector.get(key) - mean;
            result += (diff*diff);
        }

        return Math.sqrt(result/keySet.size());
    }

    private double vectorNorm(HashMap<String, Double> inputVector){
        double result = 0.0;

        for(String key: inputVector.keySet()){
            double temp = inputVector.get(key);
            result += (temp*temp);
        }

        return Math.sqrt(result);
    }

}
