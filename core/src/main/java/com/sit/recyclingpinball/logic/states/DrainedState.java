package com.sit.recyclingpinball.logic.states;

import com.sit.recyclingpinball.logic.entities.PinballEntity;

public class DrainedState implements IPinballState {

    public DrainedState() {
    }

    @Override
    public void update(float dt, PinballEntity ctx) {
        ctx.setVelocity(0, 0);
    }
}
