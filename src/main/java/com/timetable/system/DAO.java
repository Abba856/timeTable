package com.timetable.system;

import java.sql.SQLException;
import java.util.List;

/**
 * Generic DAO interface defines common database operations for all entities.
 * 
 * @param <T> the entity type
 */
public interface DAO<T extends BaseEntity> {
    
    /**
     * Saves an entity to the database.
     * If the entity is not saved (id <= 0), it will be inserted.
     * If the entity is already saved (id > 0), it will be updated.
     * 
     * @param entity the entity to save
     * @throws SQLException if a database access error occurs
     */
    void save(T entity) throws SQLException;
    
    /**
     * Inserts a new entity into the database.
     * 
     * @param entity the entity to insert
     * @throws SQLException if a database access error occurs
     */
    void insert(T entity) throws SQLException;
    
    /**
     * Updates an existing entity in the database.
     * 
     * @param entity the entity to update
     * @throws SQLException if a database access error occurs
     */
    void update(T entity) throws SQLException;
    
    /**
     * Deletes an entity from the database by ID.
     * 
     * @param id the entity ID to delete
     * @throws SQLException if a database access error occurs
     */
    void delete(int id) throws SQLException;
    
    /**
     * Finds an entity by ID.
     * 
     * @param id the entity ID to find
     * @return the entity, or null if not found
     * @throws SQLException if a database access error occurs
     */
    T findById(int id) throws SQLException;
    
    /**
     * Finds all entities of this type.
     * 
     * @return a list of all entities
     * @throws SQLException if a database access error occurs
     */
    List<T> findAll() throws SQLException;
}