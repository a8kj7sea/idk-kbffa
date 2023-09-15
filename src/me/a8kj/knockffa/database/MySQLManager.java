package me.a8kj.knockffa.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class MySQLManager {

    @Getter
    @Setter
    private String host;
    @Getter
    @Setter
    private String database;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private Connection connection;

    private static final String NOT_CONNECTED_MESSAGE = "Not connected to the database.";

    public MySQLManager(@NonNull String host, @NonNull String database, @NonNull String username, @NonNull String password) {
        setHost(host);
        setDatabase(database);
        setUsername(username);
        setPassword(password);
    }

    public MySQLManager() {
    }

    public void connect() {
        String jdbcUrl = "jdbc:mysql://" + host + ":" + "3306" + "/" + database;

        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            Bukkit.getServer().getLogger().info("Connected to MySQL database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                Bukkit.getServer().getLogger().info("Disconnected from MySQL database.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void createTable(String tableName, String tableStructure) {
        if (!isConnected()) {
            Bukkit.getServer().getLogger().warning(NOT_CONNECTED_MESSAGE);
            return;
        }

        String createTableQuery = "create table if not exists " + tableName + " (" + tableStructure + ");";

        try (PreparedStatement statement = connection.prepareStatement(createTableQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet query(String sql) {
        if (!isConnected()) {
            Bukkit.getServer().getLogger().warning(NOT_CONNECTED_MESSAGE);
            return null;
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int update(String sql) {
        if (!isConnected()) {
            Bukkit.getServer().getLogger().warning(NOT_CONNECTED_MESSAGE);
            return -1;
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
