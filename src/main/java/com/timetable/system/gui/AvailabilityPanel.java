package com.timetable.system.gui;

import com.timetable.system.Availability;
import com.timetable.system.AvailabilityDAO;
import com.timetable.system.Lecturer;
import com.timetable.system.LecturerDAO;
import com.timetable.system.Timeslot;
import com.timetable.system.TimeslotDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 * AvailabilityPanel class represents the UI for managing lecturer availability.
 */
public class AvailabilityPanel extends JPanel {
    private JComboBox<Lecturer> lecturerComboBox;
    private JTable availabilityTable;
    private DefaultTableModel tableModel;
    private JComboBox<Timeslot> timeslotComboBox;
    private JButton addButton;
    private JButton deleteButton;
    private JButton clearButton;
    private AvailabilityDAO availabilityDAO;
    private LecturerDAO lecturerDAO;
    private TimeslotDAO timeslotDAO;
    private int selectedAvailabilityId = -1;

    /**
     * Constructs a new AvailabilityPanel.
     */
    public AvailabilityPanel() {
        try {
            availabilityDAO = new AvailabilityDAO();
            lecturerDAO = new LecturerDAO();
            timeslotDAO = new TimeslotDAO();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to initialize DAOs: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadLecturers();
        loadTimeslots();
    }

    /**
     * Initializes the UI components.
     */
    private void initializeComponents() {
        // ComboBoxes
        lecturerComboBox = new JComboBox<>();
        timeslotComboBox = new JComboBox<>();
        
        // Table setup
        tableModel = new DefaultTableModel(new Object[]{"ID", "Lecturer", "Timeslot"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Table is not editable
            }
        };
        availabilityTable = new JTable(tableModel);

        // Buttons
        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        
        // Disable delete button initially
        deleteButton.setEnabled(false);
    }

    /**
     * Sets up the layout of the panel.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        // Selection panel
        JPanel selectionPanel = new JPanel(new FlowLayout());
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Select Lecturer"));
        selectionPanel.add(new JLabel("Lecturer:"));
        selectionPanel.add(lecturerComboBox);

        // Table panel with scroll pane
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Availability"));
        tablePanel.add(new JScrollPane(availabilityTable), BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Availability"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Timeslot field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Timeslot:"), gbc);
        gbc.gridx = 1;
        formPanel.add(timeslotComboBox, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Combine form and buttons
        JPanel formAndButtonsPanel = new JPanel(new BorderLayout());
        formAndButtonsPanel.add(formPanel, BorderLayout.CENTER);

        // Add components to main panel
        add(selectionPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(formAndButtonsPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets up event handlers for UI components.
     */
    private void setupEventHandlers() {
        // Lecturer selection listener
        lecturerComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });

        // Table selection listener
        availabilityTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = availabilityTable.getSelectedRow();
            if (selectedRow >= 0) {
                // Get data from the selected row
                selectedAvailabilityId = (int) tableModel.getValueAt(selectedRow, 0);
                
                // Enable delete button
                deleteButton.setEnabled(true);
            } else {
                // Clear selection
                selectedAvailabilityId = -1;
                
                // Disable delete button
                deleteButton.setEnabled(false);
            }
        });

        // Add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAvailability();
            }
        });

        // Delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAvailability();
            }
        });

        // Clear button
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
    }

    /**
     * Loads lecturers into the lecturer combo box.
     */
    private void loadLecturers() {
        try {
            List<Lecturer> lecturers = lecturerDAO.findAll();
            lecturerComboBox.removeAllItems();
            for (Lecturer lecturer : lecturers) {
                lecturerComboBox.addItem(lecturer);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load lecturers: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads timeslots into the timeslot combo box.
     */
    private void loadTimeslots() {
        try {
            List<Timeslot> timeslots = timeslotDAO.findAll();
            timeslotComboBox.removeAllItems();
            for (Timeslot timeslot : timeslots) {
                timeslotComboBox.addItem(timeslot);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load timeslots: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Refreshes the availability table with data from the database for the selected lecturer.
     */
    private void refreshTable() {
        Lecturer selectedLecturer = (Lecturer) lecturerComboBox.getSelectedItem();
        if (selectedLecturer == null) {
            return;
        }

        try {
            // Clear existing data
            tableModel.setRowCount(0);
            
            // Load availabilities from database for the selected lecturer
            List<Availability> availabilities = availabilityDAO.findByLecturerId(selectedLecturer.getId());
            
            // Load all timeslots for display
            List<Timeslot> timeslots = timeslotDAO.findAll();
            
            // Create a map of timeslot ID to timeslot for easy lookup
            java.util.Map<Integer, Timeslot> timeslotMap = new java.util.HashMap<>();
            for (Timeslot timeslot : timeslots) {
                timeslotMap.put(timeslot.getId(), timeslot);
            }
            
            // Add availabilities to table
            for (Availability availability : availabilities) {
                Timeslot timeslot = timeslotMap.get(availability.getTimeslotId());
                String timeslotStr = timeslot != null ? 
                    timeslot.getDayOfWeek() + " " + timeslot.getStartTime() + "-" + timeslot.getEndTime() : 
                    "Unknown Timeslot";
                
                Object[] row = {
                    availability.getId(),
                    selectedLecturer.getName(),
                    timeslotStr
                };
                tableModel.addRow(row);
            }
            
            // Clear selection
            availabilityTable.clearSelection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load availabilities: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adds a new availability to the database.
     */
    private void addAvailability() {
        Lecturer selectedLecturer = (Lecturer) lecturerComboBox.getSelectedItem();
        Timeslot selectedTimeslot = (Timeslot) timeslotComboBox.getSelectedItem();

        // Validate input
        if (selectedLecturer == null) {
            JOptionPane.showMessageDialog(this, "Please select a lecturer.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedTimeslot == null) {
            JOptionPane.showMessageDialog(this, "Please select a timeslot.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Check if this availability already exists
            List<Availability> existingAvailabilities = availabilityDAO.findByLecturerId(selectedLecturer.getId());
            for (Availability availability : existingAvailabilities) {
                if (availability.getTimeslotId() == selectedTimeslot.getId()) {
                    JOptionPane.showMessageDialog(this, "This availability already exists.", 
                                                "Duplicate Entry", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            
            // Create and save availability
            Availability availability = new Availability();
            availability.setLecturerId(selectedLecturer.getId());
            availability.setTimeslotId(selectedTimeslot.getId());
            
            availabilityDAO.save(availability);
            
            // Refresh table and clear form
            refreshTable();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Availability added successfully.", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to add availability: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes an availability from the database.
     */
    private void deleteAvailability() {
        if (selectedAvailabilityId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an availability to delete.", 
                                        "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this availability? This action cannot be undone.", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (option != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            availabilityDAO.delete(selectedAvailabilityId);
            
            // Refresh table and clear form
            refreshTable();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Availability deleted successfully.", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to delete availability: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears the form fields and resets the selection.
     */
    private void clearForm() {
        timeslotComboBox.setSelectedIndex(0);
        selectedAvailabilityId = -1;
        availabilityTable.clearSelection();
        
        // Disable delete button
        deleteButton.setEnabled(false);
    }
}