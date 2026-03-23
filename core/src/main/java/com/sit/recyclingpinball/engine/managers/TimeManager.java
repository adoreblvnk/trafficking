package com.sit.recyclingpinball.engine.managers;

import com.sit.recyclingpinball.engine.interfaces.providers.ITimeProvider;

/**
 * Game-level time manager that applies time scaling to platform-provided delta
 * time. Injected with ITimeProvider to achieve platform independence. Values
 * below 1.0 slow time; values above 1.0 accelerate.
 */
public class TimeManager {

    private final ITimeProvider timeProvider;
    private float timeScale = 1.0f;

    public TimeManager(ITimeProvider timeProvider) {
        if (timeProvider == null) {
            throw new IllegalArgumentException("TimeProvider cannot be null");
        }
        this.timeProvider = timeProvider;
    }

    /**
     * Returns the platform delta time multiplied by the game's time scale.
     */
    public float getDeltaTime() {
        return timeProvider.getDeltaTime() * timeScale;
    }
}
