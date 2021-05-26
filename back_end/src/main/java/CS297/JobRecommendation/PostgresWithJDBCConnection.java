package CS297.JobRecommendation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Database.DBOperations;
import Model.Job;

public class PostgresWithJDBCConnection {
    public static final String ClientPassword = "GM755123637qs";
    public static final String DATABASE_CLIENT = "CS297P";
    public static final String DATABASE_URL = "jdbc:postgresql://cs297-instance.ckjk7kzjfhir.us-east-1.rds.amazonaws.com:5432/CS297_Project?createDatabaseIfNotExist=true&serverTimezone=UTC";

//    public static final String DATABASE_CLIENT = "postgres"; // LOCAL SETTINGS
//    public static final String ClientPassword = "adminuser"; // LOCAL SETTINGS
//    public static final String DATABASE_URL = "jdbc:postgresql://127.0.0.1:5432/job_recommender"; // LOCAL SETTINGS

    // output: surround + keyword1 + between + keyword2 + between + ... + between + keywordN + surround
    private static String sandwichStringList(String[] keywords, String between, String surround) {
        String returnStr = surround; // add open surrounding param
        for (int i = 0; i < keywords.length; i++) {
            returnStr = returnStr + keywords[i]; // add keyword
            if (i < keywords.length - 1) {
                returnStr = returnStr + between; // add between param, except for last element
            }
        }
        return returnStr + surround; // add close surrounding param
    }

    // output:
    // before + keyword1 + after + between +
    // before + keyword2 + after + between +
    // ... + between +
    // before + keywordN + after
    private static String sandwichStringList(String[] keywords, String before, String after, String between) {
        String returnStr = "";
        for (int i = 0; i < keywords.length; i++) {
            returnStr = returnStr + before + keywords[i] + after; // add before + keyword + after
            if (i < keywords.length - 1) {
                returnStr = returnStr + between; // add between param, except for last element
            }
        }
        return returnStr;
    }

    public static void searchByFeature(Connection conn,
                                       List <Job> jobEntryList,
                                       String[] keywords,
                                       String[] fields,
                                       String user_id) throws SQLException {

        // create SQL conditional statements according to which text-boxes were filled in
        ArrayList<String> whereConditions = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            // depending on field to be searched; change the SQL condition
            if (fields[i].equals("skill")) {
                // searching job_description
                String descriptionCond = "job_desc_tsv @@ to_tsquery(" +
                        sandwichStringList(keywords[i].split(" "), "|", "'") +
                        ")";
                whereConditions.add(descriptionCond);
            } else {
                // searching job_title, company, or location
                String shortColumnCond = fields[i] + " ~* " +
                        sandwichStringList(keywords[i].split(" "), "|", "'");
                whereConditions.add(shortColumnCond);
            }
        }

        // combine all conditions into two sub-statements; a mathematical expression and a WHERE clause
        String[] conds = whereConditions.toArray(whereConditions.toArray(new String[fields.length]));
        String rank = "(" + sandwichStringList(conds,"CASE WHEN ", " THEN 1 ELSE 0 END", " + ") + ") AS rank";
        String whereStatement = "WHERE " + sandwichStringList(conds, " OR ", "");

        // SQL query for database
        String keywordSearchQuery = "SELECT job_data.*, " +
                rank +
                " FROM job_data " +
                whereStatement +
                " ORDER BY rank DESC LIMIT 100";

//        // if user_id is not empty string; mark the jobs from user's favorite list
//        if (!(user_id.equals(""))) {
//            keywordSearchQuery = "SELECT job_data.*, " +
////                    "(CASE WHEN history.userid = " + user_id + " THEN 1 ELSE 0 END) AS favorite, " +
//                    rank +
//                    " FROM job_data " +
//                    whereStatement +
//                    " ORDER BY rank DESC ";
//        }

        System.out.println("Query: " + keywordSearchQuery);

        // operate on database
        try (Statement stmt = conn.createStatement()) {
            // query database
            ResultSet rs = stmt.executeQuery(keywordSearchQuery);

            // retrieve results from query
            int i = 0;
            DBOperations db = new DBOperations();
            while (rs.next()) {
                if (i == 20) {
                    break;
                }
                i++;
                // retrieve data from query
                String job_id = rs.getString("job_id");
                String title = rs.getString("job_title");
                String company = rs.getString("company");
                String location = rs.getString("location");
                String description = rs.getString("job_description");

                // populate job object with query results
                Job job = new Job();
                job.setJob_title(title);
                job.setJob_id(job_id);
                job.setCompany(company);
                job.setLocation(location);
                job.setJob_description(description);

                // if job is a favorite

                if (db.checkExist(job_id, user_id) != null) {
                    job.setFavorite(true);
                }

                // add job object to return object
                jobEntryList.add(job);
            }
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
    }

    public static List <Job> get_result(String[] keywords, String[] fields, String user_id)
    {
        // list to be populated with keyword search results
        List <Job> jobEntryList = new ArrayList < > ();
        // establish database connection
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_CLIENT, ClientPassword);) {
            // search database by keywords in specific columns (i.e. fields)
            // CAUTION: mutates jobEntryList
            searchByFeature(conn, jobEntryList, keywords, fields, user_id);
            conn.close();
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobEntryList;
    }

    public static void main(String[] args) {
        List <JobEntry> jobEntryList = new ArrayList < > ();
        String user_id = "";
        String[] keywords = {"senior", "CA", "java", "apple google"}; // FOR NOW: keyword search from static array
        String[] fields = {"job_title", "location", "skill", "company"}; // FOR NOW: keyword search from static array
        List<Job> jobList = get_result(keywords, fields, user_id);
        for (int i = 0; i < jobList.size(); i++) {
            System.out.println(jobList.get(i).getJob_title() + "\t|\t"
                    + jobList.get(i).getCompany() + "\t|\t"
                    + jobList.get(i).getLocation());
        }
        System.out.println("number of jobs found: " + jobList.size());
    }
}