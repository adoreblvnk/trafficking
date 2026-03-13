package com.sit.recyclingpinball.logic.entities;

import com.sit.recyclingpinball.engine.entities.DynamicEntity;
import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;
import com.sit.recyclingpinball.engine.physics.BoxCollider;
import com.sit.recyclingpinball.logic.events.BallLaunchedEvent;
import com.sit.recyclingpinball.logic.events.PinballEventBus;

public class ShooterRodEntity extends DynamicEntity implements InputListener {
    private final float anchorY;
    private final float maxPullDistance;
    private final PinballEventBus eventBus;
    private final String shaftTextureId;
    private final String knobTextureId;
    private boolean isDragging;

    public ShooterRodEntity(String id, float x, float y, PinballEventBus eventBus) {
        super(id, x, y, 64, 160);
        this.collider = new BoxCollider(x, y, 64, 160);
        this.anchorY = y;
        this.maxPullDistance = 100f;
        this.eventBus = eventBus;
        this.shaftTextureId = "slide_vertical_grey";
        this.knobTextureId = "ball_blue_large";
        this.isDragging = false;
    }

    private float getX() {
        return getPosition().x;
    }

    private float getY() {
        return getPosition().y;
    }

    @Override
    public void render(IGraphicsProvider graphics) {
        // Draw the shaft at the bottom, and the knob extending upwards (where the ball sits)
        graphics.drawTexture(shaftTextureId, getX() + 24, getY(), 16, 96);
        graphics.drawTexture(knobTextureId, getX(), getY() + 96, 64, 64);
    }

    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) {
        float touchY = 1000f - y;
        if (getCollider() != null && getCollider().contains(x, touchY)) {
            isDragging = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean onDrag(int x, int y, int ptr) {
        if (isDragging) {
            float touchY = 1000f - y;
            float newY = Math.max(anchorY - maxPullDistance, touchY);
            // Additionally clamp it to not go above anchorY, though prompt doesn't explicitly say for onDrag, 
            // but it says "use Math.max(anchorY - maxPullDistance, touchY) to clamp it."
            newY = Math.min(anchorY, newY);
            
            getPosition().y = newY;
            if (getCollider() != null) {
                ((BoxCollider)getCollider()).setPosition(getPosition().x, getPosition().y);
            }
            setVelocity(0, 0);
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchUp(int x, int y, int ptr, int btn) {
        if (isDragging) {
            isDragging = false;
            float pullDistance = anchorY - getY();
            setVelocity(0, pullDistance * 15f);
            return true;
        }
        return false;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (getVelocity().y > 0 && getY() >= anchorY) {
            getPosition().y = anchorY;
            setVelocity(0, 0);
            if (getCollider() != null) {
                ((BoxCollider)getCollider()).setPosition(getPosition().x, getPosition().y);
            }
            eventBus.post(new BallLaunchedEvent());
        }
    }

    @Override
    public boolean isStatic() {
        return true;
    }
}
