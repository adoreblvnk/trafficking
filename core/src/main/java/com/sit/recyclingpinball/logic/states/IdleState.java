package com.sit.recyclingpinball.logic.states;

import com.sit.recyclingpinball.logic.entities.PinballEntity;

public class IdleState implements IPinballState {
    @Override
    public void update(float dt, PinballEntity ctx) {
        ctx.setVelocity(0, 0);
    }

    @Override
    public boolean onTouchDown(PinballEntity ctx, int x, int y, int ptr, int btn) {
        // If they click reasonably close to the pinball, start dragging
        float px = ctx.getPosition().x;
        float py = ctx.getPosition().y;
        // Invert Y because screen Y goes down, but world Y goes up.
        // Wait, screen input is typically top-left 0,0, world is bottom-left 0,0.
        // For simplicity, let's just transition to dragging state on any touch.
        ctx.setState(new DraggingState(x, y));
        return true;
    }

    @Override
    public boolean onDrag(PinballEntity ctx, int x, int y, int ptr) { return false; }
    @Override
    public boolean onTouchUp(PinballEntity ctx, int x, int y, int ptr, int btn) { return false; }
}
