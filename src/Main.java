import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static int NFold = 5;
    public static DistanceStrategy distanceStrategy;
    public static boolean keepPunct = true;

    private static final String positiveDir = "./data/pos";
    private static final String negativeDir = "./data/neg";
    private static final String stopWordDir = "./data/stopWords.txt";

    private static final String filter = "[^a-zA-Z\\s]";
    private static final String accuracyKey = "Accuracy", precisionKey = "Precision", recallKey = "Recall";
    private static FeatureVectorStrategy featureVectorStrategy;
    private static Preprocess preprocess;
    private static ArrayList<DataModel> dataModels = new ArrayList<>();
    private static ArrayList<Path> pathList = new ArrayList<>();

    public static void main(String[] args) {

        System.out.println(Arrays.toString(args));

        if(args.length < 4 ) {
            System.exit(1);
        }
        if(args[2].replace("--", "").equals("binary")){
            featureVectorStrategy = new BinaryFeatureVector();
        }
        else {
            featureVectorStrategy = new FrequencyFeatureVector();
        }

        if(args[3].replace("--", "").equals("modify")){
            keepPunct = false;
        }
        else {
            keepPunct = true;
        }

        if(args[4].replace("--", "").replace("p=", "").equals("0")){
            distanceStrategy = new ManhattanDistance();
        }
        if(args[4].replace("--", "").replace("p=", "").equals("1")){
            distanceStrategy = new EuclideanDistance();
        }

        ReadFile readFile = new ReadFile();
        pathList.addAll(readFile.loadFileNames(Paths.get(positiveDir)));
        pathList.addAll(readFile.loadFileNames(Paths.get(negativeDir)));
        long seed = System.nanoTime();
        Collections.shuffle(pathList, new Random(seed));
        String[] stopWords = readFile.loadFromFile(Paths.get(stopWordDir)).replace("\r", " ")
                                     .replace("\n", " ").split(" ");
        for(Path path: pathList){
            String content = readFile.loadFromFile(path);
            content = content.replace("\r", "").replace("\n", "");

            if(!keepPunct)
                content = content.replaceAll(filter, "");

            // add all datamodel to filelist
            DataModel dm;
            if(String.valueOf(path).contains("pos")){
                dm = new DataModel(path, true);

            } else{
                dm = new DataModel(path, false);
            }

            String sArray[] = content.split(" ");
            ArrayList<String> tempContent = new ArrayList<>(Arrays.asList(sArray));
            for(int i =0; i<stopWords.length; i++){
                if(tempContent.contains(stopWords[i])) tempContent.remove(stopWords[i]);
            }
            dm.setContent(tempContent);

            dataModels.add(dm);
        }
        preprocess = new Preprocess(dataModels);

        try {
            if (args[1].replace("--", "").equals("knn")) {
                startKNN(preprocess);
                NFold = Integer.parseInt(args[5].replace("--", "").replace("k=", ""));
            } else if (args[1].replace("--", "").equals("ncc"))
                startNCC(preprocess);

            System.out.println("Done");
        } catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
        System.exit(0);
    }
    private static void startKNN(Preprocess preprocess){

        ArrayList<DataModel> trainingDataList;
        ArrayList<DataModel> testingDataList;
        HashMap<String, Double> avgResult = new HashMap<>();
        avgResult.put(accuracyKey, 0.0);
        avgResult.put(precisionKey, 0.0);
        avgResult.put(recallKey, 0.0);

        for(int i = 0; i<NFold; i++) {

            preprocess.runCrossValidation(i);

            trainingDataList = (ArrayList<DataModel>) dataModels.stream().filter(DataModel::isTrainingData).collect(Collectors.toList());
            testingDataList = (ArrayList<DataModel>) dataModels.stream().filter(DataModel::isTestData).collect(Collectors.toList());

            System.out.println("\nFold #" + (i + 1) + "\nPrepreprocessing: ");
            HashMap<String, Double> defaultFeatureVector = generateDefaultVector(trainingDataList);

            generateTrainigVectors(defaultFeatureVector, trainingDataList, true);

            generateTestingVector(testingDataList, true);

            KNNClassifier knnClassifier = new KNNClassifier(trainingDataList, NFold);
            HashMap<String, Double> tempResult = runClassifier(knnClassifier, testingDataList);
            avgResult.put(accuracyKey, avgResult.get(accuracyKey)+tempResult.get(accuracyKey));
            avgResult.put(precisionKey, avgResult.get(precisionKey)+tempResult.get(precisionKey));
            avgResult.put(recallKey, avgResult.get(recallKey)+tempResult.get(recallKey));
        }

        System.out.println("Average Accuracy: " + String.format("%.3f", avgResult.get(accuracyKey)/5.0));
        System.out.println("Average Precision: " + String.format("%.3f", avgResult.get(precisionKey)/5.0));
        System.out.println("Average Recall: " + String.format("%.3f", avgResult.get(recallKey)/5.0));
    }

    public static void startNCC(Preprocess preprocess){

        ArrayList<DataModel> trainingDataList;
        ArrayList<DataModel> testingDataList;
        HashMap<String, Double> avgResult = new HashMap<>();
        avgResult.put(accuracyKey, 0.0);
        avgResult.put(precisionKey, 0.0);
        avgResult.put(recallKey, 0.0);

        for(int i = 0; i<NFold; i++){

            preprocess.runCrossValidation(i);

            trainingDataList = (ArrayList<DataModel>) dataModels.stream().filter(DataModel::isTrainingData).collect(Collectors.toList());
            testingDataList = (ArrayList<DataModel>) dataModels.stream().filter(DataModel::isTestData).collect(Collectors.toList());

            System.out.println("\nFold #" + (i+1) + "\nPrepreprocessing: ");
            HashMap<String, Double> defaultFeatureVector = generateDefaultVector(trainingDataList);
            System.out.print("Building training vectors: ");
            generateTrainigVectors(defaultFeatureVector, trainingDataList, false);

            NearestCentroidClassifier ncClassifier = new NearestCentroidClassifier(trainingDataList);

            System.out.println("\nTraining: ");
            ncClassifier.train(preprocess);

            System.out.print("\nBuilding Testing vectors: ");
            generateTestingVector(testingDataList, false);
            System.out.println("Complete: 100%");

            HashMap<String, Double> tempResult = runClassifier(ncClassifier, testingDataList);

            avgResult.put(accuracyKey, avgResult.get(accuracyKey)+tempResult.get(accuracyKey));
            avgResult.put(precisionKey, avgResult.get(precisionKey)+tempResult.get(precisionKey));
            avgResult.put(recallKey, avgResult.get(recallKey)+tempResult.get(recallKey));

        }
        System.out.println("Average Accuracy: " + String.format("%.3f", avgResult.get(accuracyKey)/5.0));
        System.out.println("Average Precision: " + String.format("%.3f", avgResult.get(precisionKey)/5.0));
        System.out.println("Average Recall: " + String.format("%.3f", avgResult.get(recallKey)/5.0));
    }



    private static HashMap<String, Double> generateDefaultVector(ArrayList<DataModel> dataList) {
        HashMap<String, Double> defaultFeatureVector = new HashMap<>();

        for(DataModel dataModel: dataList){
            // build default feature vector
            ArrayList<String> currentContent = dataModel.getContent();
            for(String key : currentContent){
                if(!defaultFeatureVector.containsKey(key)) {
                    defaultFeatureVector.put(key, 0.0);
                }
            }
        }
        return defaultFeatureVector;
    }

    private static void generateTestingVector(ArrayList<DataModel> testingDataList, boolean normalize) {
        for (DataModel dataModel: testingDataList){
            HashMap<String, Double> inputVector = new HashMap<>();
            inputVector = preprocess.buildFeatureVector(inputVector, dataModel.getContent(), featureVectorStrategy);
            if(normalize) inputVector = preprocess.normalize(inputVector);
            dataModel.setFeaturevector(inputVector);
        }
    }

    private static void generateTrainigVectors(HashMap<String, Double> defaultFeatureVector, ArrayList<DataModel> dataList, boolean normalize) {
        int count = 0;
        for (DataModel dataModel: dataList){
            HashMap<String, Double> inputVector = new HashMap<>();
            inputVector = preprocess.buildFeatureVector(defaultFeatureVector, dataModel.getContent(), featureVectorStrategy);
            if(normalize) inputVector = preprocess.normalize(inputVector);
            dataModel.setFeaturevector(inputVector);

            // Print status of preprocess
            count++;
            if(count%160==0){
                System.out.print(count/16 + "% -> ");
            }
        }
    }

    public static HashMap<String, Double> runClassifier(Classifier classifier, ArrayList<DataModel> testingDataList){
        System.out.println("\nTesting: ");
        double tp = 0, fp = 0, tn = 0, fn = 0;
        double avgAccuracy = 0, avgPrecision = 0, avgRecall = 0;
        HashMap<String, Double> avgResult = new HashMap<>();

        int testCount = 0;
        for( DataModel dataModel: testingDataList) {
            int prediction = classifier.test(dataModel);
            if(prediction == 1){
                if(dataModel.isPos()) tp++;
                else fp++;
            }
            else {
                if(dataModel.isPos()) fn++;
                else tn++;
            }

            testCount++;
            if(testCount%40==0){
                System.out.print(testCount/4 + "% -> ");
            }
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

        avgResult.put(accuracyKey, accuracy);
        avgResult.put(precisionKey, precision);
        avgResult.put(recallKey, recall);

        return avgResult;
    }

}
