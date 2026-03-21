package com.sit.recyclingpinball.logic.entities;

import com.badlogic.gdx.Input;
import com.sit.recyclingpinball.engine.entities.DynamicEntity;
import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;
import com.sit.recyclingpinball.engine.physics.BoxCollider;
import com.sit.recyclingpinball.logic.events.BallLaunchedEvent;
import com.sit.recyclingpinball.logic.events.PinballEventBus;
import com.sit.recyclingpinball.logic.events.ShooterRodMovedEvent;

public class ShooterRodEntity extends DynamicEntity implements InputListener {
    private final float anchorY;
    private final float maxPullDistance;
    private final PinballEventBus eventBus;
    private final String shaftTextureId;
    private final String knobTextureId;
    private boolean isDragging;
    private boolean isKeyPulling;
    private final float keyPullSpeed = 150f;
    private float launchVelocity;

    public ShooterRodEntity(String id, float x, float y, PinballEventBus eventBus) {
        super(id, x, y, 64, 160);
        this.collider = new BoxCollider(x, y, 64, 160);
        this.anchorY = y;
        this.maxPullDistance = 100f;
        this.eventBus = eventBus;
        this.shaftTextureId = "slide_vertical_grey";
        this.knobTextureId = "ball_blue_large";
        this.isDragging = false;
        this.isKeyPulling = false;
    }

    private float getX() {
        return getPosition().x;
    }

    private float getY() {
        return getPosition().y;
    }

    @Override
    public void render(IGraphicsProvider graphics) {
        // Draw the shaft at the bottom, and the knob extending upwards (where the ball
        // sits)
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
            // Additionally clamp it to not go above anchorY, though prompt doesn't
            // explicitly say for onDrag,
            // but it says "use Math.max(anchorY - maxPullDistance, touchY) to clamp it."
            newY = Math.min(anchorY, newY);

            setPosition(getPosition().x, newY);
            setVelocity(0, 0);

            eventBus.post(new ShooterRodMovedEvent(newY));
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchUp(int x, int y, int ptr, int btn) {
        if (isDragging) {
            isDragging = false;
            float pullDistance = anchorY - getY();
            launchVelocity = pullDistance * 20f;
            setVelocity(0, launchVelocity);
            return true;
        }
        return false;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        
        if (isKeyPulling) {
            float newY = getY() - keyPullSpeed * dt;
            newY = Math.max(anchorY - maxPullDistance, newY);
            setPosition(getPosition().x, newY);
            setVelocity(0, 0);
            eventBus.post(new ShooterRodMovedEvent(newY));
        }

        if (getVelocity().y > 0 && getY() >= anchorY) {
            setPosition(getPosition().x, anchorY);
            setVelocity(0, 0);
            eventBus.post(new BallLaunchedEvent(launchVelocity));
        }
    }

    @Override
    public boolean onKeyDown(int keycode) {
        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
            isKeyPulling = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyUp(int keycode) {
        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
            if (isKeyPulling) {
                isKeyPulling = false;
                float pullDistance = anchorY - getY();
                launchVelocity = pullDistance * 20f;
                setVelocity(0, launchVelocity);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isStatic() {
        return true;
    }
}
