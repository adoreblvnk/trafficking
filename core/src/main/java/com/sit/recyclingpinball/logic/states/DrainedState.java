package com.sit.recyclingpinball.logic.states;

import com.sit.recyclingpinball.logic.entities.IPinballEntityContext;

public class DrainedState implements IPinballState {

    public DrainedState() {
    }

    @Override
    public void update(float dt, IPinballEntityContext ctx) {
        ctx.setVelocity(0, 0);
    }
}
