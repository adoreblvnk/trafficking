package com.sit.trafficking.engine.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.TimeUtils;
import com.sit.trafficking.engine.managers.SoundManager;
import com.sit.trafficking.utils.Constants;

public class DynamicEntity extends Entity implements ICollidable {

    private float width;
    private float height;
    private long lastSoundTime = 0;
    private float hitFlashTimer = 0f;
    private Color flashColor = Color.WHITE;

    public DynamicEntity(Body body, float width, float height) {
        super(body);
        this.width = width;
        this.height = height;
        this.color = Color.RED;
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        if (hitFlashTimer > 0) {
            hitFlashTimer -= Gdx.graphics.getDeltaTime();
            shapeRenderer.setColor(flashColor);
        } else {
            shapeRenderer.setColor(color);
        }

        float w = width * Constants.PPM;
        float h = height * Constants.PPM;
        
        // Calculate bottom-left relative to center for drawing
        float x = (position.x * Constants.PPM) - (w / 2);
        float y = (position.y * Constants.PPM) - (h / 2);

        // Origin for rotation should be center of the rect
        float originX = w / 2;
        float originY = h / 2;

        float rotation = body.getAngle() * MathUtils.radiansToDegrees;

        shapeRenderer.rect(x, y, originX, originY, w, h, 1, 1, rotation);
    }

    @Override
    public void onCollision(Entity other, float intensity) {
        if (intensity < 1.0f) return; // Ignore resting interactions
        
        hitFlashTimer = 0.1f;
        flashColor = Color.WHITE; // Default collision color

        if (TimeUtils.timeSinceMillis(lastSoundTime) > 150) {
            lastSoundTime = TimeUtils.millis();

            SoundManager.getInstance().playSound();
            
            flashColor = Color.YELLOW; // Major collision color
        }
    }
}
