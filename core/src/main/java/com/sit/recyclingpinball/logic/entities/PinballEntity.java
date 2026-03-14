package com.sit.recyclingpinball.logic.entities;

import com.sit.recyclingpinball.engine.entities.DynamicEntity;
import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.engine.interfaces.ICollidable;
import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;
import com.sit.recyclingpinball.engine.physics.CircleCollider;
import com.sit.recyclingpinball.logic.states.IPinballState;
import com.sit.recyclingpinball.logic.states.IdleState;
import com.sit.recyclingpinball.logic.states.InPlayState;
import com.sit.recyclingpinball.logic.events.PinballEventBus;
import com.sit.recyclingpinball.logic.events.PinballEventListener;
import com.sit.recyclingpinball.logic.events.IPinballEvent;
import com.sit.recyclingpinball.logic.events.BallLaunchedEvent;

public class PinballEntity extends DynamicEntity implements InputListener, PinballEventListener {
    private IPinballState currentState;
    private final PinballEventBus eventBus;

    public PinballEntity(String id, float x, float y, PinballEventBus eventBus) {
        super(id, x, y, 48, 48);
        this.collider = new CircleCollider(x, y, 24);
        this.eventBus = eventBus;
        this.currentState = new InPlayState(); // Start in play state to allow falling onto shooter rod
        this.eventBus.register(this);
        setCollisionEnabled(true);
        setFriction(0.999f);
    }

    public void setState(IPinballState state) {
        this.currentState = state;
    }

    public PinballEventBus getEventBus() {
        return eventBus;
    }

    @Override
    public void update(float dt) {
        currentState.update(dt, this);
        super.update(dt);
    }

    @Override
    public void render(IGraphicsProvider graphics) {
        graphics.drawTexture("pinball_default", getPosition().x - 24, getPosition().y - 24, 48, 48);
    }

    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) {
        return currentState.onTouchDown(this, x, y, ptr, btn);
    }

    @Override
    public boolean onDrag(int x, int y, int ptr) {
        return currentState.onDrag(this, x, y, ptr);
    }

    @Override
    public boolean onTouchUp(int x, int y, int ptr, int btn) {
        return currentState.onTouchUp(this, x, y, ptr, btn);
    }

    @Override
    public void onEvent(IPinballEvent event) {
        currentState.onEvent(this, event);
    }

    @Override
    public void onCollision(ICollidable other) {
        super.onCollision(other);
        if (other instanceof FlipperEntity) {
            FlipperEntity f = (FlipperEntity) other;
            float rotVel = f.getRotationalVelocity();
            if (rotVel != 0) {
                // Boost ball upwards if flipper is moving
                getVelocity().y += Math.abs(rotVel) * 1.5f;
                getVelocity().x += rotVel * 0.5f;
            }
        } else if (other instanceof ShooterRodEntity) {
            ShooterRodEntity rod = (ShooterRodEntity) other;
            if (rod.getVelocity().y <= 0) {
                // When rod is resting or being pulled down, switch to IdleState
                setState(new IdleState());
                getVelocity().y = 0;
                getVelocity().x = 0;

                // To be perfectly OOP, you could query the colliders instead of hardcoding 160
                // and 24:
                // float rodHeight = rod.getCollider().getAABB().height;
                // float ballRadius = getCollider().getAABB().width / 2;
                float rodTop = rod.getPosition().y + 160 + 24;

                if (getPosition().y <= rodTop + 2f) { // Small margin to snap securely
                    setPosition(getPosition().x, rodTop);
                    getVelocity().y = 0;
                }
            }
        }
    }
}
