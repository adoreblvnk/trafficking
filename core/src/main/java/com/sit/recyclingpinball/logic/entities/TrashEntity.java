package com.sit.recyclingpinball.logic.entities;

import com.sit.recyclingpinball.engine.entities.StaticEntity;
import com.sit.recyclingpinball.engine.physics.CircleCollider;
import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;
import com.sit.recyclingpinball.engine.interfaces.ICollidable;
import com.sit.recyclingpinball.logic.LogicConstants;
import com.sit.recyclingpinball.logic.factories.TrashType;
import com.sit.recyclingpinball.logic.events.PinballEventBus;
import com.sit.recyclingpinball.logic.events.TrashCollectedEvent;

public class TrashEntity extends StaticEntity {
    private final TrashType type;
    private final String textureId;
    private final int points;
    private final PinballEventBus eventBus;

    public TrashEntity(String id, float x, float y, TrashType type, String textureId, int points,
            PinballEventBus eventBus) {
        super(id, x, y, 64, 64, 1, 1, 1);
        this.type = type;
        this.textureId = textureId;
        this.points = points;
        this.eventBus = eventBus;
        setCollider(new CircleCollider(x, y, 32));
        setCollisionEnabled(true);
        setTag(LogicConstants.TAG_TRASH);
    }

    @Override
    public void render(IGraphicsProvider graphics) {
        graphics.drawTexture(textureId, getPosition().getX() - 32, getPosition().getY() - 32, 64, 64);
    }

    @Override
    public void onCollision(ICollidable other) {
        super.onCollision(other);
        if (LogicConstants.TAG_PINBALL.equals(other.getTag())) {
            if (isCollisionEnabled()) {
                eventBus.post(new TrashCollectedEvent(type, getId()));
                setCollisionEnabled(false);
            }
        }
    }

    public int getPoints() {
        return points;
    }
    public TrashType getType() {
        return type;
    }
}
