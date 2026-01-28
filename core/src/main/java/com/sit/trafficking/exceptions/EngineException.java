package com.sit.trafficking.exceptions;

/**
 * Base unchecked exception for all engine-related errors.
 */
public class EngineException extends RuntimeException {
    public EngineException(String message) {
        super(message);
    }
}
