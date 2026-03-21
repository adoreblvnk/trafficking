package com.sit.recyclingpinball.logic.entities;

import com.sit.recyclingpinball.engine.interfaces.providers.EngineKey;

import com.sit.recyclingpinball.engine.entities.DynamicEntity;
import com.sit.recyclingpinball.engine.physics.OBBCollider;
import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;
import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.logic.LogicConstants;

public class FlipperEntity extends DynamicEntity implements InputListener {
    private final boolean isLeft;
    private final float maxAngle;
    private final float startAngle;
    private float currentAngle;
    private float rotationalVelocity;
    private final String textureId;

    public float getRotationalVelocity() {
        return rotationalVelocity;
    }

    public FlipperEntity(String id, float x, float y, boolean isLeft) {
        super(id, x, y, 180, 40);
        this.isLeft = isLeft;
        this.textureId = LogicConstants.TEX_FLIPPER;

        if (isLeft) {
            this.startAngle = -10f;
            this.maxAngle = 45f;
        } else {
            // Right flipper must point leftwards (180 degrees).
            // A rest angle tilting down-left translates to 180 + 10 = 190 degrees.
            // A swept angle pointing up-left translates to 180 - 45 = 135 degrees.
            this.startAngle = 190f;
            this.maxAngle = 135f;
        }
        this.currentAngle = this.startAngle;
        this.rotationalVelocity = 0f;

        // Both pivot on their local (20, 20). When the right flipper starts at 190
        // degrees,
        // it naturally mirrors the left one, setting its hinge perfectly to the right
        // side of its bounds!
        setCollider(new OBBCollider(x, y, 180, 40, 20, 20, currentAngle));
        setCollisionEnabled(true);
        setTag(LogicConstants.TAG_FLIPPER);
    }

    @Override
    public void update(float dt) {
        currentAngle += rotationalVelocity * dt;

        if (isLeft) {
            if (currentAngle > maxAngle) {
                currentAngle = maxAngle;
                rotationalVelocity = 0;
            } else if (currentAngle < startAngle) {
                currentAngle = startAngle;
                rotationalVelocity = 0;
            }
        } else {
            // Because 135 (max) is less than 190 (start), sweeping up is a NEGATIVE
            // rotation
            if (currentAngle < maxAngle) {
                currentAngle = maxAngle;
                rotationalVelocity = 0;
            } else if (currentAngle > startAngle) {
                currentAngle = startAngle;
                rotationalVelocity = 0;
            }
        }

        if (getCollider() != null) {
            getCollider().setRotation(currentAngle);
        }
        super.update(dt);
    }

    @Override
    public void resolveCollision(com.sit.recyclingpinball.engine.entities.DynamicEntity entity) {
        float rotVel = getRotationalVelocity();
        if (rotVel != 0) {
            // Boost ball upwards if flipper is moving
            entity.getVelocity().setY(entity.getVelocity().getY() + Math.abs(rotVel) * 1.5f);
            entity.getVelocity().setX(entity.getVelocity().getX() + rotVel * 0.5f);
        }
    }

    @Override
    public void render(IGraphicsProvider graphics) {
        graphics.drawTexture(textureId, getPosition().getX(), getPosition().getY(), 180, 40, 20, 20, currentAngle);
    }

    @Override
    public boolean onKeyDown(EngineKey keycode) {
        // Check LEFT flipper keys
        if (isLeft && (keycode == EngineKey.A || keycode == EngineKey.LEFT)) {
            rotationalVelocity = 512f; // Sweeps CCW (up)
            return true;
        }
        // Check RIGHT flipper keys
        if (!isLeft && (keycode == EngineKey.D || keycode == EngineKey.RIGHT)) {
            rotationalVelocity = -512f; // Sweeps CW (up for right flipper)
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyUp(EngineKey keycode) {
        if (isLeft && (keycode == EngineKey.A || keycode == EngineKey.LEFT)) {
            rotationalVelocity = -512f; // Sweeps CW (down)
            return true;
        } else if (!isLeft && (keycode == EngineKey.D || keycode == EngineKey.RIGHT)) {
            rotationalVelocity = 512f; // Sweeps CCW (down for right flipper)
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) {
        return false;
    }

    @Override
    public boolean onDrag(int x, int y, int ptr) {
        return false;
    }

    @Override
    public boolean onTouchUp(int x, int y, int ptr, int btn) {
        return false;
    }

    @Override
    public boolean isStatic() {
        return true;
    }
}
