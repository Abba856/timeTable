package com.timetable.system;

/**
 * BaseEntity class serves as the base class for all entities in the system.
 * It provides common functionality such as ID management.
 */
public abstract class BaseEntity {
    protected int id;
    
    /**
     * Default constructor.
     */
    public BaseEntity() {
        this.id = -1; // Default value indicating unsaved entity
    }
    
    /**
     * Constructor with ID.
     * 
     * @param id the entity ID
     */
    public BaseEntity(int id) {
        this.id = id;
    }
    
    /**
     * Gets the entity ID.
     * 
     * @return the entity ID
     */
    public int getId() {
        return id;
    }
    
    /**
     * Sets the entity ID.
     * 
     * @param id the entity ID to set
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Checks if the entity is saved (has a valid ID).
     * 
     * @return true if the entity is saved, false otherwise
     */
    public boolean isSaved() {
        return id > 0;
    }
}