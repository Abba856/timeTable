package com.timetable.system;

import com.timetable.system.gui.MainWindow;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Main class for the Automated Timetable Generation System.
 */
public class TimetableApplication {
    
    /**
     * Main method for the application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            // Initialize the database
            DatabaseInitializer.initializeDatabase();
            System.out.println("Database initialized successfully.");
            
            // Start the GUI application
            SwingUtilities.invokeLater(() -> {
                MainWindow mainWindow = new MainWindow();
                mainWindow.setVisible(true);
            });
            
        } catch (SQLException | IOException e) {
            System.err.println("Failed to initialize the application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}