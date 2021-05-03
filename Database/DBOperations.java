package Database;

import java.sql.*;

import Model.Job;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class DBOperations {
    public Connection conn;
    private static ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
    private static SessionFactory sessionFactory = (SessionFactory) context.getBean("sessionFactory");
    //private static Session session; //create factory session based on configurations



    //public static void main(String[] args){

        //createTable();
    //}

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
         Job job = session.get(Job.class,4);

         if (job != null){
             System.out.println("deleted!");
             session.delete(job);
         }
         Job job2 = new Job();
         job2.setId(5);
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




    public boolean register(String userId, String password, String firstName, String lastName) {
        if (!checkConnection()){
            return false;
        }
        String comm = "INSERT INTO users VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement statement = conn.prepareStatement(comm);
            statement.setString(1, userId);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, password);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

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
