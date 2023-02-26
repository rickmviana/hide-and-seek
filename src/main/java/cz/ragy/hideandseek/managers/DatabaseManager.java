package cz.ragy.hideandseek.managers;

import cz.ragy.hideandseek.managers.ConfigManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    public static String databaseType = (String) ConfigManager.config.get("Database.Type");
    public String host = (String) ConfigManager.config.get("Database.host");
    public String port = (String) ConfigManager.config.get("Database.port");
    public String database = (String) ConfigManager.config.get("Database.database");
    public String username = (String) ConfigManager.config.get("Database.username");
    public String password = (String) ConfigManager.config.get("Database.password");
    public static Connection connection;

    public DatabaseManager() {
        if(databaseType.toLowerCase().equals("mysql")) {
            Connection connection = null;
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if(databaseType.toLowerCase().equals("sqlite")) {
            String fileName = "database.db";
            Connection connection = null;
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:" + fileName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
