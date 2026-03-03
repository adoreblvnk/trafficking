package com.sit.trafficking.engine.managers;

import com.sit.trafficking.engine.interfaces.providers.ITimeProvider;

/**
 * Game-level time manager that applies time scaling to platform-provided delta time.
 * Injected with ITimeProvider to achieve platform independence.
 * Values below 1.0 slow time; values above 1.0 accelerate.
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

    /**
     * Sets the time scale multiplier for slow-motion or fast-forward effects.
     *
     * @param scale the scale factor (0.0 pauses, 1.0 is normal, >1.0 is fast)
     */
    public void setTimeScale(float scale) {
        if (scale < 0) {
            this.timeScale = 0;
        } else if (Float.isNaN(scale) || Float.isInfinite(scale)) {
            this.timeScale = 1f;
        } else {
            this.timeScale = scale;
        }
    }

    public float getTimeScale() {
        return timeScale;
    }
}
