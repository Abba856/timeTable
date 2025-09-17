package com.timetable.system;

/**
 * Availability entity represents a lecturer's availability in the system.
 */
public class Availability extends BaseEntity {
    private int lecturerId;
    private int timeslotId;
    
    /**
     * Default constructor.
     */
    public Availability() {
        super();
    }
    
    /**
     * Constructor with ID.
     * 
     * @param id the availability ID
     */
    public Availability(int id) {
        super(id);
    }
    
    /**
     * Constructor with all fields.
     * 
     * @param id the availability ID
     * @param lecturerId the lecturer ID
     * @param timeslotId the timeslot ID
     */
    public Availability(int id, int lecturerId, int timeslotId) {
        super(id);
        this.lecturerId = lecturerId;
        this.timeslotId = timeslotId;
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
        return "Availability{" +
                "id=" + id +
                ", lecturerId=" + lecturerId +
                ", timeslotId=" + timeslotId +
                '}';
    }
}