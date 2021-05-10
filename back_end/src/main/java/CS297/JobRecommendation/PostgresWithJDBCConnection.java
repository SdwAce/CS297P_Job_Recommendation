package CS297.JobRecommendation;
//package postgresql.jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgresWithJDBCConnection {
    public static final String ClientPassword = "adminuser";
    public static final String DATABASE_CLIENT = "postgres";
    public static final String DATABASE_URL = "jdbc:postgresql://127.0.0.1:5432/job_recommender";

    public static String constructSQLKeywordArray(String[] keywords, String surround) {
        String returnStr = "'"; // add open bracket
        for (int i = 0; i < keywords.length; i++) {
            returnStr = returnStr + keywords[i]; // add keyword surrounded by % and '
            if (i < keywords.length - 1) {
                returnStr = returnStr + " & "; // add comma, except for last element
            }
        }
        return returnStr + "'"; // add close bracket
    }

    public static void viewTable(Connection con, List <JobEntry> jobEntryList, String[] keywords) throws SQLException {
        String keywordSearchQuery = "SELECT * FROM jobs WHERE job_desc_tsv @@ to_tsquery(" + constructSQLKeywordArray(keywords, "") + ")";

        String keywordSortedQuery = "SELECT id, job_title, company, location, job_description, (array_length(x, 1) - 1) AS freq FROM jobs " +
                                    "CROSS JOIN LATERAL regexp_split_to_array(job_description, '\\m"+ keywords[0] +"\\M') as t(x) " +
                                    "where job_description like any (array['%"+ keywords[0] +"%']) order by freq desc";

        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(keywordSearchQuery);
            int i = 0;
            while (rs.next()) {
                if (i == 10)
                {
                    break;
                }

                String title = rs.getString("job_title");
                String company = rs.getString("company");
                String estSalary = rs.getString("estimated_salary");
                String location = rs.getString("location");
                String description = rs.getString("job_description");
                JobEntry jobEntry = new JobEntry();
                jobEntry.setTitle(title);
                jobEntry.setCompany(company);
                jobEntry.setEstimatedSalary(estSalary);
                jobEntry.setLocation(location);
                jobEntry.setDescription(description);
                jobEntryList.add(jobEntry);
                i++;
            }
            // print top 10 entries
//            int i = 0; // TODO delete
            for (JobEntry jobEntry : jobEntryList) {
                System.out.print("Title:: " + jobEntry.getTitle());
                System.out.print(" | Company:: " + jobEntry.getCompany());
                System.out.print(" | Location:: " + jobEntry.getLocation());
//                System.out.print(" | Description:: " + jobEntry.getDescription());
                System.out.println();
//                i++; // TODO delete
//                if (i == 10)
//                    break;
            }
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
//        return jobEntryList;
    }

    public static List <JobEntry> get_result(String [] keywords)
    {
        List <JobEntry> jobEntryList = new ArrayList < > ();
//        String[] keywords = {"apple"}; // FOR NOW: keyword search from static array
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_CLIENT, ClientPassword);) {
            viewTable(conn, jobEntryList, keywords);
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobEntryList;
    }

    public static void main(String[] args) {
        List <JobEntry> jobEntryList = new ArrayList < > ();
        String[] keywords = {"apple"}; // FOR NOW: keyword search from static array
        // establishes database connection
        // auto closes connection and preparedStatement
//        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_CLIENT, ClientPassword);) {
//            viewTable(conn, jobEntryList, keywords);
//        } catch (SQLException e) {
//            System.out.print(e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        get_result(keywords);
    }
}