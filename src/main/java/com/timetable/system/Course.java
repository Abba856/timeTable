package com.timetable.system;

/**
 * Course entity represents a course in the system.
 */
public class Course extends BaseEntity {
    private String title;
    private String level;
    private boolean labRequired;
    
    /**
     * Default constructor.
     */
    public Course() {
        super();
    }
    
    /**
     * Constructor with ID.
     * 
     * @param id the course ID
     */
    public Course(int id) {
        super(id);
    }
    
    /**
     * Constructor with all fields.
     * 
     * @param id the course ID
     * @param title the course title
     * @param level the course level (ND/HND)
     * @param labRequired whether the course requires a lab
     */
    public Course(int id, String title, String level, boolean labRequired) {
        super(id);
        this.title = title;
        this.level = level;
        this.labRequired = labRequired;
    }
    
    /**
     * Gets the course title.
     * 
     * @return the course title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Sets the course title.
     * 
     * @param title the course title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Gets the course level.
     * 
     * @return the course level
     */
    public String getLevel() {
        return level;
    }
    
    /**
     * Sets the course level.
     * 
     * @param level the course level to set
     */
    public void setLevel(String level) {
        this.level = level;
    }
    
    /**
     * Checks if the course requires a lab.
     * 
     * @return true if the course requires a lab, false otherwise
     */
    public boolean isLabRequired() {
        return labRequired;
    }
    
    /**
     * Sets whether the course requires a lab.
     * 
     * @param labRequired true if the course requires a lab, false otherwise
     */
    public void setLabRequired(boolean labRequired) {
        this.labRequired = labRequired;
    }
    
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", level='" + level + '\'' +
                ", labRequired=" + labRequired +
                '}';
    }
}