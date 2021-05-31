package Database;

import java.sql.*;
import java.util.*;

public class Recommendation {
    public static Connection conn;
    public Recommendation(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(RDSConfig.URL);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(RDSConfig.URL);

        } catch (Exception e) {
            e.printStackTrace();
       }
        extractTopKey("diwen");
        close();
    }
    private static Set<String> getFavoriteItemIds(String userId){
        Set<String> result = new HashSet<>();
        if (conn == null) {
            return result;
        }

        try {
            String sql = "SELECT jobid from history where userid = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1,userId);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                String jobId = rs.getString("jobid");
                result.add(jobId);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
    public static List<Map.Entry<String, Integer>> extractTopKey(String user_id){
        Set<String> favJobs = getFavoriteItemIds(user_id);
        Map<String,Integer> map = new HashMap<>();
        for (String job_id: favJobs){
            addKey(job_id,map);
        }
        System.out.println(map.size());
        List<Map.Entry<String, Integer>> keywordList = new ArrayList<>(map.entrySet());
        keywordList.sort((Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) ->
                Integer.compare(e2.getValue(), e1.getValue()));

        // Cut down search list only top 5
        if (keywordList.size() > 5) {
            keywordList = keywordList.subList(0, 5);
        }
        for (int i = 0; i < keywordList.size(); i++){
            Map.Entry<String, Integer> entry = keywordList.get(i);
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());

        }

       return keywordList;


    }
    private static void addKey(String job_id,Map<String,Integer> map){
        if (conn == null) {
            return;
        }

        try {
            String sql = "SELECT keyword from keywords where job_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1,job_id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                String keyword = rs.getString("keyword");
                Integer count = map.getOrDefault(keyword,0);
                map.put(keyword,count + 1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



    }



    public static void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

