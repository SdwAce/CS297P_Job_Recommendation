package Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.sql.*;
import java.util.*;

//import External.MonkeyLearnClient;
import Model.*;
//import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
//import org.hibernate.search.FullTextSession;
//import org.hibernate.search.query.dsl.QueryBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class DBOperations {

    public static  Connection conn;
    private static ApplicationContext context;
    private static SessionFactory sessionFactory;
    private static Set<String> set = new HashSet<>(Arrays.asList("data","ai","software","web","ios","android","business","machine learn","natural language","system","ui"));

 public static void main(String[] args) throws IOException, InterruptedException, SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {


        context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        //getParameters("New York, NY");
        //setFavorite("2222",new Job("Software_Engineer","Microsoft","Irvine"));
        //searchNearJobs(Double.valueOf(-117),Double.valueOf(34));
        //showHistory("diwen");
        sessionFactory =  (SessionFactory) context.getBean("sessionFactory");
        setFavorite("diwen","f8f2458c-05b7-4ab6-a424-be6a371f1e96");
        close();

    }

    //initialize the connection whenever the object is created
    public DBOperations() {
        context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        sessionFactory = (SessionFactory) context.getBean("sessionFactory");
        //context =
        //SessionFactory sessionFactory = (SessionFactory) context.getBean("sessionFactory");
    }

    public static boolean register(User user) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User getUser = (User) session.get(User.class,user.getUser_id());
        if(getUser != null){
            return false;
        }
        session.save(user);
        session.getTransaction().commit();
        session.close();
        return true;

    }
    public String[] login(String userId, String password){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User getUser = (User) session.get(User.class,userId);
        if (getUser == null || !getUser.getPassword().equals(password)){
            session.close();
            return new String[]{"Failed",null};
        }
        session.close();
        return new String[]{"Success",getUser.getFirstName()};
    }




   public String getFirstName(String user_id){
       Session session = sessionFactory.openSession();
       session.beginTransaction(); //creating transaction object
       User user = (User) session.get(User.class, user_id);
        try {
            String firstname = user.getFirstName();
        }catch(NullPointerException e){
            System.out.println(e.getMessage());
        }
        session.close();
        return user.getFirstName();
    }
    //method for update lat and lon
    public static void update(){
        try (Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            ResultSet result = stmt.executeQuery("SELECT * FROM job_data");
            while (result.next()) {
                String address = result.getString("location");
                try {
                    Double[] parameters = getParameters(address);
                    //System.out.println(parameters[0]);
                if (parameters[0] != null && parameters[1] != null){
                    result.updateDouble("lon", parameters[0]);
                    result.updateDouble("lat", parameters[1]);
                    result.updateRow();
                }

                } catch (IOException e) {
                    e.printStackTrace();
                }
             }
        }catch (SQLException e) {

        }
    }

    public static History checkExist(String job_id,String user_id ){
        Session session = sessionFactory.openSession();
        History history = session.get(History.class,new HistoryKey(user_id,job_id));
        session.close();
        return history;
    }

    public static List<Job> searchNearJobs(String user_id,Double lon, Double lat){
        List<Job> result = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(RDSConfig.URL);

            if (conn == null) {
                return result;
            }
            String sql = "SELECT * FROM job_data where lat between ? and ? and lon between ? and ? LIMIT 10";
            try {
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
                        if(checkExist(jobtoAdd.getJob_id(),user_id) != null){
                            jobtoAdd.setFavorite(true);
                        }

                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                conn.close();
                return result;
            }
            conn.close();
            //System.out.println("Import done successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    public static Double[] getParameters(String address) throws IOException {
        Double[] solution = new Double[2];
        String url = "http://api.positionstack.com/v1/forward?access_key=c2bd376cfcf3894b4333edd277c28994&query=" + address;
        //get lat and lon based on string
        url = url.replaceAll(" ","%20");
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        int responseCode = con.getResponseCode();
        if (responseCode != 200){
            return solution;
        }
        //System.out.println("\nSending 'GET' request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //System.out.println(response.toString());
        JSONObject json = new JSONObject(response.toString());

        JSONArray jsonArray = json.getJSONArray("data");
        JSONObject myJsonObject = new JSONObject();
        if (jsonArray.length() == 0){
            return solution;
        }
        try {
            myJsonObject = jsonArray.getJSONObject(0);
            solution[0] = Double.valueOf(myJsonObject.optString("longitude"));
            solution[1] = Double.valueOf(myJsonObject.optString("latitude"));
        }catch (org.json.JSONException e){
        }
        return solution;

   }

   public static List<Job> showHistory(String user__id) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
       List<Job> result = new ArrayList<>();
       try {
           Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
           conn = DriverManager.getConnection(RDSConfig.URL);

           if (conn == null) {
               return result;
           }

               //in (SELECT jobid FROM history where userid = ?)
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
           }


           //System.out.println("Import done successfully");
       } catch (Exception e) {
           e.printStackTrace();
       }
       conn.close();
       return result;
   }
    public static void setFavorite(String userid, String job_id){
        //saveJob(job);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        History history = new History();
        history.setKey(new HistoryKey(userid, job_id));
        session.saveOrUpdate(history);
        session.getTransaction().commit();
        session.close();
//        saveKeyword(TFIDF(job_id),job_id);
    }

    public static void unsetFavorite(String userid, String job_id){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        History history = checkExist(job_id,userid);

        if (history != null){
            System.out.println("mdzz");
            session.delete(history);
            session.getTransaction().commit();
        }
        session.close();

    }

//    private static void saveKeyword(List<List<String>> keyList,String job_id){
//        Session session = sessionFactory.openSession();
//        session.beginTransaction();
//        for (String key : keyList.get(0)){
//            KeyWordsKey keykey= new KeyWordsKey(job_id,key);
//            Keywords getKeyword = (Keywords) session.get(Keywords.class,keykey);
//            if(getKeyword == null){
//                Keywords keyword = new Keywords();
//                keyword.setKey(keykey);
//                session.saveOrUpdate(keyword);
//            }
//        }
//        session.getTransaction().commit();
//        session.close();
//        return;
//    }
//
//    public static List<List<String>> TFIDF(String job_id){
//        Session session = sessionFactory.openSession();
//        session.beginTransaction();
//        Job job = (Job) session.get(Job.class,job_id);
//        List<List<String>> keywords = new ArrayList<>();
//        try {
//            String description = job.getJob_description();
//            MonkeyLearnClient client = new MonkeyLearnClient();
//            keywords = client.extract(description);
//        }catch(NullPointerException | IOException e){
//            System.out.println(e.getMessage());
//        }
//        if (keywords.size() == 0){
//            return keywords;
//        }
//        List<String> keyList = keywords.get(0);
//        System.out.println(keyList.size());
//        for(int i = 0; i < keyList.size(); i++){
//            String keyword = keyList.get(i);
//            //abbreviate key words
//            for (String str : set){
//                if (keyword.contains(str)){
//                    keyList.set(i,str);
//                    break;
//                };
//            }
//        }
//        for(String str : keywords.get(0)){
//            System.out.println(str);
//        }
//        session.close();
//
//        return keywords;
//    }

    public static void close(){
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(sessionFactory != null){
            sessionFactory.close();
        }
    }


}
