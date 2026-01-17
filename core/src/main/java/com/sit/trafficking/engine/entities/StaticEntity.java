package com.sit.trafficking.engine.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.sit.trafficking.utils.Constants;

public class StaticEntity extends Entity {

    // Dimensions in Meters
    private float width;
    private float height;

    public StaticEntity(Body body, float width, float height) {
        super(body);
        this.width = width;
        this.height = height;
        this.color = Color.GREEN;
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        // Box2D positions are centered. ShapeRenderer draws from bottom-left.
        // Convert center (meters) -> bottom-left (pixels)
        float x = (position.x - width / 2) * Constants.PPM;
        float y = (position.y - height / 2) * Constants.PPM;
        float w = width * Constants.PPM;
        float h = height * Constants.PPM;

        shapeRenderer.rect(x, y, w, h);
    }
}
