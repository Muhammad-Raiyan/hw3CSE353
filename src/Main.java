import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {



    public static int NFold = 5;
    private static final String positiveDir = "C:\\Users\\ishmam\\Documents\\Programming\\hw1CS353\\data\\pos";
    private static final String negativeDir = "C:\\Users\\ishmam\\Documents\\Programming\\hw1CS353\\data\\neg";
    private static String filter = "[^a-zA-Z\\s]";

    private static ArrayList<DataModel> dataModels = new ArrayList<>();
    private static ArrayList<Path> pathList = new ArrayList<>();

    public static void main(String[] args) {

        ReadFile readFile = new ReadFile();
        pathList.addAll(readFile.loadFileNames(Paths.get(positiveDir)));
        pathList.addAll(readFile.loadFileNames(Paths.get(negativeDir)));
        long seed = System.nanoTime();
        Collections.shuffle(pathList, new Random(seed));

        for(Path path: pathList){
            String content = readFile.loadFromFile(path);
            content = content.replace("\r", "").replace("\n", "");
            //content = content.replaceAll(filter, "");

            // add all datamodel to filelist
            DataModel dm;
            if(String.valueOf(path).contains("pos")){
                dm = new DataModel(path, true);

            } else{
                dm = new DataModel(path, false);
            }

            String sArray[] = content.split(" ");
            dm.setContent(new ArrayList<>(Arrays.asList(sArray)));

            dataModels.add(dm);
        }
        startKNN();
        System.out.println("Done");
    }

    private static void startKNN(){

        Preprocess preprocess = new Preprocess(dataModels);


        ArrayList<DataModel> trainingDataList;
        ArrayList<DataModel> testingDataList;

        for(int i = 0; i<NFold; i++){

            preprocess.runCrossValidation(i);

            trainingDataList = (ArrayList<DataModel>) dataModels.stream().filter(DataModel::isTrainingData).collect(Collectors.toList());
            testingDataList = (ArrayList<DataModel>) dataModels.stream().filter(DataModel::isTestData).collect(Collectors.toList());

            System.out.print("Fold #" + (i+1) + "\nPrepreprocessing: ");
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

                // Print status of preprocess
                count++;
                if(count%160==0){
                    System.out.print(count/16.00 + "% -> ");
                }
            }

            System.out.println();
        }

    }

}
