package com.timetable.system;

/**
 * Venue entity represents a venue in the system.
 */
public class Venue extends BaseEntity {
    private String name;
    private int capacity;
    private String type;
    
    /**
     * Default constructor.
     */
    public Venue() {
        super();
    }
    
    /**
     * Constructor with ID.
     * 
     * @param id the venue ID
     */
    public Venue(int id) {
        super(id);
    }
    
    /**
     * Constructor with all fields.
     * 
     * @param id the venue ID
     * @param name the venue name
     * @param capacity the venue capacity
     * @param type the venue type (Lecture Hall/Lab)
     */
    public Venue(int id, String name, int capacity, String type) {
        super(id);
        this.name = name;
        this.capacity = capacity;
        this.type = type;
    }
    
    /**
     * Gets the venue name.
     * 
     * @return the venue name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the venue name.
     * 
     * @param name the venue name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the venue capacity.
     * 
     * @return the venue capacity
     */
    public int getCapacity() {
        return capacity;
    }
    
    /**
     * Sets the venue capacity.
     * 
     * @param capacity the venue capacity to set
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    /**
     * Gets the venue type.
     * 
     * @return the venue type
     */
    public String getType() {
        return type;
    }
    
    /**
     * Sets the venue type.
     * 
     * @param type the venue type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return "Venue{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", type='" + type + '\'' +
                '}';
    }
}