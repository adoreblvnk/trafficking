package com.sit.recyclingpinball.engine.managers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.sit.recyclingpinball.engine.entities.AbstractEntity;
import com.sit.recyclingpinball.engine.platform.libgdx.PlatformGraphics;

/**
 * Registry for game entities providing lifecycle management and ordered
 * rendering.
 *
 * This manager maintains a registry of all active game entities, providing
 * efficient lookup by ID and supporting Z-index based rendering. It serves as
 * the central repository for entity lifecycle within the game engine.
 *
 * Architecture Justification: The use of thread-safe concurrent collections
 * (ConcurrentHashMap, CopyOnWriteArrayList) is a deliberate design decision.
 * While the current simulation is single-threaded, these structures provide
 * robust defensive programming for a generic, reusable engine. This
 * architecture facilitates future scalability, allowing for parallelized update
 * loops or asynchronous entity modifications without risking
 * ConcurrentModificationException.
 *
 * @see com.sit.recyclingpinball.engine.entities.AbstractEntity
 * @see com.sit.recyclingpinball.engine.platform.libgdx.PlatformGraphics
 */
public class EntityManager implements com.sit.recyclingpinball.engine.interfaces.IEntityManager {

    private final Map<String, AbstractEntity> entityMap;
    private final List<AbstractEntity> entityList;
    private final List<AbstractEntity> renderList;
    private boolean isZIndexDirty = false;

    public EntityManager() {
        this.entityMap = new ConcurrentHashMap<>();
        this.entityList = new CopyOnWriteArrayList<>();
        this.renderList = new CopyOnWriteArrayList<>();
    }

    public boolean addEntity(AbstractEntity e) {
        if (e == null) {
            return false;
        }
        if (e.getId() == null || e.getId().isEmpty()) {
            return false;
        }

        // Prevent duplicate IDs in the list
        if (entityMap.containsKey(e.getId())) {
            // If replacing, remove old from list first
            AbstractEntity old = entityMap.get(e.getId());
            entityList.remove(old);
            renderList.remove(old);
        }
        entityMap.put(e.getId(), e);
        entityList.add(e);
        renderList.add(e);
        isZIndexDirty = true;
        return true;
    }

    public boolean removeEntity(String id) {
        if (id == null || id.isEmpty()) {
            return false;
        }

        AbstractEntity e = entityMap.remove(id);
        if (e != null) {
            entityList.remove(e);
            renderList.remove(e);
            isZIndexDirty = true;
            return true;
        }
        return false;
    }

    public void update(float dt) {
        entityList.forEach(e -> e.update(dt));
    }

    /**
     * Renders all entities in z-index order using the provided graphics provider.
     *
     * @param graphics
     *            the graphics provider for platform-independent rendering
     */
    public void render(PlatformGraphics graphics) {
        if (isZIndexDirty) {
            sortIfDirty();
        }
        renderList.forEach(e -> e.render(graphics));
    }

    private synchronized void sortIfDirty() {
        if (isZIndexDirty) {
            renderList.sort(Comparator.comparingInt(AbstractEntity::getZIndex));
            isZIndexDirty = false;
        }
    }

    public List<AbstractEntity> getEntities() {
        return Collections.unmodifiableList(entityList);
    }

    /**
     * Retrieves all entities that implement or extend a specific type. Uses
     * Generics to avoid downcasting in the caller.
     *
     * @param type
     *            the Class object of the type to filter by
     * @param <T>
     *            the generic type parameter
     * @return a list of entities cast to the specified type
     */
    public <T> List<T> getEntitiesByType(Class<T> type) {
        return entityList.stream().filter(type::isInstance).map(type::cast).toList();
    }
}
