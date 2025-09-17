package com.timetable.system.gui;

import com.timetable.system.Course;
import com.timetable.system.CourseDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * CoursePanel class represents the UI for managing courses.
 */
public class CoursePanel extends JPanel {
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JTextField titleField;
    private JComboBox<String> levelComboBox;
    private JCheckBox labRequiredCheckBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private CourseDAO courseDAO;
    private int selectedCourseId = -1;

    // Level options
    private static final String[] LEVEL_OPTIONS = {"ND", "HND"};

    /**
     * Constructs a new CoursePanel.
     */
    public CoursePanel() {
        try {
            courseDAO = new CourseDAO();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to initialize CourseDAO: " + e.getMessage(), 
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
        tableModel = new DefaultTableModel(new Object[]{"ID", "Title", "Level", "Lab Required"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Table is not editable
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) {
                    return Boolean.class; // Lab Required column
                }
                return super.getColumnClass(columnIndex);
            }
        };
        courseTable = new JTable(tableModel);

        // Form fields
        titleField = new JTextField(20);
        levelComboBox = new JComboBox<>(LEVEL_OPTIONS);
        labRequiredCheckBox = new JCheckBox("Lab Required");
        
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
        tablePanel.setBorder(BorderFactory.createTitledBorder("Courses"));
        tablePanel.add(new JScrollPane(courseTable), BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Course Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Title field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);

        // Level field
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Level:"), gbc);
        gbc.gridx = 1;
        formPanel.add(levelComboBox, gbc);

        // Lab required field
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Lab Required:"), gbc);
        gbc.gridx = 1;
        formPanel.add(labRequiredCheckBox, gbc);

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
        courseTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = courseTable.getSelectedRow();
            if (selectedRow >= 0) {
                // Get data from the selected row
                selectedCourseId = (int) tableModel.getValueAt(selectedRow, 0);
                titleField.setText((String) tableModel.getValueAt(selectedRow, 1));
                levelComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 2));
                labRequiredCheckBox.setSelected((Boolean) tableModel.getValueAt(selectedRow, 3));
                
                // Enable update/delete buttons
                updateButton.setEnabled(true);
                deleteButton.setEnabled(true);
            } else {
                // Clear selection
                selectedCourseId = -1;
                clearForm();
            }
        });

        // Add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCourse();
            }
        });

        // Update button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCourse();
            }
        });

        // Delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCourse();
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
     * Refreshes the course table with data from the database.
     */
    private void refreshTable() {
        try {
            // Clear existing data
            tableModel.setRowCount(0);
            
            // Load courses from database
            List<Course> courses = courseDAO.findAll();
            for (Course course : courses) {
                Object[] row = {
                    course.getId(),
                    course.getTitle(),
                    course.getLevel(),
                    course.isLabRequired()
                };
                tableModel.addRow(row);
            }
            
            // Clear selection
            courseTable.clearSelection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load courses: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adds a new course to the database.
     */
    private void addCourse() {
        String title = titleField.getText().trim();
        String level = (String) levelComboBox.getSelectedItem();
        boolean labRequired = labRequiredCheckBox.isSelected();

        // Validate input
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a title for the course.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Create and save course
            Course course = new Course();
            course.setTitle(title);
            course.setLevel(level);
            course.setLabRequired(labRequired);
            
            courseDAO.save(course);
            
            // Refresh table and clear form
            refreshTable();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Course added successfully.", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to add course: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates an existing course in the database.
     */
    private void updateCourse() {
        if (selectedCourseId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to update.", 
                                        "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String title = titleField.getText().trim();
        String level = (String) levelComboBox.getSelectedItem();
        boolean labRequired = labRequiredCheckBox.isSelected();

        // Validate input
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a title for the course.", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Create and update course
            Course course = new Course();
            course.setId(selectedCourseId);
            course.setTitle(title);
            course.setLevel(level);
            course.setLabRequired(labRequired);
            
            courseDAO.save(course);
            
            // Refresh table and clear form
            refreshTable();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Course updated successfully.", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to update course: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes a course from the database.
     */
    private void deleteCourse() {
        if (selectedCourseId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to delete.", 
                                        "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this course? This action cannot be undone.", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (option != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            courseDAO.delete(selectedCourseId);
            
            // Refresh table and clear form
            refreshTable();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Course deleted successfully.", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to delete course: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears the form fields and resets the selection.
     */
    private void clearForm() {
        titleField.setText("");
        levelComboBox.setSelectedIndex(0);
        labRequiredCheckBox.setSelected(false);
        selectedCourseId = -1;
        courseTable.clearSelection();
        
        // Disable update/delete buttons
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
}