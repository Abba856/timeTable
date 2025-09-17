package com.timetable.system;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DatabaseConnection class manages the JDBC connection to the database.
 * It supports both SQLite and MySQL based on the configuration in database.properties.
 */
public class DatabaseConnection {
    private static Connection connection = null;
    
    /**
     * Gets a database connection.
     * 
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("database.properties")) {
                Properties prop = new Properties();
                prop.load(input);
                
                String url = prop.getProperty("db.url");
                String user = prop.getProperty("db.user");
                String password = prop.getProperty("db.password");
                
                // Load the JDBC driver
                if (url.startsWith("jdbc:sqlite:")) {
                    Class.forName("org.sqlite.JDBC");
                } else if (url.startsWith("jdbc:mysql:")) {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                }
                
                // Establish connection
                connection = DriverManager.getConnection(url, user, password);
            } catch (IOException | ClassNotFoundException e) {
                throw new SQLException("Failed to establish database connection", e);
            }
        }
        return connection;
    }
    
    /**
     * Closes the database connection.
     * 
     * @throws SQLException if a database access error occurs
     */
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}