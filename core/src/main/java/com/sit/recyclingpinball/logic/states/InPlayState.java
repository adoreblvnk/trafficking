package com.sit.recyclingpinball.logic.states;

import com.sit.recyclingpinball.logic.entities.PinballEntity;
import com.sit.recyclingpinball.logic.events.BallDrainedEvent;

public class InPlayState implements IPinballState {
    private final PinballEntity ctx;

    public InPlayState(PinballEntity ctx) {
        this.ctx = ctx;
    }

    @Override
    public void update(float dt, PinballEntity ctx) {
        // Apply gravity
        ctx.getVelocity().setY(ctx.getVelocity().getY() - 900f * dt);

        if (ctx.getPosition().getY() < -50) {
            ctx.setState(new DrainedState(ctx));
            ctx.getEventBus().post(new BallDrainedEvent());
        }
    }
}
