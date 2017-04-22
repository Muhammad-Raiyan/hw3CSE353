import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static int NFold = 5;
    private static final String positiveDir = "C:\\Users\\ishmam\\Documents\\Programming\\hw1CS353\\data\\pos";
    private static final String negativeDir = "C:\\Users\\ishmam\\Documents\\Programming\\hw1CS353\\data\\neg";
    private static final String stopWordsDir = "C:\\Users\\ishmam\\Documents\\Programming\\hw3CSE353\\data\\stopWords.txt";
    private static String[] stopWords;
    private static String filter = "[^a-zA-Z\\s]";

    private static ArrayList<DataModel> dataModels = new ArrayList<>();
    private static ArrayList<Path> pathList = new ArrayList<>();

    public static void main(String[] args) {

        ReadFile readFile = new ReadFile();
        pathList.addAll(readFile.loadFileNames(Paths.get(positiveDir)));
        pathList.addAll(readFile.loadFileNames(Paths.get(negativeDir)));
        long seed = System.nanoTime();
        Collections.shuffle(pathList, new Random(seed));
        //String stopWord = readFile.loadFromFile(Paths.get(stopWordsDir));
        //stopWord = stopWord.replace("\r", " ").replace("\n", " ");
        //stopWords = stopWord.split("\\s+");
        for(Path path: pathList){
            String content = readFile.loadFromFile(path);
            content = content.replace("\r", " ").replace("\n", " ");

            //content = content.replaceAll(filter, "");

            // add all datamodel to filelist
            DataModel dm;
            if(String.valueOf(path).contains("pos")){
                dm = new DataModel(path, true);

            } else{
                dm = new DataModel(path, false);
            }

            ArrayList<String> sArray = new ArrayList<>(Arrays.asList(content.split("\\s+")));
            sArray.replaceAll(String::toLowerCase);
            //sArray = removeStopWords(sArray);
            dm.setContent(sArray);

            dataModels.add(dm);
        }
        startKNN();
        System.out.println("Done");
    }

    private static void startKNN(){

        Preprocess preprocess = new Preprocess(dataModels);
        double avgAccuracy = 0, avgPrecision = 0, avgRecall = 0;

        ArrayList<DataModel> trainingDataList;
        ArrayList<DataModel> testingDataList;

        for(int i = 0; i<NFold; i++){

            preprocess.runCrossValidation(i);

            trainingDataList = (ArrayList<DataModel>) dataModels.stream().filter(DataModel::isTrainingData).collect(Collectors.toList());
            testingDataList = (ArrayList<DataModel>) dataModels.stream().filter(DataModel::isTestData).collect(Collectors.toList());

            System.out.println("Fold #" + (i+1) + "\nPrepreprocessing: ");
            HashMap<String, Double> defaultFeatureVector = new HashMap<>();
            int count = 0;
            for(DataModel dataModel: trainingDataList){

                // build default feature vector
                ArrayList<String> currentContent = dataModel.getContent();
                for(String key : currentContent){
                    if(!defaultFeatureVector.containsKey(key)) {
                        defaultFeatureVector.put(key, 0.0);
                    }
                }
            }

            for (DataModel dataModel: trainingDataList){
                HashMap<String, Double> inputVector;
                inputVector = preprocess.buildBinaryFeatureVector(defaultFeatureVector, dataModel.getContent());
                //inputVector = preprocess.normalize(inputVector);
                dataModel.setFeaturevector(inputVector);

                // Print status of preprocess
                count++;
                if(count%160==0){
                    System.out.print(count/16 + "% -> ");
                }
            }

            System.out.println("\nTesting: ");
            int testCount = 0;
            double tp = 0, fp = 0, tn = 0, fn = 0;

            // Build testing vectors
            for (DataModel dataModel: testingDataList){
                HashMap<String, Double> inputVector = new HashMap<>();
                inputVector = preprocess.buildBinaryFeatureVector(defaultFeatureVector, dataModel.getContent());
                //inputVector = preprocess.normalize(inputVector);
                dataModel.setFeaturevector(inputVector);
                testCount++;
                if(testCount%40==0){
                    System.out.print(testCount/4 + "% -> ");
                }
            }
            System.out.println();
            for( DataModel dataModel: testingDataList) {

                KNNClassifier knnClassifier = new KNNClassifier(trainingDataList, 3);
                int prediction = knnClassifier.test(dataModel);
                if(prediction == 1){
                    if(dataModel.isPos()) tp++;
                    else fp++;
                }
                else {
                    if(dataModel.isPos()) fn++;
                    else tn++;
                }

                /*testCount++;
                if(testCount%40==0){
                    System.out.print(testCount/4 + "% -> ");
                }*/
            }

            double precisionP = tp/(tp+fp);
            double precisionN = tn/(tn+fp);
            double recallP = tp / (tp+fn);
            double recallN = tn / (tn+fp);
            double accuracy = (tp+tn)/(tp+tn+fp+fn);
            double precision = (precisionP+precisionN)/2.0;
            double recall = (recallN + recallP) / 2.0;
            System.out.println("\nTP - FP - TN - FN: " + tp + " " + fp + " " + tn + " " + fn);
            System.out.println("Accuracy: " + String.format("%.4f", accuracy));
            System.out.println("Precision: " + String.format("%.4f", precision));
            System.out.println("Recall: " + String.format("%.4f", recall));

            avgAccuracy += accuracy;
            avgPrecision += precision;
            avgRecall += recall;
            System.out.println();
        }
        System.out.println("Average Accuracy: " + String.format("%.3f", avgAccuracy/5.0));
        System.out.println("Average Precision: " + String.format("%.3f", avgPrecision/5.0));
        System.out.println("Average Recall: " + String.format("%.3f", avgRecall/5.0));
    }

    public static ArrayList<String> removeStopWords(ArrayList<String> content){

        for(int i=0; i<stopWords.length; i++){
            content.remove(stopWords[i]);
        }

        return content;
    }
}
