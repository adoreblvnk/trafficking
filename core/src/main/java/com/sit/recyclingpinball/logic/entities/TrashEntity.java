package com.sit.recyclingpinball.logic.entities;

import com.sit.recyclingpinball.engine.components.SpriteComponent;
import com.sit.recyclingpinball.engine.entities.StaticEntity;
import com.sit.recyclingpinball.engine.physics.CircleCollider;
import com.sit.recyclingpinball.engine.interfaces.IGraphics;
import com.sit.recyclingpinball.engine.interfaces.ICollidable;
import com.sit.recyclingpinball.logic.LogicConstants;
import com.sit.recyclingpinball.logic.events.PinballEventBus;
import com.sit.recyclingpinball.logic.events.TrashCollectedEvent;

public class TrashEntity extends StaticEntity {
    private final SpriteComponent sprite;
    private final PinballEventBus eventBus;

    public TrashEntity(String id, float x, float y, String textureId, PinballEventBus eventBus) {
        super(id, x, y, LogicConstants.TRASH_SIZE, LogicConstants.TRASH_SIZE, 1, 1, 1);
        this.sprite = new SpriteComponent(textureId, LogicConstants.TRASH_SIZE, LogicConstants.TRASH_SIZE);
        this.eventBus = eventBus;
        setCollider(new CircleCollider(x, y, LogicConstants.TRASH_SIZE / 2f));
        setCollisionEnabled(true);
        setTag(LogicConstants.TAG_TRASH);
    }

    @Override
    public void render(IGraphics graphics) {
        graphics.drawTexture(sprite.textureId(), getPosition().getX() - (sprite.width() / 2f),
                getPosition().getY() - (sprite.height() / 2f), sprite.width(), sprite.height());
    }

    @Override
    public void onCollision(ICollidable other) {
        super.onCollision(other);
        if (LogicConstants.TAG_PINBALL.equals(other.getTag())) {
            if (isCollisionEnabled()) {
                eventBus.post(new TrashCollectedEvent(getId()));
                setCollisionEnabled(false);
            }
        }
    }
}
