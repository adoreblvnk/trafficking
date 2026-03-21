package com.sit.recyclingpinball.engine.interfaces.providers;

/**
 * Platform-independent display interface. Abstracts screen dimensions from any
 * drawing framework.
 */
public interface IDisplay {

    /**
     * Returns the width of the display in pixels.
     */
    int getWidth();

    /**
     * Returns the height of the display in pixels.
     */
    int getHeight();
}
