package com.sit.recyclingpinball.logic.entities;

import com.sit.recyclingpinball.engine.entities.StaticEntity;
import com.sit.recyclingpinball.engine.physics.CircleCollider;
import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;
import com.sit.recyclingpinball.logic.factories.TrashType;

public class TrashEntity extends StaticEntity {
    private final TrashType type;
    private final String textureId;
    private final int points;

    public TrashEntity(String id, float x, float y, TrashType type, String textureId, int points) {
        super(id, x, y, 64, 64, 1, 1, 1);
        this.type = type;
        this.textureId = textureId;
        this.points = points;
        this.collider = new CircleCollider(x, y, 32);
        setCollisionEnabled(true);
        setTag("trash");
    }

    @Override
    public void render(IGraphicsProvider graphics) {
        graphics.drawTexture(textureId, getPosition().getX() - 32, getPosition().getY() - 32, 64, 64);
    }

    public int getPoints() { return points; }
    public TrashType getType() { return type; }
}
