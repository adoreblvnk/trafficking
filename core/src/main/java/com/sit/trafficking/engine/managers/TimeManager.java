package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.Gdx;

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
