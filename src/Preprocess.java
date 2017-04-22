import java.util.ArrayList;
import java.util.HashMap;

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

    public HashMap<String, Double> buildBinaryFeatureVector(HashMap<String, Double> inputMap, ArrayList<String> content){
        HashMap<String, Double> tempMap = new HashMap<>(inputMap);
        for(String currentKey: content){
            if(tempMap.containsKey(currentKey) && tempMap.get(currentKey) == 0.0){
                tempMap.put(currentKey, 1.0);
            }
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
        }
        return tempMap;
    }


}
