package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TableCreation {
    public static void main(String[] args) throws ClassNotFoundException {
        getRemoteConnection();
    }
    private static void getRemoteConnection() throws ClassNotFoundException {
        try {
            Class.forName("org.postgresql.Driver");
            String url = RDSConfig.URL;
            Connection conn = DriverManager.getConnection(url);
            if (conn == null) {
                return;
            }
            Statement statement = conn.createStatement();
            String sql = "Create Table users ("
                         + "user_id varchar primary key,"
                         + "first_name varchar,"
                         + "last_name varchar,"
                         + "password  varchar"
                         + ")";
            statement.executeUpdate(sql);
            conn.close();
            System.out.println("Remote connection successful.");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


}
