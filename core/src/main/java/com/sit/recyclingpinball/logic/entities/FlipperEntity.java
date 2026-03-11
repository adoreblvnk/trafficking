package com.sit.recyclingpinball.logic.entities;

import com.badlogic.gdx.Input;
import com.sit.recyclingpinball.engine.entities.DynamicEntity;
import com.sit.recyclingpinball.engine.physics.OBBCollider;
import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;
import com.sit.recyclingpinball.engine.interfaces.InputListener;

public class FlipperEntity extends DynamicEntity implements InputListener {
    private final boolean isLeft;
    private final float maxAngle;
    private final float startAngle;
    private float currentAngle;
    private float rotationalVelocity;
    private final String textureId;
    public float getRotationalVelocity() { return rotationalVelocity; }
    
    public FlipperEntity(String id, float x, float y, boolean isLeft) {
        super(id, x, y, 180, 40);
        this.isLeft = isLeft;
        this.textureId = "flipper";
        
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
        
        // Both pivot on their local (20, 20). When the right flipper starts at 190 degrees,
        // it naturally mirrors the left one, setting its hinge perfectly to the right side of its bounds!
        this.collider = new OBBCollider(x, y, 180, 40, 20, 20, currentAngle);
        setCollisionEnabled(true);
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
            // Because 135 (max) is less than 190 (start), sweeping up is a NEGATIVE rotation
            if (currentAngle < maxAngle) {
                currentAngle = maxAngle;
                rotationalVelocity = 0;
            } else if (currentAngle > startAngle) {
                currentAngle = startAngle;
                rotationalVelocity = 0;
            }
        }
        
        if (this.collider instanceof OBBCollider) {
            ((OBBCollider) this.collider).setRotation(currentAngle);
        }
        super.update(dt);
    }

    @Override
    public void render(IGraphicsProvider graphics) {
        graphics.drawTexture(textureId, getPosition().x, getPosition().y, 180, 40, 20, 20, currentAngle);
    }

    @Override
    public boolean onKeyDown(int keycode) {
        if (isLeft && keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
            rotationalVelocity = 600f; // Sweeps CCW (up)
            return true;
        } else if (!isLeft && keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
            rotationalVelocity = -600f; // Sweeps CW (up for right flipper)
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyUp(int keycode) {
        if (isLeft && keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
            rotationalVelocity = -600f; // Sweeps CW (down)
            return true;
        } else if (!isLeft && keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
            rotationalVelocity = 600f; // Sweeps CCW (down for right flipper)
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) { return false; }
    @Override
    public boolean onDrag(int x, int y, int ptr) { return false; }
    @Override
    public boolean onTouchUp(int x, int y, int ptr, int btn) { return false; }

    @Override
    public boolean isStatic() {
        return true; 
    }
}
