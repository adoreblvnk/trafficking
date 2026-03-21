package com.sit.recyclingpinball.engine.interfaces;

import com.sit.recyclingpinball.engine.interfaces.providers.EngineKey;

/**
 * Enables objects to respond to user input events.
 */
public interface InputListener {
    boolean onTouchDown(int x, int y, int ptr, int btn);
    boolean onDrag(int x, int y, int ptr);
    boolean onTouchUp(int x, int y, int ptr, int btn);
    default boolean onKeyDown(EngineKey keycode) {
        return false;
    }
    default boolean onKeyUp(EngineKey keycode) {
        return false;
    }
}
