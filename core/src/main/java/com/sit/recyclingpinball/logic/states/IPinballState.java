package com.sit.recyclingpinball.logic.states;

import com.sit.recyclingpinball.logic.entities.PinballEntity;

public interface IPinballState {
    void update(float dt, PinballEntity ctx);
    boolean onTouchDown(PinballEntity ctx, int x, int y, int ptr, int btn);
    boolean onDrag(PinballEntity ctx, int x, int y, int ptr);
    boolean onTouchUp(PinballEntity ctx, int x, int y, int ptr, int btn);
}
