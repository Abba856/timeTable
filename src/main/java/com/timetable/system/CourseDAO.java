package com.timetable.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * CourseDAO implements data access operations for Course entities.
 */
public class CourseDAO implements DAO<Course> {
    
    private Connection connection;
    
    /**
     * Constructs a new CourseDAO with a database connection.
     * 
     * @throws SQLException if a database access error occurs
     */
    public CourseDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }
    
    @Override
    public void save(Course course) throws SQLException {
        if (course.isSaved()) {
            update(course);
        } else {
            insert(course);
        }
    }
    
    @Override
    public void insert(Course course) throws SQLException {
        String sql = "INSERT INTO Course (title, level, lab_required) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, course.getTitle());
            statement.setString(2, course.getLevel());
            statement.setBoolean(3, course.isLabRequired());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating course failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    course.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating course failed, no ID obtained.");
                }
            }
        }
    }
    
    @Override
    public void update(Course course) throws SQLException {
        String sql = "UPDATE Course SET title = ?, level = ?, lab_required = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, course.getTitle());
            statement.setString(2, course.getLevel());
            statement.setBoolean(3, course.isLabRequired());
            statement.setInt(4, course.getId());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating course failed, no rows affected.");
            }
        }
    }
    
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Course WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Deleting course failed, no rows affected.");
            }
        }
    }
    
    @Override
    public Course findById(int id) throws SQLException {
        String sql = "SELECT * FROM Course WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Course course = new Course();
                    course.setId(resultSet.getInt("id"));
                    course.setTitle(resultSet.getString("title"));
                    course.setLevel(resultSet.getString("level"));
                    course.setLabRequired(resultSet.getBoolean("lab_required"));
                    return course;
                }
            }
        }
        return null;
    }
    
    @Override
    public List<Course> findAll() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM Course";
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt("id"));
                course.setTitle(resultSet.getString("title"));
                course.setLevel(resultSet.getString("level"));
                course.setLabRequired(resultSet.getBoolean("lab_required"));
                courses.add(course);
            }
        }
        
        return courses;
    }
}