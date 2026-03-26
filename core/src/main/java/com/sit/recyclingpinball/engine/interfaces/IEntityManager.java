package com.sit.recyclingpinball.engine.interfaces;

import java.util.List;

import com.sit.recyclingpinball.engine.entities.AbstractEntity;
// Uses PlatformGraphics directly to avoid high-frequency polymorphic dispatch overhead.
import com.sit.recyclingpinball.engine.platform.libgdx.PlatformGraphics;

public interface IEntityManager {
    boolean addEntity(AbstractEntity e);
    boolean removeEntity(String id);
    void update(float dt);
    void render(PlatformGraphics graphics);
    List<AbstractEntity> getEntities();
    <T> List<T> getEntitiesByType(Class<T> type);
}
