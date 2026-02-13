package com.sit.trafficking.engine.interfaces;

public interface InputListener {
    boolean onTouchDown(int x, int y, int ptr, int btn);
    boolean onDrag(int x, int y, int ptr);
    boolean onTouchUp(int x, int y, int ptr, int btn);

    default boolean onKeyDown(int keycode) { return false; }
}
