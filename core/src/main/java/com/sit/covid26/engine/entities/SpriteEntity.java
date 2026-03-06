package com.sit.covid26.engine.entities;

import com.sit.covid26.engine.interfaces.providers.IGraphicsProvider;
import com.sit.covid26.engine.physics.ICollider;

public class SpriteEntity extends DynamicEntity {

    private String textureId;

    public SpriteEntity(String id, float x, float y, float w, float h, String textureId, ICollider collider) {
        super(id, x, y, w, h);
        this.textureId = textureId;
        this.collider = collider;
    }

    @Override
    public void render(IGraphicsProvider graphics) {
        graphics.drawTexture(textureId, getPosition().x, getPosition().y, getWidth(), getHeight());
    }
}