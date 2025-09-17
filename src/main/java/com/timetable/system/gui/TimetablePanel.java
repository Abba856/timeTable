package com.timetable.system.gui;

import com.timetable.system.TimetableEntry;
import com.timetable.system.TimetableEntryDAO;
import com.timetable.system.Lecturer;
import com.timetable.system.LecturerDAO;
import com.timetable.system.Course;
import com.timetable.system.CourseDAO;
import com.timetable.system.Venue;
import com.timetable.system.VenueDAO;
import com.timetable.system.Timeslot;
import com.timetable.system.TimeslotDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.*;

/**
 * TimetablePanel class represents the UI for viewing and generating timetables.
 */
public class TimetablePanel extends JPanel {
    private JTable timetableTable;
    private DefaultTableModel tableModel;
    private JButton generateButton;
    private JButton refreshButton;
    private JButton exportButton;
    private TimetableEntryDAO timetableEntryDAO;
    private LecturerDAO lecturerDAO;
    private CourseDAO courseDAO;
    private VenueDAO venueDAO;
    private TimeslotDAO timeslotDAO;
    
    // Data maps for quick lookup
    private Map<Integer, Lecturer> lecturerMap;
    private Map<Integer, Course> courseMap;
    private Map<Integer, Venue> venueMap;
    private Map<Integer, Timeslot> timeslotMap;
    
    // Days of the week
    private static final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    
    // Timeslots for each day
    private Map<String, List<Timeslot>> timeslotsByDay;

    /**
     * Constructs a new TimetablePanel.
     */
    public TimetablePanel() {
        try {
            timetableEntryDAO = new TimetableEntryDAO();
            lecturerDAO = new LecturerDAO();
            courseDAO = new CourseDAO();
            venueDAO = new VenueDAO();
            timeslotDAO = new TimeslotDAO();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to initialize DAOs: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadData();
        refreshTable();
    }

    /**
     * Initializes the UI components.
     */
    private void initializeComponents() {
        // Initialize data maps
        lecturerMap = new HashMap<>();
        courseMap = new HashMap<>();
        venueMap = new HashMap<>();
        timeslotMap = new HashMap<>();
        timeslotsByDay = new HashMap<>();
        
        // Initialize timeslots by day
        for (String day : DAYS) {
            timeslotsByDay.put(day, new ArrayList<>());
        }
        
        // Table setup
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Table is not editable
            }
        };
        timetableTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                
                // Add alternating row colors
                if (!isRowSelected(row)) {
                    component.setBackground(row % 2 == 0 ? new Color(240, 240, 240) : Color.WHITE);
                }
                
                return component;
            }
        };
        timetableTable.setRowHeight(50);
        timetableTable.getTableHeader().setReorderingAllowed(false);
        
        // Buttons
        generateButton = new JButton("Generate Timetable");
        refreshButton = new JButton("Refresh");
        exportButton = new JButton("Export to CSV");
    }

    /**
     * Sets up the layout of the panel.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(generateButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(exportButton);
        
        // Table panel with scroll pane
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Timetable"));
        tablePanel.add(new JScrollPane(timetableTable), BorderLayout.CENTER);

        // Add components to main panel
        add(buttonPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
    }

    /**
     * Sets up event handlers for UI components.
     */
    private void setupEventHandlers() {
        // Generate button
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateTimetable();
            }
        });

        // Refresh button
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });

        // Export button
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportToCSV();
            }
        });
    }

    /**
     * Loads data from the database into maps for quick lookup.
     */
    private void loadData() {
        try {
            // Load lecturers
            List<Lecturer> lecturers = lecturerDAO.findAll();
            for (Lecturer lecturer : lecturers) {
                lecturerMap.put(lecturer.getId(), lecturer);
            }
            
            // Load courses
            List<Course> courses = courseDAO.findAll();
            for (Course course : courses) {
                courseMap.put(course.getId(), course);
            }
            
            // Load venues
            List<Venue> venues = venueDAO.findAll();
            for (Venue venue : venues) {
                venueMap.put(venue.getId(), venue);
            }
            
            // Load timeslots and organize by day
            List<Timeslot> timeslots = timeslotDAO.findAll();
            for (Timeslot timeslot : timeslots) {
                timeslotMap.put(timeslot.getId(), timeslot);
                timeslotsByDay.get(timeslot.getDayOfWeek()).add(timeslot);
            }
            
            // Sort timeslots by start time
            for (String day : DAYS) {
                Collections.sort(timeslotsByDay.get(day), new Comparator<Timeslot>() {
                    @Override
                    public int compare(Timeslot t1, Timeslot t2) {
                        return t1.getStartTime().compareTo(t2.getStartTime());
                    }
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load data: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Refreshes the timetable table with data from the database.
     */
    private void refreshTable() {
        try {
            // Clear existing data
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            
            // Create columns (days of the week)
            tableModel.addColumn("Time");
            for (String day : DAYS) {
                tableModel.addColumn(day);
            }
            
            // Get all timetable entries
            List<TimetableEntry> entries = timetableEntryDAO.findAll();
            
            // Create a map of entries by timeslot ID for quick lookup
            Map<Integer, TimetableEntry> entryMap = new HashMap<>();
            for (TimetableEntry entry : entries) {
                entryMap.put(entry.getTimeslotId(), entry);
            }
            
            // Add rows for each timeslot
            for (String day : DAYS) {
                List<Timeslot> dayTimeslots = timeslotsByDay.get(day);
                for (Timeslot timeslot : dayTimeslots) {
                    // Create a row for this timeslot if it doesn't exist yet
                    String timeSlotStr = timeslot.getStartTime() + " - " + timeslot.getEndTime();
                    
                    // Check if this timeslot already has a row
                    int rowIndex = -1;
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        if (tableModel.getValueAt(i, 0).equals(timeSlotStr)) {
                            rowIndex = i;
                            break;
                        }
                    }
                    
                    // If no row exists for this time, create one
                    if (rowIndex == -1) {
                        Object[] row = new Object[DAYS.length + 1];
                        row[0] = timeSlotStr;
                        for (int i = 1; i <= DAYS.length; i++) {
                            row[i] = "";
                        }
                        tableModel.addRow(row);
                        rowIndex = tableModel.getRowCount() - 1;
                    }
                    
                    // Find the column index for this day
                    int columnIndex = Arrays.asList(DAYS).indexOf(day) + 1;
                    
                    // If there's an entry for this timeslot, populate the cell
                    TimetableEntry entry = entryMap.get(timeslot.getId());
                    if (entry != null) {
                        // Get related entities
                        Course course = courseMap.get(entry.getCourseId());
                        Lecturer lecturer = lecturerMap.get(entry.getLecturerId());
                        Venue venue = venueMap.get(entry.getVenueId());
                        
                        // Create cell content
                        String cellContent = "<html>";
                        if (course != null) {
                            cellContent += "<b>" + course.getTitle() + "</b><br>";
                        }
                        if (lecturer != null) {
                            cellContent += lecturer.getName() + "<br>";
                        }
                        if (venue != null) {
                            cellContent += venue.getName();
                        }
                        cellContent += "</html>";
                        
                        tableModel.setValueAt(cellContent, rowIndex, columnIndex);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load timetable: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Generates a new timetable using a simple algorithm.
     */
    private void generateTimetable() {
        try {
            // Clear existing timetable entries
            List<TimetableEntry> existingEntries = timetableEntryDAO.findAll();
            for (TimetableEntry entry : existingEntries) {
                timetableEntryDAO.delete(entry.getId());
            }
            
            // Get all required data
            List<Course> courses = courseDAO.findAll();
            List<Lecturer> lecturers = lecturerDAO.findAll();
            List<Venue> venues = venueDAO.findAll();
            List<Timeslot> timeslots = timeslotDAO.findAll();
            
            // Create maps for availability
            Map<Integer, List<Integer>> lecturerAvailability = new HashMap<>();
            for (Lecturer lecturer : lecturers) {
                lecturerAvailability.put(lecturer.getId(), new ArrayList<>());
            }
            
            // For simplicity, we'll assume all lecturers are available at all times
            // In a real implementation, you would load actual availability data
            for (Lecturer lecturer : lecturers) {
                for (Timeslot timeslot : timeslots) {
                    lecturerAvailability.get(lecturer.getId()).add(timeslot.getId());
                }
            }
            
            // Create a list of timetable entries to be created
            List<TimetableEntry> newEntries = new ArrayList<>();
            
            // Simple greedy algorithm to assign courses to timeslots
            Random random = new Random();
            
            for (Course course : courses) {
                // Find a suitable lecturer for this course
                Lecturer assignedLecturer = null;
                for (Lecturer lecturer : lecturers) {
                    // In a real implementation, you would have more sophisticated matching
                    assignedLecturer = lecturer;
                    break;
                }
                
                if (assignedLecturer == null) {
                    continue; // Skip if no lecturer found
                }
                
                // Find a suitable venue for this course
                Venue assignedVenue = null;
                for (Venue venue : venues) {
                    // Check if venue meets course requirements
                    if (course.isLabRequired() && !"Lab".equals(venue.getType())) {
                        continue; // Course requires lab but venue is not a lab
                    }
                    
                    if (venue.getCapacity() >= 30) { // Minimum capacity requirement
                        assignedVenue = venue;
                        break;
                    }
                }
                
                if (assignedVenue == null) {
                    continue; // Skip if no venue found
                }
                
                // Find an available timeslot for the lecturer
                Timeslot assignedTimeslot = null;
                List<Integer> availableTimeslots = lecturerAvailability.get(assignedLecturer.getId());
                
                if (!availableTimeslots.isEmpty()) {
                    // Pick a random available timeslot
                    int randomIndex = random.nextInt(availableTimeslots.size());
                    int timeslotId = availableTimeslots.get(randomIndex);
                    
                    // Find the timeslot object
                    for (Timeslot timeslot : timeslots) {
                        if (timeslot.getId() == timeslotId) {
                            assignedTimeslot = timeslot;
                            break;
                        }
                    }
                }
                
                if (assignedTimeslot == null) {
                    continue; // Skip if no timeslot found
                }
                
                // Create a new timetable entry
                TimetableEntry entry = new TimetableEntry();
                entry.setCourseId(course.getId());
                entry.setLecturerId(assignedLecturer.getId());
                entry.setVenueId(assignedVenue.getId());
                entry.setTimeslotId(assignedTimeslot.getId());
                
                newEntries.add(entry);
                
                // Remove this timeslot from the lecturer's availability
                lecturerAvailability.get(assignedLecturer.getId()).remove(Integer.valueOf(assignedTimeslot.getId()));
            }
            
            // Save all new entries
            for (TimetableEntry entry : newEntries) {
                timetableEntryDAO.save(entry);
            }
            
            // Refresh the table
            refreshTable();
            
            JOptionPane.showMessageDialog(this, "Timetable generated successfully with " + newEntries.size() + " entries.", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to generate timetable: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exports the timetable to a CSV file.
     */
    private void exportToCSV() {
        // In a real implementation, you would export the timetable data to a CSV file
        JOptionPane.showMessageDialog(this, "Export functionality would be implemented here.", 
                                    "Export", JOptionPane.INFORMATION_MESSAGE);
    }
}