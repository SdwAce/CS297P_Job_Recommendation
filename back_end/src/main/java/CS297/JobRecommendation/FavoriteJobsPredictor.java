package CS297.JobRecommendation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Database.Recommendation;
import Model.Job;
import weka.classifiers.Classifier; // Classifier interface
import weka.classifiers.Evaluation; // Cross-Validation
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.LogitBoost;
import weka.classifiers.pmml.consumer.NeuralNetwork;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.J48; // main Decision Tree class implemented with C4.5 algorithm
import weka.classifiers.trees.LMT;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;
import weka.core.Instance;
import weka.core.Instances; // wrapper for datasets
import weka.core.converters.DatabaseLoader; // load data from postgres
import weka.filters.unsupervised.attribute.NumericToBinary;

public class FavoriteJobsPredictor {

    public Instances trainData;

    public FavoriteJobsPredictor() {
        this.trainData = null;
    }

    public Instances getTrainData() { return this.trainData; }

    private Instances trainTestSplit(Instances data) {
        long seed = 0; // TODO
        Random rand = new Random(seed);   // create seeded number generator
        Instances randData = new Instances(data);   // create copy of original data
        randData.randomize(rand);         // randomize data with number generator
        return randData;
    }

    private Classifier dtCrossValidate(Instances dataSet) throws Exception {

        // perform 80-20 train-test split with 10 folds
        int folds = 5;
        Instances randData = trainTestSplit(dataSet); // randomize a copy of dataSet
        Instances trainSet = randData.trainCV(folds, 0); // train set
        Instances testSet = randData.testCV(folds, 0); // test set

//        Classifier classifier = new NaiveBayes(); // Classifier here is Naive Bayes
//        Classifier classifier = new BayesNet(); // Classifier here is Bayes Neural Net
        Classifier classifier = new IBk(); // Classifier here is K Nearest Neighbors
//        Classifier classifier = new SMO(); // Classifier here is Support Vector Machine
//        Classifier classifier = new J48(); // Classifier here is Decision Tree
//        Classifier classifier = new AdaBoostM1(); // Classifier here is Boosting/Ensemble Learner

        // train the model on training data
        classifier.buildClassifier(trainSet);
        // evaluate the model with testing data
        Evaluation eval = new Evaluation(trainSet);
        eval.evaluateModel(classifier, testSet);

        // Print the summary statistics
        System.out.println("** Classifier Evaluation with Datasets **");
        System.out.println(eval.toSummaryString());
        System.out.print(" the expression for the input data as per alogorithm is ");
        System.out.println(eval.toMatrixString());
        System.out.println(eval.toClassDetailsString());

        return classifier;
    }

    private static DatabaseLoader connectToDB() throws Exception{
        DatabaseLoader wekaDBLoader = new DatabaseLoader();
        wekaDBLoader.setUrl("jdbc:postgresql://cs297-instance.ckjk7kzjfhir.us-east-1.rds.amazonaws.com:5432/CS297_Project?createDatabaseIfNotExist=true&serverTimezone=UTC");
        wekaDBLoader.setUser("CS297P");
        wekaDBLoader.setPassword("GM755123637qs");
        wekaDBLoader.connectToDatabase();
        return wekaDBLoader;
    }

    // inits the instance field this.trainData
    private void getJobData(DatabaseLoader wekaDBLoader, String user_id) throws Exception {
        List<Map.Entry<String, Integer>> topKeywords = Recommendation.extractTopKey(user_id);
        List<String> keyphrases = new ArrayList<>();
        for (int i = 0; i < topKeywords.size(); i++){
            Map.Entry<String, Integer> entry = topKeywords.get(i);
            String keyphrase = entry.getKey();//.replaceAll(" ", "\\\\ ");
            keyphrases.add(keyphrase);
        }
        String[] phrases = new String[keyphrases.size()];
        phrases = keyphrases.toArray(phrases);
        String tsquery = PostgresWithJDBCConnection.sandwichStringList(phrases, "job_desc_tsv @@ phraseto_tsquery('", "')", " OR ");

        // in case keywords have not been generated for this user's favorite jobs
        if (tsquery.equals("")) {
            tsquery = "TRUE";
        }

        String mlQuery = "SELECT job_data.job_id, job_data.job_title, job_data.company, job_data.location, job_data.job_description, " +
                "CASE WHEN dumfav.job_id IS NOT NULL THEN 1 ELSE 0 END AS favorite " +
                "FROM job_data " +
                "LEFT JOIN " +
                "(SELECT job_data.job_id FROM job_data WHERE " + tsquery + " LIMIT 2000) AS dumfav " +
                "ON job_data.job_id = dumfav.job_id";
//        String mlQuery =
//                "SELECT job_data.job_title, job_data.company, job_data.location, " +
//                "CASE WHEN dumfav.job_id IS NOT NULL THEN 1 ELSE 0 END AS favorite " +
//                "FROM job_data " +
//                "LEFT JOIN " +
//                    "(SELECT history.jobid AS job_id FROM job_data INNER JOIN history ON history.jobid = job_data.job_id WHERE history.userid = '" + user_id + "' LIMIT 1000) AS dumfav " +
//                "ON job_data.job_id = dumfav.job_id";

        // query for database
        wekaDBLoader.setQuery(mlQuery);
        System.out.println("MLQuery: " + mlQuery);

        // get data and meta data
        Instances data = wekaDBLoader.getDataSet();
        System.out.println(data.attribute("favorite"));
        Instances dataFormat= wekaDBLoader.getStructure();

        // apply filter to cast type
        NumericToBinary numtobin = new NumericToBinary();
        numtobin.setInputFormat(dataFormat);
        data = NumericToBinary.useFilter(data, numtobin);

//        for (int i = 0; i < 30; i++) {
//            System.out.printf("%s %s %n", i, data.get(i));
//        }

        // set class index
        data.setClassIndex(data.numAttributes() - 1);

        // set instance field
        this.trainData = data;
    }

    private void printRecommendations(Classifier classifier) {
        List<Instance> recommendations = new ArrayList<>();
        Instances jobData = this.trainData;
        double num = 0;
        double den = jobData.numInstances() - 1;
        int start = 0;
        System.out.println("start = " + start + ", den = " + den + " numInsts = " + jobData.numInstances());
        for (int q = start; q < (start + den) % jobData.numInstances(); q++) {
            int index = (q) % jobData.numInstances();
            Instance instanceAtIndex = jobData.get(index);

            try {
                if (classifier.classifyInstance(instanceAtIndex) == instanceAtIndex.value(3)) {
                    num++;
                }
                else if (instanceAtIndex.value(3) == 0.0) {
                    // catch all false negatives; they are possible recommendations
                    recommendations.add(instanceAtIndex);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("ratio correct = " + num + "/" + den + " = " + num/den + ", number incorrect = " + (den - num));

        System.out.println("Recommendations: " + recommendations.size());
        for (Instance i : recommendations) {
            System.out.println(
                    "\t: " + i
            );
        }
    }

    public Classifier getClassifier(String user_id) {
        Classifier classifier = null;
        try {
            DatabaseLoader wekaDBLoader = connectToDB();
            getJobData(wekaDBLoader, user_id);
//            Instances dummyFavorites = getDummyFavorites(wekaDBLoader, user_id);
            if (this.trainData == null) {
                System.out.println("Instance obj from getJobData() or getDummyFavorites is null");
                return null;
            }

            classifier = dtCrossValidate(this.trainData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classifier;
    }

    public static void main(String[] args) {
        try {
            String user_id = "Andrew yu";
            FavoriteJobsPredictor predictor = new FavoriteJobsPredictor();
            Classifier classifier = predictor.getClassifier(user_id);
            predictor.printRecommendations(classifier);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}