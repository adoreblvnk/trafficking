package com.sit.recyclingpinball.logic.states;

import com.sit.recyclingpinball.logic.entities.PinballEntity;
import com.sit.recyclingpinball.logic.events.BallDrainedEvent;

public class InPlayState implements IPinballState {
    @Override
    public void update(float dt, PinballEntity ctx) {
        // Apply gravity
        ctx.getVelocity().y -= 900f * dt;

        if (ctx.getPosition().y < -50) {
            ctx.setState(new DrainedState());
            ctx.getEventBus().post(new BallDrainedEvent());
        }
    }

    @Override
    public boolean onTouchDown(PinballEntity ctx, int x, int y, int ptr, int btn) { return false; }
    @Override
    public boolean onDrag(PinballEntity ctx, int x, int y, int ptr) { return false; }
    @Override
    public boolean onTouchUp(PinballEntity ctx, int x, int y, int ptr, int btn) { return false; }
}
