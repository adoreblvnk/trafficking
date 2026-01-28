package com.sit.trafficking.exceptions;

/**
 * Thrown when attempting to add an entity with an ID that is already registered.
 */
public class DuplicateEntityIdException extends EngineException {
    public DuplicateEntityIdException(String id) {
        super("Entity with ID '" + id + "' already exists.");
    }
}
