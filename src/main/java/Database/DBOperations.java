package Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;


import Model.*;
import org.hibernate.Session;


public class DBOperations {

    public static  Connection conn;


    public static void main(String[] args) throws IOException, InterruptedException, SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
//        try {
//           Class.forName("org.postgresql.Driver").newInstance();
//           conn = DriverManager.getConnection(RDSConfig.URL);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //saveProfile("jake su","sundiwen163@uci.edu","Computer Science","graduate",false);
//        System.out.println(checkExist("05f45289-57a2-4edf-a75a-ee629a1bef9","diwen"));
//        //context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
//        //getParameters("New York, NY");
//        //setFavorite("2222",new Job("Software_Engineer","Microsoft","Irvine"));
//        //searchNearJobs(Double.valueOf(-117),Double.valueOf(34));
//        //showHistory("diwen");
//        //sessionFactory =  (SessionFactory) context.getBean("sessionFactory");
//        //setFavorite("diwen","f8f2458c-05b7-4ab6-a424-be6a371f1e96");
//        //checkProfileExist("diwen");
//        //saveProfile("diwen","diwens3@uci.edu","Computer Science","undergraduate",false);
        login("diwen","12345");
          close();

    }

    //initialize the connection whenever the object is created
    public DBOperations() {
        try {
            Class.forName("org.postgresql.Driver").newInstance();
            conn = DriverManager.getConnection(RDSConfig.URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //context =
        //SessionFactory sessionFactory = (SessionFactory) context.getBean("sessionFactory");
    }

    //method for ing lat and lon
//    public static void update(){
//        try (Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
//            ResultSet result = stmt.executeQuery("SELECT * FROM job_data");
//            while (result.next()) {
//                String address = result.getString("location");
//                try {
//                    Double[] parameters = getParameters(address);
//                    //System.out.println(parameters[0]);
//                    if (parameters[0] != null && parameters[1] != null){
//                        result.updateDouble("lon", parameters[0]);
//                        result.updateDouble("lat", parameters[1]);
//                        result.updateRow();
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }catch (SQLException e) {
//
//        }
//    }
    public static String[] login(String userId, String password){

        String sql = "SELECT * FROM users where user_id = ? and password = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userId);
            statement.setString(2, password);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new String[]{"Success",rs.getString("firstname")};
                }
            }
        }catch (SQLException throwables)  {
            throwables.printStackTrace();
        }
        return new String[]{"Failed",null};
    }
    //check the existence of history
    private static boolean checkHistoryExist(String job_id, String user_id){

        if (conn == null) {
            return false;
        }
        String sql = "SELECT * FROM history where jobid = ? and userid = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, job_id);
            statement.setString(2, user_id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        }catch (SQLException throwables)  {
            throwables.printStackTrace();
        }
        return false;

    }

    public static List<Job> searchNearJobs(String user_id,Double lon, Double lat) {
        List<Job> result = new ArrayList<>();
        if (conn == null) {
            return result;
        }
        String sql = "SELECT * FROM job_data where lat between ? and ? and lon between ? and ? LIMIT 10";
        try{
               PreparedStatement statement = conn.prepareStatement(sql);
               statement.setDouble(1, lat - 0.3);
               statement.setDouble(2, lat + 0.3);
               statement.setDouble(3, lon - 0.3);
               statement.setDouble(4, lon + 0.3);
               try (ResultSet rs = statement.executeQuery()) {
                    while(rs.next()) {
                        Job jobtoAdd = new Job();
                        result.add(jobtoAdd);
                        jobtoAdd.setJob_id(rs.getString("job_id"));
                        jobtoAdd.setLocation(rs.getString("Location"));
                        jobtoAdd.setCompany(rs.getString("company"));
                        jobtoAdd.setJob_description(rs.getString("job_description"));
                        jobtoAdd.setJob_title(rs.getString("job_title"));
                        jobtoAdd.setLat(rs.getDouble("lat"));
                        jobtoAdd.setLon(rs.getDouble("lon"));
                        if(checkHistoryExist(jobtoAdd.getJob_id(),user_id) == true){
                            jobtoAdd.setFavorite(true);
                        }

                    }
                }
        } catch (SQLException throwables)  {
            throwables.printStackTrace();
        }


        return result;

    }

//    public static Double[] getParameters(String address) throws IOException {
//        Double[] solution = new Double[2];
//        String url = "http://api.positionstack.com/v1/forward?access_key=c2bd376cfcf3894b4333edd277c28994&query=" + address;
//        //get lat and lon based on string
//        url = url.replaceAll(" ","%20");
//        URL obj = new URL(url);
//        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//        // optional default is GET
//        con.setRequestMethod("GET");
//        //add request header
//        int responseCode = con.getResponseCode();
//        if (responseCode != 200){
//            return solution;
//        }
//        //System.out.println("\nSending 'GET' request to URL : " + url);
//        //System.out.println("Response Code : " + responseCode);
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();
//        //System.out.println(response.toString());
//        JSONObject json = new JSONObject(response.toString());
//
//        JSONArray jsonArray = json.getJSONArray("data");
//        JSONObject myJsonObject = new JSONObject();
//        if (jsonArray.length() == 0){
//            return solution;
//        }
//        try {
//            myJsonObject = jsonArray.getJSONObject(0);
//            solution[0] = Double.valueOf(myJsonObject.optString("longitude"));
//            solution[1] = Double.valueOf(myJsonObject.optString("latitude"));
//        }catch (org.json.JSONException e){
//        }
//        return solution;
//
//    }

    public static List<Job> showHistory(String user__id)  {
        List<Job> result = new ArrayList<>();
        if (conn == null) {
           return result;
        }
        String sql = "SELECT * FROM job_data where job_id in (SELECT jobid FROM history where userid = ?)";
        try {
           PreparedStatement statement = conn.prepareStatement(sql);
           statement.setString(1, user__id);
           try (ResultSet rs = statement.executeQuery()) {
               while (rs.next()) {
                   Job jobtoAdd = new Job();
                   result.add(jobtoAdd);
                   jobtoAdd.setJob_id(rs.getString("job_id"));
                   jobtoAdd.setLocation(rs.getString("location"));
                   jobtoAdd.setCompany(rs.getString("company"));
                   jobtoAdd.setJob_description(rs.getString("job_description"));
                   jobtoAdd.setJob_title(rs.getString("job_title"));
                   jobtoAdd.setLat(rs.getDouble("lat"));
                   jobtoAdd.setLon(rs.getDouble("lon"));
               }
           }
        } catch (SQLException e) {
           e.printStackTrace();
        }
        return result;
    }

    //check profile exist or not and return profile object
    public static Profile checkProfileExist(String user_id) {
        Profile profile = null;
        if (conn == null) {
            return profile;
        }
        try {
            //in (SELECT jobid FROM history where userid = ?)
            String sql = "SELECT * FROM profile where user_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, user_id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                profile = new Profile();
                profile.setUser_id(rs.getString("user_id"));
                profile.setEmail(rs.getString("uci_email"));
                profile.setMajor(rs.getString("major"));
                profile.setLevel(rs.getString("education_level"));
                profile.setFind_Job(rs.getBoolean("found_job"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return profile;
    }
    //check profile exist or not and update if necessary
    public static void unsetFavorite(String userid, String job_id){
        if (conn == null) {
            return;
        }
        try  {
            String sql = "delete FROM history where jobid = ? and userid = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, job_id);
            statement.setString(2, userid);
            statement.executeQuery();
            return;

        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return;
    }

    static boolean checkProfileExist(String user_id,String email, String major, String level, boolean found_job) {
        if (conn == null) {
            return false;
        }
        try (Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            String sql = "select * from profile where user_id = " +"'" + user_id + "'" ;
            ResultSet result = statement.executeQuery(sql);
            if (result.next()) {
                result.updateString("uci_email", email);
                result.updateString("major", major);
                result.updateString("education_level", level);
                result.updateBoolean("found_job", found_job);
                result.updateRow();
                return true;
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static void saveProfile(String user_id, String email, String major, String level, boolean found_job){
        if (conn == null) {
            return;
        }
        try {
            //if exist then update
            boolean exist = checkProfileExist(user_id,email,major,level,found_job);
            PreparedStatement statement;
            if (exist  == true) {
                return;
            }else{//if not exist then insert
                String sql2 = "INSERT INTO profile (user_id,uci_email,major,education_level,found_job) VALUES (?, ?, ?, ?, ?)";
                statement = conn.prepareStatement(sql2);
                statement.setString(1, user_id);
                statement.setString(2, email);
                statement.setString(3, major);
                statement.setString(4, level);
                statement.setBoolean(5, found_job);
                statement.executeUpdate();

            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }




    public static void close(){
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}