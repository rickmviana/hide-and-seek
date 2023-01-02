package cz.ragy.hideandseek.managers;

import cz.ragy.hideandseek.HideAndSeek;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class MySQLManager {
    public static String databaseType = ConfigManager.config.getString("Database.Type").toLowerCase();
    public static String failedConnectToDB = ConfigManager.config.getString("Database.Failed");
    public static String ConnectedToDB = ConfigManager.config.getString("Database.Connected");

    private Connection connection;
    private String host, database, username, password;
    private int port;

    public MySQLManager(String host, int port, String database, String username, String password) {
        this.host = ConfigManager.config.getString("Database.host").toLowerCase();
        this.port = (int) ConfigManager.config.get("Database.port");
        this.database = ConfigManager.config.getString("Database.database").toLowerCase();
        this.username = ConfigManager.config.getString("Database.username").toLowerCase();
        this.password = (String) ConfigManager.config.get("Database.password");
    }

    public void connect() throws SQLException {
        if(databaseType.toLowerCase().equals("MySQL")) {
            Connection connection = null;
            try {

                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Bukkit.getLogger().warning("Please wait, connecting to the database.");
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
                Bukkit.getLogger().info(ConnectedToDB);
            } catch (SQLException e) {
                Bukkit.getLogger().warning(failedConnectToDB);
                Bukkit.getLogger().warning(String.valueOf(e));
            }
        } else if(databaseType.toLowerCase().equals("SQLite")) {
            String fileName = "database.db";
            Connection connection = null;
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:" + fileName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() throws SQLException {
        connection.close();
    }

    private void createTables() {
        try { PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS players (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, uuid VARCHAR(36) NOT NULL, first_join BIGINT NOT NULL)"); statement.executeUpdate(); } catch (SQLException e) { Bukkit.getLogger().warning("There is an error while create tables."); Bukkit.getLogger().warning(String.valueOf(e)); } }

    public void addPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        long firstJoin = System.currentTimeMillis();

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO players (id, uuid, first_join) VALUES (?, ?, ?)");
            statement.setInt(1, player.getEntityId());
            statement.setString(2, uuid.toString());
            statement.setLong(3, firstJoin);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}