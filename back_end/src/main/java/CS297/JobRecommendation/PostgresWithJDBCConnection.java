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
    public static final String ClientPassword = "GM755123637qs";
    public static final String DATABASE_CLIENT = "CS297P";
    public static final String DATABASE_URL = "jdbc:postgresql://cs297-instance.ckjk7kzjfhir.us-east-1.rds.amazonaws.com:5432/CS297_Project?createDatabaseIfNotExist=true&serverTimezone=UTC";

    public static String constructSQLKeywordArray(String[] keywords, String surround) {
        String returnStr = "'"; // add open bracket
        for (int i = 0; i < keywords.length; i++) {
            returnStr = returnStr + keywords[i]; // add keyword surrounded by % and '
            if (i < keywords.length - 1) {
                returnStr = returnStr + " & "; // add comma, except for last job_dataelement
            }
        }
        return returnStr + "'"; // add close bracket
    }

    public static void viewTable(Connection con, List <JobEntry> jobEntryList, String[] keywords) throws SQLException {
        String keywordSearchQuery = "SELECT * FROM job_data WHERE job_desc_tsv @@ to_tsquery(" + constructSQLKeywordArray(keywords, "") + ")";

        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(keywordSearchQuery);
            int i = 0;
            while (rs.next()) {
                if (i == 10)
                {
                    break;
                }
                String job_id = rs.getString("job_id");
                String title = rs.getString("job_title");
                String company = rs.getString("company");
                String location = rs.getString("location");
                String description = rs.getString("job_description");
                JobEntry jobEntry = new JobEntry();
                jobEntry.setJob_id(job_id);
                jobEntry.setTitle(title);
                jobEntry.setCompany(company);
                jobEntry.setLocation(location);
                jobEntry.setDescription(description);
                jobEntryList.add(jobEntry);
                i++;
            }
            // print top 10 entries
//            int i = 0; // TODO delete
            for (JobEntry jobEntry : jobEntryList) {
                System.out.print("ID:: " + jobEntry.getJob_id());
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