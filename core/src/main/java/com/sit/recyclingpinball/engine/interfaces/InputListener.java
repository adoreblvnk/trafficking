package com.sit.recyclingpinball.engine.interfaces;

/**
 * Enables objects to respond to user input events.
 */
public interface InputListener {
    boolean onTouchDown(int x, int y, int ptr, int btn);
    boolean onDrag(int x, int y, int ptr);
    boolean onTouchUp(int x, int y, int ptr, int btn);
    default boolean onKeyDown(int keycode) { return false; }
    default boolean onKeyUp(int keycode) { return false; }
}
