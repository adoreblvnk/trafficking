package com.sit.recyclingpinball.logic.states;

import com.sit.recyclingpinball.logic.entities.IPinballEntityContext;
import com.sit.recyclingpinball.logic.events.PinballEventVisitor;

public interface IPinballState extends PinballEventVisitor {
    void update(float dt, IPinballEntityContext ctx);
    default boolean onTouchDown(IPinballEntityContext ctx, int x, int y, int ptr, int btn) {
        return false;
    }
    default boolean onDrag(IPinballEntityContext ctx, int x, int y, int ptr) {
        return false;
    }
    default boolean onTouchUp(IPinballEntityContext ctx, int x, int y, int ptr, int btn) {
        return false;
    }
}
