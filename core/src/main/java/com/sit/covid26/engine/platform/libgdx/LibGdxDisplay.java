package com.sit.covid26.engine.platform.libgdx;

import com.badlogic.gdx.Gdx;
import com.sit.covid26.engine.interfaces.providers.IDisplay;

/**
 * libGDX implementation of IDisplay.
 * Wraps Gdx.graphics to provide screen dimensions.
 */
public class LibGdxDisplay implements IDisplay {

    @Override
    public int getWidth() {
        return Gdx.graphics.getWidth();
    }

    @Override
    public int getHeight() {
        return Gdx.graphics.getHeight();
    }
}
