package com.timetable.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * VenueDAO implements data access operations for Venue entities.
 */
public class VenueDAO implements DAO<Venue> {
    
    private Connection connection;
    
    /**
     * Constructs a new VenueDAO with a database connection.
     * 
     * @throws SQLException if a database access error occurs
     */
    public VenueDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }
    
    @Override
    public void save(Venue venue) throws SQLException {
        if (venue.isSaved()) {
            update(venue);
        } else {
            insert(venue);
        }
    }
    
    @Override
    public void insert(Venue venue) throws SQLException {
        String sql = "INSERT INTO Venue (name, capacity, type) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, venue.getName());
            statement.setInt(2, venue.getCapacity());
            statement.setString(3, venue.getType());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating venue failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    venue.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating venue failed, no ID obtained.");
                }
            }
        }
    }
    
    @Override
    public void update(Venue venue) throws SQLException {
        String sql = "UPDATE Venue SET name = ?, capacity = ?, type = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, venue.getName());
            statement.setInt(2, venue.getCapacity());
            statement.setString(3, venue.getType());
            statement.setInt(4, venue.getId());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating venue failed, no rows affected.");
            }
        }
    }
    
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Venue WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Deleting venue failed, no rows affected.");
            }
        }
    }
    
    @Override
    public Venue findById(int id) throws SQLException {
        String sql = "SELECT * FROM Venue WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Venue venue = new Venue();
                    venue.setId(resultSet.getInt("id"));
                    venue.setName(resultSet.getString("name"));
                    venue.setCapacity(resultSet.getInt("capacity"));
                    venue.setType(resultSet.getString("type"));
                    return venue;
                }
            }
        }
        return null;
    }
    
    @Override
    public List<Venue> findAll() throws SQLException {
        List<Venue> venues = new ArrayList<>();
        String sql = "SELECT * FROM Venue";
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            while (resultSet.next()) {
                Venue venue = new Venue();
                venue.setId(resultSet.getInt("id"));
                venue.setName(resultSet.getString("name"));
                venue.setCapacity(resultSet.getInt("capacity"));
                venue.setType(resultSet.getString("type"));
                venues.add(venue);
            }
        }
        
        return venues;
    }
}