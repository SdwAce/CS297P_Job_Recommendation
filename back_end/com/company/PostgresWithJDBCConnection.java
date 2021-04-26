package com.company;
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
        String returnStr = "["; // add open bracket
        for (int i = 0; i < keywords.length; i++) {
            returnStr = returnStr + "'" + surround + keywords[i] + surround + "'"; // add keyword surrounded by % and '
            if (i < keywords.length - 1) {
                returnStr = returnStr + ","; // add comma, except for last element
            }
        }
        return returnStr + "]"; // add close bracket
    }

    public static void keywordSearch(Connection con, List <JobEntry> jobEntryList, String[] keywords, String field) throws SQLException {
        String keywordSearchQuery = "SELECT * FROM jobs WHERE " + field +
                " LIKE ANY (ARRAY" + constructSQLKeywordArray(keywords, "%") + ")";
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(keywordSearchQuery);
            while (rs.next()) {
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
            }

            // print top 10 entries
            int i = 0; // TODO delete
            for (JobEntry jobEntry : jobEntryList) {
                System.out.print("Title:: " + jobEntry.getTitle());
                System.out.print(" | Company:: " + jobEntry.getCompany());
                System.out.print(" | Location:: " + jobEntry.getLocation());
//                System.out.print(" | Description:: " + jobEntry.getDescription());
                System.out.println();
                i++; // TODO delete
                if (i == 10)
                    break;
            }
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
    }

    public static void main(String[] args) {
        List <JobEntry> jobEntryList = new ArrayList < > ();
        String[] keywords = {"Cupertino"}; // FOR NOW: keyword search from static array
        String field = "Location";
        // establishes database connection
        // auto closes connection and preparedStatement
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_CLIENT, ClientPassword);) {
            keywordSearch(conn, jobEntryList, keywords, field);
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}