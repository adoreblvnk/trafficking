package com.sit.recyclingpinball.logic.states;

import com.sit.recyclingpinball.logic.entities.PinballEntity;
import com.sit.recyclingpinball.logic.events.BallLaunchedEvent;
import com.sit.recyclingpinball.logic.events.ShooterRodMovedEvent;

public class IdleState implements IPinballState {
    private final PinballEntity ctx;

    public IdleState(PinballEntity ctx) {
        this.ctx = ctx;
    }

    @Override
    public void update(float dt, PinballEntity ctx) {
        ctx.setVelocity(0, 0);
    }

    @Override
    public void visit(BallLaunchedEvent launchEvent) {
        ctx.setState(ctx.getStateFactory().createInPlayState());
        ctx.getVelocity().setY(launchEvent.getLaunchVelocity());
    }

    @Override
    public void visit(ShooterRodMovedEvent rodEvent) {
        float rodTop = rodEvent.getRodY() + 160 + 24; // 160 rod height + 24 pinball radius
        ctx.setPosition(ctx.getPosition().getX(), rodTop);
    }
}
