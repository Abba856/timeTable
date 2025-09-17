package com.timetable.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * LecturerDAO implements data access operations for Lecturer entities.
 */
public class LecturerDAO implements DAO<Lecturer> {
    
    private Connection connection;
    
    /**
     * Constructs a new LecturerDAO with a database connection.
     * 
     * @throws SQLException if a database access error occurs
     */
    public LecturerDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }
    
    @Override
    public void save(Lecturer lecturer) throws SQLException {
        if (lecturer.isSaved()) {
            update(lecturer);
        } else {
            insert(lecturer);
        }
    }
    
    @Override
    public void insert(Lecturer lecturer) throws SQLException {
        String sql = "INSERT INTO Lecturer (name, rank) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, lecturer.getName());
            statement.setString(2, lecturer.getRank());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating lecturer failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    lecturer.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating lecturer failed, no ID obtained.");
                }
            }
        }
    }
    
    @Override
    public void update(Lecturer lecturer) throws SQLException {
        String sql = "UPDATE Lecturer SET name = ?, rank = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, lecturer.getName());
            statement.setString(2, lecturer.getRank());
            statement.setInt(3, lecturer.getId());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating lecturer failed, no rows affected.");
            }
        }
    }
    
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Lecturer WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Deleting lecturer failed, no rows affected.");
            }
        }
    }
    
    @Override
    public Lecturer findById(int id) throws SQLException {
        String sql = "SELECT * FROM Lecturer WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Lecturer lecturer = new Lecturer();
                    lecturer.setId(resultSet.getInt("id"));
                    lecturer.setName(resultSet.getString("name"));
                    lecturer.setRank(resultSet.getString("rank"));
                    return lecturer;
                }
            }
        }
        return null;
    }
    
    @Override
    public List<Lecturer> findAll() throws SQLException {
        List<Lecturer> lecturers = new ArrayList<>();
        String sql = "SELECT * FROM Lecturer";
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            while (resultSet.next()) {
                Lecturer lecturer = new Lecturer();
                lecturer.setId(resultSet.getInt("id"));
                lecturer.setName(resultSet.getString("name"));
                lecturer.setRank(resultSet.getString("rank"));
                lecturers.add(lecturer);
            }
        }
        
        return lecturers;
    }
}