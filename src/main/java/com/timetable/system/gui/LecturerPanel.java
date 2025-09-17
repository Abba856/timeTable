package com.timetable.system.gui;

import com.timetable.system.Lecturer;
import com.timetable.system.LecturerDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * LecturerPanel class represents the UI for managing lecturers.
 */
public class LecturerPanel extends JPanel {
    private JTable lecturerTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JComboBox<String> rankComboBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private LecturerDAO lecturerDAO;
    private int selectedLecturerId = -1;

    // Rank options
    private static final String[] RANK_OPTIONS = {
        "Professor", 
        "Associate Professor", 
        "Assistant Professor", 
        "Lecturer"
    };

    /**
     * Constructs a new LecturerPanel.
     */
    public LecturerPanel() {
        try {
            lecturerDAO = new LecturerDAO();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to initialize LecturerDAO: " + e.getMessage(), 
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
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Rank"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Table is not editable
            }
        };
        lecturerTable = new JTable(tableModel);

        // Form fields
        nameField = new JTextField(20);
        rankComboBox = new JComboBox<>(RANK_OPTIONS);
        
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
        tablePanel.setBorder(BorderFactory.createTitledBorder("Lecturers"));
        tablePanel.add(new JScrollPane(lecturerTable), BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Lecturer Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Name field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // Rank field
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Rank:"), gbc);
        gbc.gridx = 1;
        formPanel.add(rankComboBox, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0; gbc.gridy = 2;
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
        lecturerTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = lecturerTable.getSelectedRow();
            if (selectedRow >= 0) {
                // Get data from the selected row
                selectedLecturerId = (int) tableModel.getValueAt(selectedRow, 0);
                nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
                rankComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 2));
                
                // Enable update/delete buttons
                updateButton.setEnabled(true);
                deleteButton.setEnabled(true);
            } else {
                // Clear selection
                selectedLecturerId = -1;
                clearForm();
            }
        });

        // Add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLecturer();
            }
        });

        // Update button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLecturer();
            }
        });

        // Delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteLecturer();
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
     * Refreshes the lecturer table with data from the database.
     */
    private void refreshTable() {
        try {
            // Clear existing data
            tableModel.setRowCount(0);
            
            // Load lecturers from database
            List<Lecturer> lecturers = lecturerDAO.findAll();
            for (Lecturer lecturer : lecturers) {
                Object[] row = {
                    lecturer.getId(),
                    lecturer.getName(),
                    lecturer.getRank()
                };
                tableModel.addRow(row);
            }
            
            // Clear selection
            lecturerTable.clearSelection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load lecturers: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adds a new lecturer to the database.
     */
    private void addLecturer() {
        String name = nameField.getText().trim();
        String rank = (String) rankComboBox.getSelectedItem();

        // Validate input
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name for the lecturer.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Create and save lecturer
            Lecturer lecturer = new Lecturer();
            lecturer.setName(name);
            lecturer.setRank(rank);
            
            lecturerDAO.save(lecturer);
            
            // Refresh table and clear form
            refreshTable();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Lecturer added successfully.", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to add lecturer: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates an existing lecturer in the database.
     */
    private void updateLecturer() {
        if (selectedLecturerId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lecturer to update.", 
                                        "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String name = nameField.getText().trim();
        String rank = (String) rankComboBox.getSelectedItem();

        // Validate input
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name for the lecturer.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Create and update lecturer
            Lecturer lecturer = new Lecturer();
            lecturer.setId(selectedLecturerId);
            lecturer.setName(name);
            lecturer.setRank(rank);
            
            lecturerDAO.save(lecturer);
            
            // Refresh table and clear form
            refreshTable();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Lecturer updated successfully.", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to update lecturer: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes a lecturer from the database.
     */
    private void deleteLecturer() {
        if (selectedLecturerId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lecturer to delete.", 
                                        "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this lecturer? This action cannot be undone.", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (option != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            lecturerDAO.delete(selectedLecturerId);
            
            // Refresh table and clear form
            refreshTable();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Lecturer deleted successfully.", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to delete lecturer: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears the form fields and resets the selection.
     */
    private void clearForm() {
        nameField.setText("");
        rankComboBox.setSelectedIndex(0);
        selectedLecturerId = -1;
        lecturerTable.clearSelection();
        
        // Disable update/delete buttons
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
}