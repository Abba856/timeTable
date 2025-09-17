package com.timetable.system;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Lecturer class.
 */
public class LecturerTest {
    
    @Test
    public void testDefaultConstructor() {
        Lecturer lecturer = new Lecturer();
        assertEquals(-1, lecturer.getId());
        assertNull(lecturer.getName());
        assertNull(lecturer.getRank());
    }
    
    @Test
    public void testConstructorWithId() {
        Lecturer lecturer = new Lecturer(1);
        assertEquals(1, lecturer.getId());
        assertNull(lecturer.getName());
        assertNull(lecturer.getRank());
    }
    
    @Test
    public void testConstructorWithAllFields() {
        Lecturer lecturer = new Lecturer(1, "John Doe", "Professor");
        assertEquals(1, lecturer.getId());
        assertEquals("John Doe", lecturer.getName());
        assertEquals("Professor", lecturer.getRank());
    }
    
    @Test
    public void testSettersAndGetters() {
        Lecturer lecturer = new Lecturer();
        
        lecturer.setId(1);
        assertEquals(1, lecturer.getId());
        
        lecturer.setName("John Doe");
        assertEquals("John Doe", lecturer.getName());
        
        lecturer.setRank("Professor");
        assertEquals("Professor", lecturer.getRank());
    }
    
    @Test
    public void testIsSaved() {
        Lecturer lecturer = new Lecturer();
        assertFalse(lecturer.isSaved());
        
        lecturer.setId(1);
        assertTrue(lecturer.isSaved());
    }
    
    @Test
    public void testToString() {
        Lecturer lecturer = new Lecturer(1, "John Doe", "Professor");
        String expected = "Lecturer{id=1, name='John Doe', rank='Professor'}";
        assertEquals(expected, lecturer.toString());
    }
}