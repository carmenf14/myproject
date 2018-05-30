package ro.cfm.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Carmen on 5/26/2018.
 */
public class ConnectionHelper {
    private static ConnectionHelper ourInstance = null;
    private Connection connection;

    public static ConnectionHelper getInstance() {
        if(ourInstance==null) {

            ourInstance=new ConnectionHelper();
            try {

                Class.forName("org.postgresql.Driver");
                Properties props=new Properties();
                props.setProperty("user","postgres");
                props.setProperty("password","parola");
                ourInstance.connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/myproject",props);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return ourInstance;
    }

    private ConnectionHelper() {
    }

    public Connection getConnection() {
        return connection;
    }
}
