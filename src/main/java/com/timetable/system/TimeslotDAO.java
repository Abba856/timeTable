package com.timetable.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * TimeslotDAO implements data access operations for Timeslot entities.
 */
public class TimeslotDAO implements DAO<Timeslot> {
    
    private Connection connection;
    
    /**
     * Constructs a new TimeslotDAO with a database connection.
     * 
     * @throws SQLException if a database access error occurs
     */
    public TimeslotDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }
    
    @Override
    public void save(Timeslot timeslot) throws SQLException {
        if (timeslot.isSaved()) {
            update(timeslot);
        } else {
            insert(timeslot);
        }
    }
    
    @Override
    public void insert(Timeslot timeslot) throws SQLException {
        String sql = "INSERT INTO Timeslot (day_of_week, start_time, end_time) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, timeslot.getDayOfWeek());
            statement.setString(2, timeslot.getStartTime());
            statement.setString(3, timeslot.getEndTime());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating timeslot failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    timeslot.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating timeslot failed, no ID obtained.");
                }
            }
        }
    }
    
    @Override
    public void update(Timeslot timeslot) throws SQLException {
        String sql = "UPDATE Timeslot SET day_of_week = ?, start_time = ?, end_time = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, timeslot.getDayOfWeek());
            statement.setString(2, timeslot.getStartTime());
            statement.setString(3, timeslot.getEndTime());
            statement.setInt(4, timeslot.getId());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating timeslot failed, no rows affected.");
            }
        }
    }
    
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Timeslot WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Deleting timeslot failed, no rows affected.");
            }
        }
    }
    
    @Override
    public Timeslot findById(int id) throws SQLException {
        String sql = "SELECT * FROM Timeslot WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Timeslot timeslot = new Timeslot();
                    timeslot.setId(resultSet.getInt("id"));
                    timeslot.setDayOfWeek(resultSet.getString("day_of_week"));
                    timeslot.setStartTime(resultSet.getString("start_time"));
                    timeslot.setEndTime(resultSet.getString("end_time"));
                    return timeslot;
                }
            }
        }
        return null;
    }
    
    @Override
    public List<Timeslot> findAll() throws SQLException {
        List<Timeslot> timeslots = new ArrayList<>();
        String sql = "SELECT * FROM Timeslot";
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            while (resultSet.next()) {
                Timeslot timeslot = new Timeslot();
                timeslot.setId(resultSet.getInt("id"));
                timeslot.setDayOfWeek(resultSet.getString("day_of_week"));
                timeslot.setStartTime(resultSet.getString("start_time"));
                timeslot.setEndTime(resultSet.getString("end_time"));
                timeslots.add(timeslot);
            }
        }
        
        return timeslots;
    }
}