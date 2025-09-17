package com.timetable.system;

/**
 * Lecturer entity represents a lecturer in the system.
 */
public class Lecturer extends BaseEntity {
    private String name;
    private String rank;
    
    /**
     * Default constructor.
     */
    public Lecturer() {
        super();
    }
    
    /**
     * Constructor with ID.
     * 
     * @param id the lecturer ID
     */
    public Lecturer(int id) {
        super(id);
    }
    
    /**
     * Constructor with all fields.
     * 
     * @param id the lecturer ID
     * @param name the lecturer name
     * @param rank the lecturer rank
     */
    public Lecturer(int id, String name, String rank) {
        super(id);
        this.name = name;
        this.rank = rank;
    }
    
    /**
     * Gets the lecturer name.
     * 
     * @return the lecturer name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the lecturer name.
     * 
     * @param name the lecturer name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the lecturer rank.
     * 
     * @return the lecturer rank
     */
    public String getRank() {
        return rank;
    }
    
    /**
     * Sets the lecturer rank.
     * 
     * @param rank the lecturer rank to set
     */
    public void setRank(String rank) {
        this.rank = rank;
    }
    
    @Override
    public String toString() {
        return "Lecturer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rank='" + rank + '\'' +
                '}';
    }
}