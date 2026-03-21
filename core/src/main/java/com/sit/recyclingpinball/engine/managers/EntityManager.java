package com.sit.recyclingpinball.engine.managers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.sit.recyclingpinball.engine.entities.AbstractEntity;
import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;

/**
 * Registry for game entities providing lifecycle management and ordered
 * rendering. Maintains thread-safe concurrent access for entity modifications
 * during iteration. No longer directly depends on libGDX (uses
 * IGraphicsProvider instead of ShapeRenderer).
 */
public class EntityManager {

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

    public AbstractEntity getEntity(String id) {
        return entityMap.get(id);
    }

    public void update(float dt) {
        for (AbstractEntity e : entityList) {
            e.update(dt);
        }
    }

    /**
     * Marks the entity manager's z-index order as dirty, forcing a re-sort before
     * the next render.
     */
    public void markZIndexDirty() {
        this.isZIndexDirty = true;
    }

    /**
     * Renders all entities in z-index order using the provided graphics provider.
     *
     * @param graphics
     *            the graphics provider for platform-independent rendering
     */
    public void render(IGraphicsProvider graphics) {
        if (isZIndexDirty) {
            sortIfDirty();
        }

        for (AbstractEntity e : renderList) {
            e.render(graphics);
        }
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
}
