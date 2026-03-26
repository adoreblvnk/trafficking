package com.sit.recyclingpinball.engine.interfaces;

import java.util.List;

import com.sit.recyclingpinball.engine.entities.AbstractEntity;

public interface IEntityManager {
    boolean addEntity(AbstractEntity e);
    boolean removeEntity(String id);
    void update(float dt);
    void render(IGraphics graphics);
    List<AbstractEntity> getEntities();
    <T> List<T> getEntitiesByType(Class<T> type);
}
