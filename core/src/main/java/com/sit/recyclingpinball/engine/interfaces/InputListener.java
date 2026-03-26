package com.sit.recyclingpinball.engine.interfaces;

import com.sit.recyclingpinball.engine.platform.libgdx.PlatformKey;

/**
 * Enables objects to respond to user input events.
 */
public interface InputListener {
    boolean onTouchDown(int x, int y, int ptr, int btn);
    boolean onDrag(int x, int y, int ptr);
    boolean onTouchUp(int x, int y, int ptr, int btn);
    default boolean onKeyDown(PlatformKey keycode) {
        return false;
    }
    default boolean onKeyUp(PlatformKey keycode) {
        return false;
    }
}
