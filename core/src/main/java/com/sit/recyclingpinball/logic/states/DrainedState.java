package com.sit.recyclingpinball.logic.states;

import com.sit.recyclingpinball.logic.entities.PinballEntity;

public class DrainedState implements IPinballState {
    @Override
    public void update(float dt, PinballEntity ctx) {
        ctx.setVelocity(0, 0);
    }

    @Override
    public boolean onTouchDown(PinballEntity ctx, int x, int y, int ptr, int btn) { return false; }
    @Override
    public boolean onDrag(PinballEntity ctx, int x, int y, int ptr) { return false; }
    @Override
    public boolean onTouchUp(PinballEntity ctx, int x, int y, int ptr, int btn) { return false; }
}
