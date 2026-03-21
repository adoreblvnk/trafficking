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
import com.sit.recyclingpinball.logic.events.PinballEventVisitor;
import com.sit.recyclingpinball.logic.events.BallLaunchedEvent;
import com.sit.recyclingpinball.logic.events.ShooterRodMovedEvent;
import com.sit.recyclingpinball.logic.events.BallDrainedEvent;
import com.sit.recyclingpinball.logic.events.TrashCollectedEvent;

public class PinballEntity extends DynamicEntity implements InputListener, PinballEventVisitor {
    private IPinballState currentState;
    private final PinballEventBus eventBus;

    public PinballEntity(String id, float x, float y, PinballEventBus eventBus) {
        super(id, x, y, 48, 48);
        this.collider = new CircleCollider(x, y, 24);
        this.eventBus = eventBus;
        this.currentState = new InPlayState(this); // Start in play state to allow falling onto shooter rod
        this.eventBus.register(this);
        setCollisionEnabled(true);
        setFriction(0.999f);
        setTag("pinball");
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
        graphics.drawTexture("pinball_default", getPosition().getX() - 24, getPosition().getY() - 24, 48, 48);
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
    public void visit(BallLaunchedEvent event) {
        currentState.visit(event);
    }

    @Override
    public void visit(ShooterRodMovedEvent event) {
        currentState.visit(event);
    }

    @Override
    public void visit(BallDrainedEvent event) {
        currentState.visit(event);
    }

    @Override
    public void visit(TrashCollectedEvent event) {
        currentState.visit(event);
    }

    @Override
    public void onCollision(ICollidable other) {
        super.onCollision(other);
        if ("flipper".equals(other.getTag())) {
            FlipperEntity f = (FlipperEntity) other;
            float rotVel = f.getRotationalVelocity();
            if (rotVel != 0) {
                // Boost ball upwards if flipper is moving
                getVelocity().setY(getVelocity().getY() + Math.abs(rotVel) * 1.5f);
                getVelocity().setX(getVelocity().getX() + rotVel * 0.5f);
            }
        } else if ("shooter".equals(other.getTag())) {
            ShooterRodEntity rod = (ShooterRodEntity) other;
            if (rod.getVelocity().getY() <= 0) {
                // When rod is resting or being pulled down, switch to IdleState
                setState(new IdleState(this));
                getVelocity().setY(0);
                getVelocity().setX(0);

                // To be perfectly OOP, you could query the colliders instead of hardcoding 160
                // and 24:
                // float rodHeight = rod.getCollider().getAABB().getHeight();
                // float ballRadius = getCollider().getAABB().getWidth() / 2;
                float rodTop = rod.getPosition().getY() + 160 + 24;

                if (getPosition().getY() <= rodTop + 2f) { // Small margin to snap securely
                    setPosition(getPosition().getX(), rodTop);
                    getVelocity().setY(0);
                }
            }
        }
    }
}
