package Database;

import java.sql.*;

import Model.History;
import Model.HistoryKey;
import Model.Job;
import Model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class DBOperations {
    public Connection conn;
    private static ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
    private static SessionFactory sessionFactory = (SessionFactory) context.getBean("sessionFactory");

    //private static Session session; //create factory session based on configurations



    public static void main(String[] args){
        //createTable();
        User user = new User();
        user.setUser_id("sundiwen");
        user.setFirstName("hhh");
        user.setLastName("hhh");
        user.setPassword("GM755123637qs");
        register(user);
        //setFavorite("2222",new Job("Software_Engineer","Microsoft","Irvine"));
    }

    //initialize the connection whenever the object is created
    public DBOperations() {
        try {
            Class.forName("org.postgresql.Driver").newInstance();
            String url = RDSConfig.URL;
            conn = DriverManager.getConnection(url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createTable(){
        Session session = sessionFactory.openSession();

        session.beginTransaction(); //creating transaction object
         //Job job = session.get(Job.class,4);

         //if (job != null){
             //System.out.println("deleted!");
             //session.delete(job);
         //}

         Job job2 = new Job();
         //job2.setId(5);
        job2.setTitle("mdzz");
        job2.setLocation("cndy");
        job2.setDescription("qnmd");
        session.save(job2);
        session.getTransaction().commit();
        //session.refresh();
        session.close();
        System.out.println("successfully saved");
   }
    private boolean checkConnection(){
        if (conn == null) {
            System.err.println("DB connection failed");
            return false;
        }
        return true;
    }

    public static boolean register(User user) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User getUesr = (User) session.get(User.class,user.getUser_id());
        if(getUesr != null){
            return false;
        }
        session.save(user);
        session.getTransaction().commit();
        //session.refresh();
        session.close();
        System.out.println("successfully saved");
        return true;

    }
    public boolean login(String userId, String password){
        if (!checkConnection()){
            return false;
        }
        String comm = "SELECT user_id FROM users WHERE user_id = ? AND password = ?";
        try{
            PreparedStatement statement = conn.prepareStatement(comm);
            statement.setString(1, userId);
            statement.setString(2, password);
            ResultSet set = statement.executeQuery();
            if (set.next()){
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static void setFavorite(String userid, Job job){
        saveJob(job);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        History history = new History();
        history.setKey(new HistoryKey(userid, job.getId()));
        session.save(history);
        session.getTransaction().commit();
        session.close();

    }

    public static void unsetFavorite(String userid, Job job ){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        History history = (History) session.get(History.class,new HistoryKey(userid,job.getId()));
        if (history != null){
            session.delete(history);
            session.getTransaction().commit();
        }
        session.close();

    }

    public static void saveJob(Job job){
        Session session = sessionFactory.openSession();
        session.beginTransaction(); //creating transaction object
        session.save(job);
        session.getTransaction().commit();
        session.close();
   }





    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
