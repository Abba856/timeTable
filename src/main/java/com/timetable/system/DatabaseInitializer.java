package com.timetable.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseInitializer class is responsible for initializing the database schema.
 */
public class DatabaseInitializer {
    
    /**
     * Initializes the database by executing the appropriate schema.sql script.
     * 
     * @throws SQLException if a database access error occurs
     * @throws IOException if an I/O error occurs
     */
    public static void initializeDatabase() throws SQLException, IOException {
        Connection connection = DatabaseConnection.getConnection();
        
        // Determine which schema file to use based on the database type
        String schemaFile = "schema.sql"; // Default to SQLite
        if (connection.getMetaData().getURL().startsWith("jdbc:mysql:")) {
            schemaFile = "schema-mysql.sql"; // Use MySQL schema
        }
        
        // Read the schema file
        try (InputStream input = DatabaseInitializer.class.getClassLoader().getResourceAsStream(schemaFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            
            StringBuilder sqlBuilder = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                // Skip empty lines and comments
                if (!line.trim().isEmpty() && !line.trim().startsWith("--")) {
                    sqlBuilder.append(line).append("\n");
                }
            }
            
            // Split statements by semicolon
            String[] statements = sqlBuilder.toString().split(";");
            
            // Execute each statement
            for (String sql : statements) {
                if (!sql.trim().isEmpty()) {
                    try (Statement statement = connection.createStatement()) {
                        statement.execute(sql);
                    }
                }
            }
        }
    }
}