package com.sit.recyclingpinball.engine.entities;

import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;
import com.sit.recyclingpinball.engine.physics.ICollider;

public class SpriteEntity extends DynamicEntity {

    private String textureId;

    public SpriteEntity(String id, float x, float y, float w, float h, String textureId, ICollider collider) {
        super(id, x, y, w, h);
        this.textureId = textureId;
        this.collider = collider;
    }

    @Override
    public void render(IGraphicsProvider graphics) {
        graphics.drawTexture(textureId, getPosition().getX(), getPosition().getY(), getWidth(), getHeight());
    }
}