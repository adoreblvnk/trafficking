package com.sit.recyclingpinball.logic.entities;

import com.sit.recyclingpinball.engine.components.SpriteComponent;
import com.sit.recyclingpinball.engine.entities.DynamicEntity;
import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.engine.interfaces.ICollidable;
import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;
import com.sit.recyclingpinball.engine.physics.CircleCollider;
import com.sit.recyclingpinball.logic.states.IPinballState;
import com.sit.recyclingpinball.logic.events.PinballEventBus;
import com.sit.recyclingpinball.logic.events.PinballEventVisitor;
import com.sit.recyclingpinball.logic.events.BallLaunchedEvent;
import com.sit.recyclingpinball.logic.events.ShooterRodMovedEvent;
import com.sit.recyclingpinball.logic.LogicConstants;
import com.sit.recyclingpinball.logic.events.BallDrainedEvent;
import com.sit.recyclingpinball.logic.events.TrashCollectedEvent;
import com.sit.recyclingpinball.logic.factories.StateFactory;

public class PinballEntity extends DynamicEntity implements InputListener, PinballEventVisitor {
    private IPinballState currentState;
    private final PinballEventBus eventBus;
    private final StateFactory stateFactory;
    private final SpriteComponent sprite;

    public PinballEntity(String id, float x, float y, PinballEventBus eventBus, StateFactory stateFactory) {
        super(id, x, y, LogicConstants.PINBALL_SIZE, LogicConstants.PINBALL_SIZE);
        setCollider(new CircleCollider(x, y, LogicConstants.PINBALL_SIZE / 2f));
        this.eventBus = eventBus;
        this.stateFactory = stateFactory;
        this.sprite = new SpriteComponent(LogicConstants.TEX_PINBALL_DEFAULT, LogicConstants.PINBALL_SIZE,
                LogicConstants.PINBALL_SIZE);
        this.currentState = stateFactory.createInPlayState();
        this.eventBus.register(this);
        setCollisionEnabled(true);
        setFriction(LogicConstants.PINBALL_FRICTION);
        setTag(LogicConstants.TAG_PINBALL);
    }

    public void setState(IPinballState state) {
        this.currentState = state;
    }

    public PinballEventBus getEventBus() {
        return eventBus;
    }

    public StateFactory getStateFactory() {
        return stateFactory;
    }

    @Override
    public void update(float dt) {
        currentState.update(dt, this);
        super.update(dt);
    }

    @Override
    public void render(IGraphicsProvider graphics) {
        graphics.drawTexture(sprite.textureId(), getPosition().getX() - (sprite.width() / 2f),
                getPosition().getY() - (sprite.height() / 2f), sprite.width(), sprite.height());
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
    public void visit(com.sit.recyclingpinball.logic.events.BallRestedOnRodEvent event) {
        setState(stateFactory.createIdleState(this));
    }

    @Override
    public void onCollision(ICollidable other) {
        super.onCollision(other);
        other.resolveCollision(this);
    }
}
