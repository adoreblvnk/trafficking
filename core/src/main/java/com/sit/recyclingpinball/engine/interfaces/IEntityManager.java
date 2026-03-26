package com.sit.recyclingpinball.engine.interfaces;

import java.util.List;

import com.sit.recyclingpinball.engine.entities.AbstractEntity;
/* ARCHITECTURE JUSTIFICATION: Concrete Delegation Wrapper.
 * We intentionally import PlatformGraphics instead of an interface to prevent
 * circular package dependencies (Engine <-> Platform). PlatformGraphics is a
 * pure Thin Facade with zero game logic, isolating LibGDX imports from Engine
 * Core without adding polymorphic dispatch overhead on high-frequency render
 * loops.
 */
import com.sit.recyclingpinball.engine.platform.libgdx.PlatformGraphics;

public interface IEntityManager {
    boolean addEntity(AbstractEntity e);
    boolean removeEntity(String id);
    void update(float dt);
    void render(PlatformGraphics graphics);
    List<AbstractEntity> getEntities();
    <T> List<T> getEntitiesByType(Class<T> type);
}
