package com.timetable.system;

/**
 * Timeslot entity represents a timeslot in the system.
 */
public class Timeslot extends BaseEntity {
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    
    /**
     * Default constructor.
     */
    public Timeslot() {
        super();
    }
    
    /**
     * Constructor with ID.
     * 
     * @param id the timeslot ID
     */
    public Timeslot(int id) {
        super(id);
    }
    
    /**
     * Constructor with all fields.
     * 
     * @param id the timeslot ID
     * @param dayOfWeek the day of week
     * @param startTime the start time
     * @param endTime the end time
     */
    public Timeslot(int id, String dayOfWeek, String startTime, String endTime) {
        super(id);
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    /**
     * Gets the day of week.
     * 
     * @return the day of week
     */
    public String getDayOfWeek() {
        return dayOfWeek;
    }
    
    /**
     * Sets the day of week.
     * 
     * @param dayOfWeek the day of week to set
     */
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    
    /**
     * Gets the start time.
     * 
     * @return the start time
     */
    public String getStartTime() {
        return startTime;
    }
    
    /**
     * Sets the start time.
     * 
     * @param startTime the start time to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    
    /**
     * Gets the end time.
     * 
     * @return the end time
     */
    public String getEndTime() {
        return endTime;
    }
    
    /**
     * Sets the end time.
     * 
     * @param endTime the end time to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    
    @Override
    public String toString() {
        return "Timeslot{" +
                "id=" + id +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}