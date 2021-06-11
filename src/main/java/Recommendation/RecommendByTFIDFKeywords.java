package Recommendation;

import Database.Recommendation;
import Model.Job;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.DatabaseLoader;

import java.sql.*;
import java.util.*;

public class RecommendByTFIDFKeywords {

    private static List<Double> multiKWSearch(Connection conn,
                                              List<Job> jobEntryList,
                                              List<String> keywords,
                                              List<Integer> occurrences,
                                              List<Double> ranks)  throws SQLException {
        String tsquery = "to_tsquery('";
        for (int i = 0; i < keywords.size(); i++) {
            tsquery = tsquery + keywords.get(i);
            if (i != keywords.size() - 1) {
                tsquery = tsquery + "|";
            }
        }
        tsquery = tsquery + "')";

        // SQL query to get job recommendations from TF-IDF keywords
        String getFavoritesQuery = "SELECT job_data.*, " +
                "ts_rank(document_with_weights, tsquery, 32) AS rank " +
                "FROM job_data, " + tsquery + " tsquery " +
                "WHERE document_with_weights @@ tsquery " +
                "ORDER BY rank DESC LIMIT 100";

        System.out.println("Query: " + getFavoritesQuery);

        // operate on database
        try (Statement stmt = conn.createStatement()) {
            // query database
            ResultSet rs = stmt.executeQuery(getFavoritesQuery);

            // retrieve results from query
            while (rs.next()) {
                // retrieve data from query
                String job_id = rs.getString("job_id");
                String title = rs.getString("job_title");
                String company = rs.getString("company");
                String location = rs.getString("location");
                String description = rs.getString("job_description");
                Double rank = rs.getDouble("rank");

                // populate job object with query results
                Job job = new Job();
                job.setJob_title(title);
                job.setJob_id(job_id);
                job.setCompany(company);
                job.setLocation(location);
                job.setJob_description(description);

                // add job object to return object
                jobEntryList.add(job);
                ranks.add(rank);
            }
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }

        return ranks;
    }

    private static void getJobsRecommendedByTFIDF(String user_id,
                                                  List<Job> jobEntryList,
                                                  List<Double> ranks) {
        Recommendation recommender = new Recommendation();

        List<Map.Entry<String, Integer>> topKeywords = Recommendation.extractTopKey(user_id);

        System.out.println("topKeywords.size = " + topKeywords.size());

        List<String> keywords = new ArrayList<>();
        List<Integer> occurrences = new ArrayList<>();
        for (int i = 0; i < topKeywords.size(); i++){
            Map.Entry<String, Integer> entry = topKeywords.get(i);
            String[] splitKeywords = entry.getKey().split(" ");
            for (String keyword : splitKeywords) {
                keywords.add(keyword);
            }
            occurrences.add(entry.getValue());
        }

        try {
            multiKWSearch(recommender.conn,
                    jobEntryList,
                    keywords,
                    occurrences,
                    ranks);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static List<Job> getRecommendations(String user_id,
                                               List<Job> jobEntryList,
                                               List<Double> ranks,
                                               Double mlWeight,
                                               Double tsWeight) {
        getJobsRecommendedByTFIDF(user_id, jobEntryList, ranks);
        FavoriteJobsPredictor predictor = new FavoriteJobsPredictor();
        Classifier classifier = predictor.getClassifier(user_id);

        // run TFIDF recommendations through MLClassifier to augment ranks
        for (int i = 0; i < jobEntryList.size(); i++) {
            Job j = jobEntryList.get(i);
            Double tsRank = ranks.get(i);
            double mlScore = 0.0;
            try { mlScore = classifier.classifyInstance(predictor.trainData.get(i)); } catch (Exception e) { e.printStackTrace(); }
            double jobRank = (tsRank * tsWeight) + (mlScore * mlWeight);
//            System.out.println("tsRank = " + tsRank + ", mlScore = " + mlScore + ", jobRank = " + jobRank);
            ranks.set(i, jobRank);
        }

        // sort jobEntryList by their rank stored in ranks list
        Collections.sort(jobEntryList, Comparator.comparingDouble(ranks::indexOf).reversed());
        Collections.sort(ranks, Comparator.comparingDouble(Double::doubleValue).reversed());
        return jobEntryList;
    }

    public static void main(String[] args) {
        String user_id = "diwen";
        List<Job> jobEntryList = new ArrayList<>();
        List<Double> ranks = new ArrayList<>();
        Double mlWeight = 0.3;
        Double tsWeight = 1.0;

        getRecommendations(user_id, jobEntryList, ranks, mlWeight, tsWeight);

        for (int i = 0; i < jobEntryList.size(); i++) {
            Job j = jobEntryList.get(i);
            System.out.println("Job " + i + ": " +
                    j.getJob_title() + " | " +
                    j.getCompany() + " | " +
                    j.getLocation() + " | " +
                    ranks.get(i));
        }
    }


}