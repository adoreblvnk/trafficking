package com.sit.trafficking.utils;

import com.badlogic.gdx.Gdx;

/**
 * Singleton class to manage game time.
 * allowing for time scaling (slow motion, speed up).
 */
public class TimeManager {
    
    private static TimeManager instance;
    private float timeScale = 1.0f;

    private TimeManager() {
        // Private constructor for Singleton pattern
    }

    /**
     * @return The single instance of TimeManager.
     */
    public static TimeManager getInstance() {
        if (instance == null) {
            instance = new TimeManager();
        }
        return instance;
    }

    /**
     * @return The scaled delta time.
     */
    public float getDeltaTime() {
        return Gdx.graphics.getDeltaTime() * timeScale;
    }

    public void setTimeScale(float timeScale) {
        this.timeScale = timeScale;
    }

    public float getTimeScale() {
        return timeScale;
    }
}
