package com.sit.trafficking.exceptions;

/**
 * Thrown when an operation is performed on an entity ID that does not exist in the EntityManager.
 */
public class EntityNotFoundException extends EngineException {
    public EntityNotFoundException(String id) {
        super("Entity with ID '" + id + "' not found.");
    }
}
