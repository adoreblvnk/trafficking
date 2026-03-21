package com.sit.recyclingpinball.logic.states;

import com.sit.recyclingpinball.logic.entities.PinballEntity;
import com.sit.recyclingpinball.logic.events.PinballEventVisitor;

public interface IPinballState extends PinballEventVisitor {
    void update(float dt, PinballEntity ctx);
    default boolean onTouchDown(PinballEntity ctx, int x, int y, int ptr, int btn) { return false; }
    default boolean onDrag(PinballEntity ctx, int x, int y, int ptr) { return false; }
    default boolean onTouchUp(PinballEntity ctx, int x, int y, int ptr, int btn) { return false; }
}
