package com.timetable.system;

/**
 * TimetableEntry entity represents an entry in the timetable.
 */
public class TimetableEntry extends BaseEntity {
    private int courseId;
    private int lecturerId;
    private int venueId;
    private int timeslotId;
    
    /**
     * Default constructor.
     */
    public TimetableEntry() {
        super();
    }
    
    /**
     * Constructor with ID.
     * 
     * @param id the timetable entry ID
     */
    public TimetableEntry(int id) {
        super(id);
    }
    
    /**
     * Constructor with all fields.
     * 
     * @param id the timetable entry ID
     * @param courseId the course ID
     * @param lecturerId the lecturer ID
     * @param venueId the venue ID
     * @param timeslotId the timeslot ID
     */
    public TimetableEntry(int id, int courseId, int lecturerId, int venueId, int timeslotId) {
        super(id);
        this.courseId = courseId;
        this.lecturerId = lecturerId;
        this.venueId = venueId;
        this.timeslotId = timeslotId;
    }
    
    /**
     * Gets the course ID.
     * 
     * @return the course ID
     */
    public int getCourseId() {
        return courseId;
    }
    
    /**
     * Sets the course ID.
     * 
     * @param courseId the course ID to set
     */
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    
    /**
     * Gets the lecturer ID.
     * 
     * @return the lecturer ID
     */
    public int getLecturerId() {
        return lecturerId;
    }
    
    /**
     * Sets the lecturer ID.
     * 
     * @param lecturerId the lecturer ID to set
     */
    public void setLecturerId(int lecturerId) {
        this.lecturerId = lecturerId;
    }
    
    /**
     * Gets the venue ID.
     * 
     * @return the venue ID
     */
    public int getVenueId() {
        return venueId;
    }
    
    /**
     * Sets the venue ID.
     * 
     * @param venueId the venue ID to set
     */
    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }
    
    /**
     * Gets the timeslot ID.
     * 
     * @return the timeslot ID
     */
    public int getTimeslotId() {
        return timeslotId;
    }
    
    /**
     * Sets the timeslot ID.
     * 
     * @param timeslotId the timeslot ID to set
     */
    public void setTimeslotId(int timeslotId) {
        this.timeslotId = timeslotId;
    }
    
    @Override
    public String toString() {
        return "TimetableEntry{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", lecturerId=" + lecturerId +
                ", venueId=" + venueId +
                ", timeslotId=" + timeslotId +
                '}';
    }
}