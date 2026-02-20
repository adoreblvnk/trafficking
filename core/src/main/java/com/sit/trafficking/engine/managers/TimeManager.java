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
        if (scale < 0) {
            Gdx.app.log("TimeManager", "Negative time scale rejected, using 0: " + scale);
            this.timeScale = 0;
        } else if (Float.isNaN(scale) || Float.isInfinite(scale)) {
            Gdx.app.error("TimeManager", "Invalid time scale rejected: " + scale);
            this.timeScale = 1f;
        } else {
            this.timeScale = scale;
        }
    }

    public float getTimeScale() {
        return timeScale;
    }
}
