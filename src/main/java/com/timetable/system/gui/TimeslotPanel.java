package com.timetable.system.gui;

import com.timetable.system.Timeslot;
import com.timetable.system.TimeslotDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * TimeslotPanel class represents the UI for managing timeslots.
 */
public class TimeslotPanel extends JPanel {
    private JTable timeslotTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> dayComboBox;
    private JTextField startTimeField;
    private JTextField endTimeField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private TimeslotDAO timeslotDAO;
    private int selectedTimeslotId = -1;

    // Day options
    private static final String[] DAY_OPTIONS = {
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    };

    /**
     * Constructs a new TimeslotPanel.
     */
    public TimeslotPanel() {
        try {
            timeslotDAO = new TimeslotDAO();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to initialize TimeslotDAO: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshTable();
    }

    /**
     * Initializes the UI components.
     */
    private void initializeComponents() {
        // Table setup
        tableModel = new DefaultTableModel(new Object[]{"ID", "Day of Week", "Start Time", "End Time"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Table is not editable
            }
        };
        timeslotTable = new JTable(tableModel);

        // Form fields
        dayComboBox = new JComboBox<>(DAY_OPTIONS);
        startTimeField = new JTextField(20);
        endTimeField = new JTextField(20);
        
        // Buttons
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        
        // Disable update/delete buttons initially
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    /**
     * Sets up the layout of the panel.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        // Table panel with scroll pane
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Timeslots"));
        tablePanel.add(new JScrollPane(timeslotTable), BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Timeslot Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Day field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Day of Week:"), gbc);
        gbc.gridx = 1;
        formPanel.add(dayComboBox, gbc);

        // Start time field
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Start Time (HH:MM):"), gbc);
        gbc.gridx = 1;
        formPanel.add(startTimeField, gbc);

        // End time field
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("End Time (HH:MM):"), gbc);
        gbc.gridx = 1;
        formPanel.add(endTimeField, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Combine form and buttons
        JPanel formAndButtonsPanel = new JPanel(new BorderLayout());
        formAndButtonsPanel.add(formPanel, BorderLayout.CENTER);

        // Add components to main panel
        add(tablePanel, BorderLayout.CENTER);
        add(formAndButtonsPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets up event handlers for UI components.
     */
    private void setupEventHandlers() {
        // Table selection listener
        timeslotTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = timeslotTable.getSelectedRow();
            if (selectedRow >= 0) {
                // Get data from the selected row
                selectedTimeslotId = (int) tableModel.getValueAt(selectedRow, 0);
                dayComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 1));
                startTimeField.setText((String) tableModel.getValueAt(selectedRow, 2));
                endTimeField.setText((String) tableModel.getValueAt(selectedRow, 3));
                
                // Enable update/delete buttons
                updateButton.setEnabled(true);
                deleteButton.setEnabled(true);
            } else {
                // Clear selection
                selectedTimeslotId = -1;
                clearForm();
            }
        });

        // Add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTimeslot();
            }
        });

        // Update button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTimeslot();
            }
        });

        // Delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTimeslot();
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
     * Refreshes the timeslot table with data from the database.
     */
    private void refreshTable() {
        try {
            // Clear existing data
            tableModel.setRowCount(0);
            
            // Load timeslots from database
            List<Timeslot> timeslots = timeslotDAO.findAll();
            for (Timeslot timeslot : timeslots) {
                Object[] row = {
                    timeslot.getId(),
                    timeslot.getDayOfWeek(),
                    timeslot.getStartTime(),
                    timeslot.getEndTime()
                };
                tableModel.addRow(row);
            }
            
            // Clear selection
            timeslotTable.clearSelection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load timeslots: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adds a new timeslot to the database.
     */
    private void addTimeslot() {
        String day = (String) dayComboBox.getSelectedItem();
        String startTime = startTimeField.getText().trim();
        String endTime = endTimeField.getText().trim();

        // Validate input
        if (startTime.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a start time for the timeslot.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (endTime.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an end time for the timeslot.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate time format (HH:MM)
        if (!isValidTimeFormat(startTime)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid start time in HH:MM format.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isValidTimeFormat(endTime)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid end time in HH:MM format.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Create and save timeslot
            Timeslot timeslot = new Timeslot();
            timeslot.setDayOfWeek(day);
            timeslot.setStartTime(startTime);
            timeslot.setEndTime(endTime);
            
            timeslotDAO.save(timeslot);
            
            // Refresh table and clear form
            refreshTable();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Timeslot added successfully.", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to add timeslot: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates an existing timeslot in the database.
     */
    private void updateTimeslot() {
        if (selectedTimeslotId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a timeslot to update.", 
                                        "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String day = (String) dayComboBox.getSelectedItem();
        String startTime = startTimeField.getText().trim();
        String endTime = endTimeField.getText().trim();

        // Validate input
        if (startTime.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a start time for the timeslot.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (endTime.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an end time for the timeslot.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate time format (HH:MM)
        if (!isValidTimeFormat(startTime)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid start time in HH:MM format.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isValidTimeFormat(endTime)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid end time in HH:MM format.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Create and update timeslot
            Timeslot timeslot = new Timeslot();
            timeslot.setId(selectedTimeslotId);
            timeslot.setDayOfWeek(day);
            timeslot.setStartTime(startTime);
            timeslot.setEndTime(endTime);
            
            timeslotDAO.save(timeslot);
            
            // Refresh table and clear form
            refreshTable();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Timeslot updated successfully.", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to update timeslot: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes a timeslot from the database.
     */
    private void deleteTimeslot() {
        if (selectedTimeslotId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a timeslot to delete.", 
                                        "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this timeslot? This action cannot be undone.", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (option != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            timeslotDAO.delete(selectedTimeslotId);
            
            // Refresh table and clear form
            refreshTable();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Timeslot deleted successfully.", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to delete timeslot: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears the form fields and resets the selection.
     */
    private void clearForm() {
        dayComboBox.setSelectedIndex(0);
        startTimeField.setText("");
        endTimeField.setText("");
        selectedTimeslotId = -1;
        timeslotTable.clearSelection();
        
        // Disable update/delete buttons
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    /**
     * Validates if the time string is in HH:MM format.
     * 
     * @param time the time string to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidTimeFormat(String time) {
        if (time == null || time.isEmpty()) {
            return false;
        }
        
        // Check if it matches HH:MM format
        String[] parts = time.split(":");
        if (parts.length != 2) {
            return false;
        }
        
        try {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            
            return hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}