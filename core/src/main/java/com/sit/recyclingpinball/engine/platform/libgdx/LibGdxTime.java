package com.sit.recyclingpinball.engine.platform.libgdx;

import com.badlogic.gdx.Gdx;
import com.sit.recyclingpinball.engine.interfaces.providers.ITimeProvider;

/**
 * libGDX implementation of ITimeProvider. Wraps Gdx.graphics.getDeltaTime()
 * with optional time scaling.
 */
public class LibGdxTime implements ITimeProvider {

    public LibGdxTime() {
    }

    @Override
    public float getDeltaTime() {
        return Gdx.graphics.getDeltaTime();
    }
}
