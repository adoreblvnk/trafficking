package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.Gdx;

/**
 * Provides scaled delta time for controlling simulation speed.
 * Values below 1.0 slow time; values above 1.0 accelerate.
 */
public class TimeManager {
    
    private float timeScale = 1.0f;

    public TimeManager() {
    }

    public float getDeltaTime() {
        return Gdx.graphics.getDeltaTime() * timeScale;
    }

    public void setTimeScale(float scale) {
        if (scale < 0) this.timeScale = 0;
        else this.timeScale = scale;
    }

    public float getTimeScale() {
        return timeScale;
    }
}
