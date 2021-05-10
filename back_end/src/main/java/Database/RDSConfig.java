package Database;

public class RDSConfig {
    private static final String instance = "cs297-instance.ckjk7kzjfhir.us-east-1.rds.amazonaws.com";
    private static final String port = "5432";
    private static final String db_name = "CS297_Project";
    private static final String user_name = "CS297P";
    private static final String password = "GM755123637qs";
    public static final String URL = "jdbc:postgresql://" + instance + ":" +port + "/" + db_name + "?"+ "user=" + user_name + "&" + "password=" + password;
}
