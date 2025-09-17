package com.timetable.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * TimetableEntryDAO implements data access operations for TimetableEntry entities.
 */
public class TimetableEntryDAO implements DAO<TimetableEntry> {
    
    private Connection connection;
    
    /**
     * Constructs a new TimetableEntryDAO with a database connection.
     * 
     * @throws SQLException if a database access error occurs
     */
    public TimetableEntryDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }
    
    @Override
    public void save(TimetableEntry timetableEntry) throws SQLException {
        if (timetableEntry.isSaved()) {
            update(timetableEntry);
        } else {
            insert(timetableEntry);
        }
    }
    
    @Override
    public void insert(TimetableEntry timetableEntry) throws SQLException {
        String sql = "INSERT INTO TimetableEntry (course_id, lecturer_id, venue_id, timeslot_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, timetableEntry.getCourseId());
            statement.setInt(2, timetableEntry.getLecturerId());
            statement.setInt(3, timetableEntry.getVenueId());
            statement.setInt(4, timetableEntry.getTimeslotId());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating timetable entry failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    timetableEntry.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating timetable entry failed, no ID obtained.");
                }
            }
        }
    }
    
    @Override
    public void update(TimetableEntry timetableEntry) throws SQLException {
        String sql = "UPDATE TimetableEntry SET course_id = ?, lecturer_id = ?, venue_id = ?, timeslot_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, timetableEntry.getCourseId());
            statement.setInt(2, timetableEntry.getLecturerId());
            statement.setInt(3, timetableEntry.getVenueId());
            statement.setInt(4, timetableEntry.getTimeslotId());
            statement.setInt(5, timetableEntry.getId());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating timetable entry failed, no rows affected.");
            }
        }
    }
    
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM TimetableEntry WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Deleting timetable entry failed, no rows affected.");
            }
        }
    }
    
    @Override
    public TimetableEntry findById(int id) throws SQLException {
        String sql = "SELECT * FROM TimetableEntry WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    TimetableEntry timetableEntry = new TimetableEntry();
                    timetableEntry.setId(resultSet.getInt("id"));
                    timetableEntry.setCourseId(resultSet.getInt("course_id"));
                    timetableEntry.setLecturerId(resultSet.getInt("lecturer_id"));
                    timetableEntry.setVenueId(resultSet.getInt("venue_id"));
                    timetableEntry.setTimeslotId(resultSet.getInt("timeslot_id"));
                    return timetableEntry;
                }
            }
        }
        return null;
    }
    
    @Override
    public List<TimetableEntry> findAll() throws SQLException {
        List<TimetableEntry> timetableEntries = new ArrayList<>();
        String sql = "SELECT * FROM TimetableEntry";
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            while (resultSet.next()) {
                TimetableEntry timetableEntry = new TimetableEntry();
                timetableEntry.setId(resultSet.getInt("id"));
                timetableEntry.setCourseId(resultSet.getInt("course_id"));
                timetableEntry.setLecturerId(resultSet.getInt("lecturer_id"));
                timetableEntry.setVenueId(resultSet.getInt("venue_id"));
                timetableEntry.setTimeslotId(resultSet.getInt("timeslot_id"));
                timetableEntries.add(timetableEntry);
            }
        }
        
        return timetableEntries;
    }
    
    /**
     * Finds all timetable entries for a specific lecturer.
     * 
     * @param lecturerId the lecturer ID
     * @return a list of timetable entries for the lecturer
     * @throws SQLException if a database access error occurs
     */
    public List<TimetableEntry> findByLecturerId(int lecturerId) throws SQLException {
        List<TimetableEntry> timetableEntries = new ArrayList<>();
        String sql = "SELECT * FROM TimetableEntry WHERE lecturer_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, lecturerId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    TimetableEntry timetableEntry = new TimetableEntry();
                    timetableEntry.setId(resultSet.getInt("id"));
                    timetableEntry.setCourseId(resultSet.getInt("course_id"));
                    timetableEntry.setLecturerId(resultSet.getInt("lecturer_id"));
                    timetableEntry.setVenueId(resultSet.getInt("venue_id"));
                    timetableEntry.setTimeslotId(resultSet.getInt("timeslot_id"));
                    timetableEntries.add(timetableEntry);
                }
            }
        }
        
        return timetableEntries;
    }
    
    /**
     * Finds all timetable entries for a specific venue.
     * 
     * @param venueId the venue ID
     * @return a list of timetable entries for the venue
     * @throws SQLException if a database access error occurs
     */
    public List<TimetableEntry> findByVenueId(int venueId) throws SQLException {
        List<TimetableEntry> timetableEntries = new ArrayList<>();
        String sql = "SELECT * FROM TimetableEntry WHERE venue_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, venueId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    TimetableEntry timetableEntry = new TimetableEntry();
                    timetableEntry.setId(resultSet.getInt("id"));
                    timetableEntry.setCourseId(resultSet.getInt("course_id"));
                    timetableEntry.setLecturerId(resultSet.getInt("lecturer_id"));
                    timetableEntry.setVenueId(resultSet.getInt("venue_id"));
                    timetableEntry.setTimeslotId(resultSet.getInt("timeslot_id"));
                    timetableEntries.add(timetableEntry);
                }
            }
        }
        
        return timetableEntries;
    }
    
    /**
     * Finds all timetable entries for a specific course.
     * 
     * @param courseId the course ID
     * @return a list of timetable entries for the course
     * @throws SQLException if a database access error occurs
     */
    public List<TimetableEntry> findByCourseId(int courseId) throws SQLException {
        List<TimetableEntry> timetableEntries = new ArrayList<>();
        String sql = "SELECT * FROM TimetableEntry WHERE course_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    TimetableEntry timetableEntry = new TimetableEntry();
                    timetableEntry.setId(resultSet.getInt("id"));
                    timetableEntry.setCourseId(resultSet.getInt("course_id"));
                    timetableEntry.setLecturerId(resultSet.getInt("lecturer_id"));
                    timetableEntry.setVenueId(resultSet.getInt("venue_id"));
                    timetableEntry.setTimeslotId(resultSet.getInt("timeslot_id"));
                    timetableEntries.add(timetableEntry);
                }
            }
        }
        
        return timetableEntries;
    }
}