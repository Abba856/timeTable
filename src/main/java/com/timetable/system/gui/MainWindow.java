package com.timetable.system.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * MainWindow class represents the main window of the application.
 */
public class MainWindow extends JFrame {
    private JPanel contentPanel;
    
    /**
     * Constructs a new MainWindow.
     */
    public MainWindow() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Initializes the UI components.
     */
    private void initializeComponents() {
        setTitle("Automated Timetable Generation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null); // Center the window
        
        // Create the content panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
    }
    
    /**
     * Sets up the layout of the main window.
     */
    private void setupLayout() {
        // Create the navigation panel
        JPanel navigationPanel = createNavigationPanel();
        
        // Add components to the main window
        setLayout(new BorderLayout());
        add(navigationPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Creates the navigation panel with menu items.
     * 
     * @return the navigation panel
     */
    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(200, 0));
        
        // Create menu buttons
        JButton lecturerButton = new JButton("Lecturers");
        JButton courseButton = new JButton("Courses");
        JButton venueButton = new JButton("Venues");
        JButton timeslotButton = new JButton("Timeslots");
        JButton availabilityButton = new JButton("Availability");
        JButton timetableButton = new JButton("Timetable");
        
        // Add buttons to the panel
        panel.add(lecturerButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(courseButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(venueButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(timeslotButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(availabilityButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(timetableButton);
        
        // Store buttons as instance variables for event handling
        // For now, we'll just add action listeners directly
        
        lecturerButton.addActionListener(new NavigationActionListener("Lecturers"));
        courseButton.addActionListener(new NavigationActionListener("Courses"));
        venueButton.addActionListener(new NavigationActionListener("Venues"));
        timeslotButton.addActionListener(new NavigationActionListener("Timeslots"));
        availabilityButton.addActionListener(new NavigationActionListener("Availability"));
        timetableButton.addActionListener(new NavigationActionListener("Timetable"));
        
        return panel;
    }
    
    /**
     * Sets up event handlers for UI components.
     */
    private void setupEventHandlers() {
        // Event handlers are added directly to buttons in this example
    }
    
    /**
     * Updates the content panel with the specified content.
     * 
     * @param title the title of the content
     * @param content the content component to display
     */
    private void updateContent(String title, JComponent content) {
        contentPanel.removeAll();
        contentPanel.add(new JLabel(title, JLabel.CENTER), BorderLayout.NORTH);
        contentPanel.add(content, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * ActionListener for navigation buttons.
     */
    private class NavigationActionListener implements ActionListener {
        private String module;
        
        public NavigationActionListener(String module) {
            this.module = module;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (module) {
                case "Lecturers":
                    updateContent("Lecturers Management", new LecturerPanel());
                    break;
                case "Courses":
                    updateContent("Courses Management", new CoursePanel());
                    break;
                case "Venues":
                    updateContent("Venues Management", new VenuePanel());
                    break;
                case "Timeslots":
                    updateContent("Timeslots Management", new TimeslotPanel());
                    break;
                case "Availability":
                    updateContent("Lecturer Availability", new AvailabilityPanel());
                    break;
                case "Timetable":
                    updateContent("Timetable Generation", new TimetablePanel());
                    break;
                default:
                    updateContent("Module Not Found", new JLabel("The selected module is not implemented yet."));
                    break;
            }
        }
    }
}