package com.sit.recyclingpinball.logic.states;

import com.sit.recyclingpinball.logic.entities.PinballEntity;
import com.sit.recyclingpinball.logic.events.IPinballEvent;
import com.sit.recyclingpinball.logic.events.BallLaunchedEvent;
import com.sit.recyclingpinball.logic.events.ShooterRodMovedEvent;

public class IdleState implements IPinballState {
    @Override
    public void update(float dt, PinballEntity ctx) {
        ctx.setVelocity(0, 0);
    }

    @Override
    public boolean onTouchDown(PinballEntity ctx, int x, int y, int ptr, int btn) {
        return false;
    }

    @Override
    public boolean onDrag(PinballEntity ctx, int x, int y, int ptr) { 
        return false; 
    }

    @Override
    public boolean onTouchUp(PinballEntity ctx, int x, int y, int ptr, int btn) { 
        return false; 
    }

    @Override
    public void onEvent(PinballEntity ctx, IPinballEvent event) {
        if (event instanceof BallLaunchedEvent) {
            BallLaunchedEvent launchEvent = (BallLaunchedEvent) event;
            ctx.setState(new InPlayState());
            ctx.getVelocity().y = launchEvent.getLaunchVelocity();
        } else if (event instanceof ShooterRodMovedEvent) {
            ShooterRodMovedEvent rodEvent = (ShooterRodMovedEvent) event;
            float rodTop = rodEvent.getRodY() + 160 + 24; // 160 rod height + 24 pinball radius
            ctx.setPosition(ctx.getPosition().x, rodTop);
        }
    }
}
