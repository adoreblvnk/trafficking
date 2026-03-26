package com.sit.recyclingpinball.logic.entities;

import com.sit.recyclingpinball.engine.components.SpriteComponent;
/* ARCHITECTURE JUSTIFICATION: Primitive Data Transfer Object (DTO).
 * The Logic layer imports PlatformKey from the platform package. This is a
 * safe traversal because PlatformKey is a pure Java enum DTO that fully
 * insulates Logic from com.badlogic.gdx.Input.Keys while preserving framework
 * independence.
 */
import com.sit.recyclingpinball.engine.platform.libgdx.PlatformKey;

import com.sit.recyclingpinball.engine.entities.DynamicEntity;
import com.sit.recyclingpinball.engine.physics.OBBCollider;
import com.sit.recyclingpinball.engine.interfaces.IGraphics;
import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.logic.LogicConstants;

public class FlipperEntity extends DynamicEntity implements InputListener {

    private final boolean isLeft;
    private final float maxAngle;
    private final float startAngle;
    private float currentAngle;
    private float rotationalVelocity;
    private final SpriteComponent sprite;

    public float getRotationalVelocity() {
        return rotationalVelocity;
    }

    public FlipperEntity(String id, float x, float y, boolean isLeft) {
        super(id, x, y, LogicConstants.FLIPPER_SIZE[0], LogicConstants.FLIPPER_SIZE[1]);
        this.isLeft = isLeft;
        this.sprite = new SpriteComponent(LogicConstants.TEX_FLIPPER, LogicConstants.FLIPPER_SIZE[0],
                LogicConstants.FLIPPER_SIZE[1]);

        if (isLeft) {
            this.startAngle = LogicConstants.FLIPPER_LEFT_START_ANGLE;
            this.maxAngle = LogicConstants.FLIPPER_LEFT_MAX_ANGLE;
        } else {
            // Right flipper must point leftwards (180 degrees).
            // A rest angle tilting down-left translates to 180 + 10 = 190 degrees.
            // A swept angle pointing up-left translates to 180 - 45 = 135 degrees.
            this.startAngle = LogicConstants.FLIPPER_RIGHT_START_ANGLE;
            this.maxAngle = LogicConstants.FLIPPER_RIGHT_MAX_ANGLE;
        }
        this.currentAngle = this.startAngle;
        this.rotationalVelocity = 0f;

        // Both pivot on their local (20, 20). When the right flipper starts at 190
        // degrees,
        // it naturally mirrors the left one, setting its hinge perfectly to the right
        // side of its bounds!
        setCollider(new OBBCollider(x, y, LogicConstants.FLIPPER_SIZE[0], LogicConstants.FLIPPER_SIZE[1],
                LogicConstants.FLIPPER_PIVOT[0], LogicConstants.FLIPPER_PIVOT[1], currentAngle));
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
            entity.getVelocity().setY(entity.getVelocity().getY() + Math.abs(rotVel) * LogicConstants.FLIPPER_BOOST[1]);
            entity.getVelocity().setX(entity.getVelocity().getX() + rotVel * LogicConstants.FLIPPER_BOOST[0]);
        }
    }

    @Override
    public void render(IGraphics graphics) {
        graphics.drawTexture(sprite.textureId(), getPosition().getX(), getPosition().getY(), sprite.width(),
                sprite.height(), LogicConstants.FLIPPER_PIVOT[0], LogicConstants.FLIPPER_PIVOT[1], currentAngle);
    }

    @Override
    public boolean onKeyDown(PlatformKey keycode) {
        // Check LEFT flipper keys
        if (isLeft && (keycode == PlatformKey.A || keycode == PlatformKey.LEFT)) {
            rotationalVelocity = LogicConstants.FLIPPER_ROT_VELOCITY; // Sweeps CCW (up)
            return true;
        }
        // Check RIGHT flipper keys
        if (!isLeft && (keycode == PlatformKey.D || keycode == PlatformKey.RIGHT)) {
            rotationalVelocity = -LogicConstants.FLIPPER_ROT_VELOCITY; // Sweeps CW (up for right flipper)
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyUp(PlatformKey keycode) {
        if (isLeft && (keycode == PlatformKey.A || keycode == PlatformKey.LEFT)) {
            rotationalVelocity = -LogicConstants.FLIPPER_ROT_VELOCITY; // Sweeps CW (down)
            return true;
        } else if (!isLeft && (keycode == PlatformKey.D || keycode == PlatformKey.RIGHT)) {
            rotationalVelocity = LogicConstants.FLIPPER_ROT_VELOCITY; // Sweeps CCW (down for right flipper)
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
