package com.sit.trafficking.engine.interfaces.providers;

/**
 * Platform-independent time provider interface.
 * Abstracts frame delta time from any game framework.
 */
public interface ITimeProvider {
    
    /**
     * Returns the time elapsed since the last frame in seconds.
     */
    float getDeltaTime();
}
