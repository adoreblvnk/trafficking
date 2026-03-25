package com.sit.recyclingpinball.logic.states;

import com.sit.recyclingpinball.logic.entities.IPinballEntityContext;
import com.sit.recyclingpinball.logic.events.BallDrainedEvent;

public class InPlayState implements IPinballState {

    public InPlayState() {
    }

    @Override
    public void update(float dt, IPinballEntityContext ctx) {
        // Apply gravity
        ctx.getVelocity().setY(ctx.getVelocity().getY() - 900f * dt);

        if (ctx.getPosition().getY() < -50) {
            ctx.setState(ctx.getStateFactory().createDrainedState());
            ctx.getEventBus().post(new BallDrainedEvent());
        }
    }
}
