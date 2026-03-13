package com.sit.recyclingpinball.engine.platform.libgdx;

import com.sit.recyclingpinball.engine.EngineConstants;
import com.sit.recyclingpinball.engine.interfaces.providers.IDisplay;

/**
 * libGDX implementation of IDisplay.
 * Wraps Gdx.graphics to provide screen dimensions.
 */
public class LibGdxDisplay implements IDisplay {

    @Override
    public int getWidth() {
        // Expose the virtual coordinate space size (not physical pixels).
        return EngineConstants.VIRTUAL_WIDTH;
    }

    @Override
    public int getHeight() {
        // Expose the virtual coordinate space size (not physical pixels).
        return EngineConstants.VIRTUAL_HEIGHT;
    }
}
