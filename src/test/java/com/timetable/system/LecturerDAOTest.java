package com.timetable.system;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Unit tests for the LecturerDAO class.
 */
public class LecturerDAOTest {
    
    private LecturerDAO lecturerDAO;
    
    @Before
    public void setUp() throws SQLException {
        // Initialize the database
        DatabaseInitializer.initializeDatabase();
        lecturerDAO = new LecturerDAO();
    }
    
    @After
    public void tearDown() throws SQLException {
        // Clean up the database
        DatabaseConnection.closeConnection();
    }
    
    @Test
    public void testInsert() throws SQLException {
        Lecturer lecturer = new Lecturer();
        lecturer.setName("John Doe");
        lecturer.setRank("Professor");
        
        lecturerDAO.insert(lecturer);
        
        // Verify the lecturer was inserted
        assertTrue(lecturer.isSaved());
        assertTrue(lecturer.getId() > 0);
    }
    
    @Test
    public void testFindById() throws SQLException {
        // Insert a lecturer first
        Lecturer lecturer = new Lecturer();
        lecturer.setName("John Doe");
        lecturer.setRank("Professor");
        lecturerDAO.insert(lecturer);
        
        // Find the lecturer by ID
        Lecturer found = lecturerDAO.findById(lecturer.getId());
        
        // Verify the lecturer was found
        assertNotNull(found);
        assertEquals(lecturer.getId(), found.getId());
        assertEquals("John Doe", found.getName());
        assertEquals("Professor", found.getRank());
    }
    
    @Test
    public void testFindAll() throws SQLException {
        // Insert a couple of lecturers
        Lecturer lecturer1 = new Lecturer();
        lecturer1.setName("John Doe");
        lecturer1.setRank("Professor");
        lecturerDAO.insert(lecturer1);
        
        Lecturer lecturer2 = new Lecturer();
        lecturer2.setName("Jane Smith");
        lecturer2.setRank("Associate Professor");
        lecturerDAO.insert(lecturer2);
        
        // Find all lecturers
        List<Lecturer> lecturers = lecturerDAO.findAll();
        
        // Verify we found the lecturers
        assertNotNull(lecturers);
        assertTrue(lecturers.size() >= 2);
    }
    
    @Test
    public void testUpdate() throws SQLException {
        // Insert a lecturer first
        Lecturer lecturer = new Lecturer();
        lecturer.setName("John Doe");
        lecturer.setRank("Professor");
        lecturerDAO.insert(lecturer);
        
        // Update the lecturer
        lecturer.setName("John Smith");
        lecturer.setRank("Associate Professor");
        lecturerDAO.update(lecturer);
        
        // Find the updated lecturer
        Lecturer updated = lecturerDAO.findById(lecturer.getId());
        
        // Verify the lecturer was updated
        assertNotNull(updated);
        assertEquals("John Smith", updated.getName());
        assertEquals("Associate Professor", updated.getRank());
    }
    
    @Test
    public void testDelete() throws SQLException {
        // Insert a lecturer first
        Lecturer lecturer = new Lecturer();
        lecturer.setName("John Doe");
        lecturer.setRank("Professor");
        lecturerDAO.insert(lecturer);
        
        // Delete the lecturer
        lecturerDAO.delete(lecturer.getId());
        
        // Try to find the deleted lecturer
        Lecturer deleted = lecturerDAO.findById(lecturer.getId());
        
        // Verify the lecturer was deleted
        assertNull(deleted);
    }
}