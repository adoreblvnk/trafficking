package com.sit.recyclingpinball.engine.platform.libgdx;

import com.badlogic.gdx.Gdx;
import com.sit.recyclingpinball.engine.interfaces.providers.ITimeProvider;

/**
 * libGDX implementation of ITimeProvider. Wraps Gdx.graphics.getDeltaTime()
 * with optional time scaling.
 */
public class LibGdxTime implements ITimeProvider {

    private float timeScale = 1.0f;

    public LibGdxTime() {
    }

    @Override
    public float getDeltaTime() {
        return Gdx.graphics.getDeltaTime() * timeScale;
    }

    /**
     * Sets the time scale multiplier for slow-motion or fast-forward effects.
     *
     * @param scale
     *            the scale factor (0.0 pauses, 1.0 is normal, >1.0 is fast)
     */
    public void setTimeScale(float scale) {
        if (scale < 0) {
            Gdx.app.log("LibGdxTime", "Negative time scale rejected, using 0: " + scale);
            this.timeScale = 0;
        } else if (Float.isNaN(scale) || Float.isInfinite(scale)) {
            Gdx.app.error("LibGdxTime", "Invalid time scale rejected: " + scale);
            this.timeScale = 1f;
        } else {
            this.timeScale = scale;
        }
    }

    public float getTimeScale() {
        return timeScale;
    }
}
