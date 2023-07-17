package dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionManager {
    private final String url;
    private final Properties properties;

    public DatabaseConnectionManager(String host, String port, String serviceName,
                                     String username, String password, boolean isOracle) {
        if (isOracle) {
            this.url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + serviceName;
        } else {
            this.url = "jdbc:mysql://" + host + ":" + port + "/" + serviceName;
        }
        this.properties = new Properties();
        this.properties.setProperty("user", username);
        this.properties.setProperty("password", password);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.url, this.properties);
    }
}
