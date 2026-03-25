package com.sit.recyclingpinball.logic.entities;

import com.sit.recyclingpinball.engine.components.SpriteComponent;
import com.sit.recyclingpinball.engine.interfaces.providers.EngineKey;
import com.sit.recyclingpinball.engine.entities.AbstractEntity;
import com.sit.recyclingpinball.engine.entities.DynamicEntity;
import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;
import com.sit.recyclingpinball.engine.physics.BoxCollider;
import com.sit.recyclingpinball.logic.events.BallLaunchedEvent;
import com.sit.recyclingpinball.logic.events.BallRestedOnRodEvent;
import com.sit.recyclingpinball.logic.events.PinballEventBus;
import com.sit.recyclingpinball.logic.events.ShooterRodMovedEvent;
import com.sit.recyclingpinball.logic.LogicConstants;

/**
 * An anchored shooter rod that responds to drag/keyboard input and launches the
 * ball. Extends AbstractEntity directly because it is not a physics-driven body
 * — it manages its own position via input and spring-back animation.
 */
public class ShooterRodEntity extends AbstractEntity implements InputListener {
    private final float anchorY;
    private final PinballEventBus eventBus;
    private final SpriteComponent shaftSprite;
    private final SpriteComponent knobSprite;
    private boolean isDragging;
    private boolean isKeyPulling;
    private float launchVelocity;
    private float velocityY;

    public ShooterRodEntity(String id, float x, float y, PinballEventBus eventBus) {
        super(id, x, y, LogicConstants.SHOOTER_SIZE[0], LogicConstants.SHOOTER_SIZE[1]);
        setCollider(new BoxCollider(x, y, LogicConstants.SHOOTER_SIZE[0], LogicConstants.SHOOTER_SIZE[1]));
        this.anchorY = y;
        this.eventBus = eventBus;
        this.shaftSprite = new SpriteComponent(LogicConstants.TEX_SLIDE_VERTICAL_GREY,
                LogicConstants.SHOOTER_SHAFT_SIZE[0], LogicConstants.SHOOTER_SHAFT_SIZE[1]);
        this.knobSprite = new SpriteComponent(LogicConstants.TEX_BALL_BLUE_LARGE, LogicConstants.SHOOTER_KNOB_SIZE,
                LogicConstants.SHOOTER_KNOB_SIZE);
        this.isDragging = false;
        this.isKeyPulling = false;
        this.velocityY = 0;
        setTag(LogicConstants.TAG_SHOOTER);
    }

    @Override
    public void resolveCollision(DynamicEntity entity) {
        if (velocityY <= 0) {
            entity.getVelocity().setY(0);
            entity.getVelocity().setX(0);

            float rodTop = getPosition().getY() + LogicConstants.SHOOTER_SIZE[1] + (LogicConstants.PINBALL_SIZE / 2f);

            if (entity.getPosition().getY() <= rodTop + 2f) {
                entity.setPosition(entity.getPosition().getX(), rodTop);
                entity.getVelocity().setY(0);
            }
            eventBus.post(new BallRestedOnRodEvent());
        }
    }

    @Override
    public void render(IGraphicsProvider graphics) {
        graphics.drawTexture(shaftSprite.textureId(), getPosition().getX() + LogicConstants.SHOOTER_SHAFT_OFFSET[0],
                getPosition().getY() + LogicConstants.SHOOTER_SHAFT_OFFSET[1], shaftSprite.width(),
                shaftSprite.height());
        graphics.drawTexture(knobSprite.textureId(), getPosition().getX() + LogicConstants.SHOOTER_KNOB_OFFSET[0],
                getPosition().getY() + LogicConstants.SHOOTER_KNOB_OFFSET[1], knobSprite.width(), knobSprite.height());
    }

    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) {
        if (getCollider() != null && getCollider().contains(x, y)) {
            isDragging = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean onDrag(int x, int y, int ptr) {
        if (isDragging) {
            float newY = Math.max(anchorY - LogicConstants.SHOOTER_MAX_PULL, (float) y);
            newY = Math.min(anchorY, newY);

            setPosition(getPosition().getX(), newY);
            velocityY = 0;

            eventBus.post(new ShooterRodMovedEvent(newY));
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchUp(int x, int y, int ptr, int btn) {
        if (isDragging) {
            isDragging = false;
            float pullDistance = anchorY - getPosition().getY();
            launchVelocity = pullDistance * LogicConstants.SHOOTER_LAUNCH_MULTIPLIER;
            velocityY = launchVelocity;
            return true;
        }
        return false;
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (isKeyPulling) {
            float newY = getPosition().getY() - LogicConstants.SHOOTER_KEY_PULL_SPEED * dt;
            newY = Math.max(anchorY - LogicConstants.SHOOTER_MAX_PULL, newY);
            setPosition(getPosition().getX(), newY);
            velocityY = 0;
            eventBus.post(new ShooterRodMovedEvent(newY));
        }

        // Spring-back: move rod upward toward anchor
        if (velocityY > 0) {
            float newY = getPosition().getY() + velocityY * dt;
            if (newY >= anchorY) {
                setPosition(getPosition().getX(), anchorY);
                velocityY = 0;
                eventBus.post(new BallLaunchedEvent(launchVelocity));
            } else {
                setPosition(getPosition().getX(), newY);
            }
        }
    }

    @Override
    public boolean onKeyDown(EngineKey keycode) {
        if (keycode == EngineKey.DOWN || keycode == EngineKey.S) {
            isKeyPulling = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyUp(EngineKey keycode) {
        if (keycode == EngineKey.DOWN || keycode == EngineKey.S) {
            if (isKeyPulling) {
                isKeyPulling = false;
                float pullDistance = anchorY - getPosition().getY();
                launchVelocity = pullDistance * LogicConstants.SHOOTER_LAUNCH_MULTIPLIER;
                velocityY = launchVelocity;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public boolean isTrigger() {
        return false;
    }

    @Override
    public float getInverseMass() {
        return 0f;
    }
}
