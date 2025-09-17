package com.timetable.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * AvailabilityDAO implements data access operations for Availability entities.
 */
public class AvailabilityDAO implements DAO<Availability> {
    
    private Connection connection;
    
    /**
     * Constructs a new AvailabilityDAO with a database connection.
     * 
     * @throws SQLException if a database access error occurs
     */
    public AvailabilityDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }
    
    @Override
    public void save(Availability availability) throws SQLException {
        if (availability.isSaved()) {
            update(availability);
        } else {
            insert(availability);
        }
    }
    
    @Override
    public void insert(Availability availability) throws SQLException {
        String sql = "INSERT INTO Availability (lecturer_id, timeslot_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, availability.getLecturerId());
            statement.setInt(2, availability.getTimeslotId());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating availability failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    availability.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating availability failed, no ID obtained.");
                }
            }
        }
    }
    
    @Override
    public void update(Availability availability) throws SQLException {
        String sql = "UPDATE Availability SET lecturer_id = ?, timeslot_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, availability.getLecturerId());
            statement.setInt(2, availability.getTimeslotId());
            statement.setInt(3, availability.getId());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating availability failed, no rows affected.");
            }
        }
    }
    
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Availability WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Deleting availability failed, no rows affected.");
            }
        }
    }
    
    @Override
    public Availability findById(int id) throws SQLException {
        String sql = "SELECT * FROM Availability WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Availability availability = new Availability();
                    availability.setId(resultSet.getInt("id"));
                    availability.setLecturerId(resultSet.getInt("lecturer_id"));
                    availability.setTimeslotId(resultSet.getInt("timeslot_id"));
                    return availability;
                }
            }
        }
        return null;
    }
    
    @Override
    public List<Availability> findAll() throws SQLException {
        List<Availability> availabilities = new ArrayList<>();
        String sql = "SELECT * FROM Availability";
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            while (resultSet.next()) {
                Availability availability = new Availability();
                availability.setId(resultSet.getInt("id"));
                availability.setLecturerId(resultSet.getInt("lecturer_id"));
                availability.setTimeslotId(resultSet.getInt("timeslot_id"));
                availabilities.add(availability);
            }
        }
        
        return availabilities;
    }
    
    /**
     * Finds all availabilities for a specific lecturer.
     * 
     * @param lecturerId the lecturer ID
     * @return a list of availabilities for the lecturer
     * @throws SQLException if a database access error occurs
     */
    public List<Availability> findByLecturerId(int lecturerId) throws SQLException {
        List<Availability> availabilities = new ArrayList<>();
        String sql = "SELECT * FROM Availability WHERE lecturer_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, lecturerId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Availability availability = new Availability();
                    availability.setId(resultSet.getInt("id"));
                    availability.setLecturerId(resultSet.getInt("lecturer_id"));
                    availability.setTimeslotId(resultSet.getInt("timeslot_id"));
                    availabilities.add(availability);
                }
            }
        }
        
        return availabilities;
    }
}