package com.sit.recyclingpinball.logic.states;

import com.sit.recyclingpinball.logic.entities.IPinballEntityContext;
import com.sit.recyclingpinball.logic.events.BallLaunchedEvent;
import com.sit.recyclingpinball.logic.events.ShooterRodMovedEvent;
import com.sit.recyclingpinball.logic.LogicConstants;

public class IdleState implements IPinballState {
    private final IPinballEntityContext ctx;

    public IdleState(IPinballEntityContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void update(float dt, IPinballEntityContext ctx) {
        ctx.setVelocity(0, 0);
    }

    @Override
    public void visit(BallLaunchedEvent launchEvent) {
        ctx.setState(ctx.getStateFactory().createInPlayState());
        ctx.getVelocity().setY(launchEvent.getLaunchVelocity());
    }

    @Override
    public void visit(ShooterRodMovedEvent rodEvent) {
        float rodTop = rodEvent.getRodY() + LogicConstants.SHOOTER_SIZE[1] + (LogicConstants.PINBALL_SIZE / 2f);
        ctx.setPosition(ctx.getPosition().getX(), rodTop);
    }
}
