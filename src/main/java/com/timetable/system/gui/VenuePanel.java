package com.timetable.system.gui;

import com.timetable.system.Venue;
import com.timetable.system.VenueDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * VenuePanel class represents the UI for managing venues.
 */
public class VenuePanel extends JPanel {
    private JTable venueTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField capacityField;
    private JComboBox<String> typeComboBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private VenueDAO venueDAO;
    private int selectedVenueId = -1;

    // Type options
    private static final String[] TYPE_OPTIONS = {"Lecture Hall", "Lab"};

    /**
     * Constructs a new VenuePanel.
     */
    public VenuePanel() {
        try {
            venueDAO = new VenueDAO();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to initialize VenueDAO: " + e.getMessage(), 
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
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Capacity", "Type"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Table is not editable
            }
        };
        venueTable = new JTable(tableModel);

        // Form fields
        nameField = new JTextField(20);
        capacityField = new JTextField(20);
        typeComboBox = new JComboBox<>(TYPE_OPTIONS);
        
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
        tablePanel.setBorder(BorderFactory.createTitledBorder("Venues"));
        tablePanel.add(new JScrollPane(venueTable), BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Venue Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Name field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // Capacity field
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Capacity:"), gbc);
        gbc.gridx = 1;
        formPanel.add(capacityField, gbc);

        // Type field
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(typeComboBox, gbc);

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
        venueTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = venueTable.getSelectedRow();
            if (selectedRow >= 0) {
                // Get data from the selected row
                selectedVenueId = (int) tableModel.getValueAt(selectedRow, 0);
                nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
                
                // Convert capacity to string for display
                Integer capacity = (Integer) tableModel.getValueAt(selectedRow, 2);
                capacityField.setText(capacity != null ? capacity.toString() : "");
                
                typeComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 3));
                
                // Enable update/delete buttons
                updateButton.setEnabled(true);
                deleteButton.setEnabled(true);
            } else {
                // Clear selection
                selectedVenueId = -1;
                clearForm();
            }
        });

        // Add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addVenue();
            }
        });

        // Update button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateVenue();
            }
        });

        // Delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteVenue();
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
     * Refreshes the venue table with data from the database.
     */
    private void refreshTable() {
        try {
            // Clear existing data
            tableModel.setRowCount(0);
            
            // Load venues from database
            List<Venue> venues = venueDAO.findAll();
            for (Venue venue : venues) {
                Object[] row = {
                    venue.getId(),
                    venue.getName(),
                    venue.getCapacity(),
                    venue.getType()
                };
                tableModel.addRow(row);
            }
            
            // Clear selection
            venueTable.clearSelection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load venues: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adds a new venue to the database.
     */
    private void addVenue() {
        String name = nameField.getText().trim();
        String capacityStr = capacityField.getText().trim();
        String type = (String) typeComboBox.getSelectedItem();

        // Validate input
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name for the venue.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (capacityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a capacity for the venue.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityStr);
            if (capacity <= 0) {
                JOptionPane.showMessageDialog(this, "Capacity must be a positive number.", 
                                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for capacity.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Create and save venue
            Venue venue = new Venue();
            venue.setName(name);
            venue.setCapacity(capacity);
            venue.setType(type);
            
            venueDAO.save(venue);
            
            // Refresh table and clear form
            refreshTable();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Venue added successfully.", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to add venue: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates an existing venue in the database.
     */
    private void updateVenue() {
        if (selectedVenueId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a venue to update.", 
                                        "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String name = nameField.getText().trim();
        String capacityStr = capacityField.getText().trim();
        String type = (String) typeComboBox.getSelectedItem();

        // Validate input
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name for the venue.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (capacityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a capacity for the venue.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityStr);
            if (capacity <= 0) {
                JOptionPane.showMessageDialog(this, "Capacity must be a positive number.", 
                                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for capacity.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Create and update venue
            Venue venue = new Venue();
            venue.setId(selectedVenueId);
            venue.setName(name);
            venue.setCapacity(capacity);
            venue.setType(type);
            
            venueDAO.save(venue);
            
            // Refresh table and clear form
            refreshTable();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Venue updated successfully.", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to update venue: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes a venue from the database.
     */
    private void deleteVenue() {
        if (selectedVenueId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a venue to delete.", 
                                        "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this venue? This action cannot be undone.", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (option != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            venueDAO.delete(selectedVenueId);
            
            // Refresh table and clear form
            refreshTable();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Venue deleted successfully.", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to delete venue: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears the form fields and resets the selection.
     */
    private void clearForm() {
        nameField.setText("");
        capacityField.setText("");
        typeComboBox.setSelectedIndex(0);
        selectedVenueId = -1;
        venueTable.clearSelection();
        
        // Disable update/delete buttons
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
}