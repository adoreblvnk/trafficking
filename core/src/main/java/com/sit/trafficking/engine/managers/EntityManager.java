package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sit.trafficking.engine.entities.AbstractEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Registry for game entities providing lifecycle management and ordered rendering.
 * Maintains thread-safe concurrent access for entity modifications during iteration.
 */
public class EntityManager {

    private final Map<String, AbstractEntity> entityMap;
    private final List<AbstractEntity> entityList;

    public EntityManager() {
        this.entityMap = new ConcurrentHashMap<>();
        this.entityList = new CopyOnWriteArrayList<>();
    }

    public boolean addEntity(AbstractEntity e) {
        if (e == null) {
            Gdx.app.log("EntityManager", "Cannot add null entity (ignored)");
            return false;
        }
        if (e.getId() == null || e.getId().isEmpty()) {
            Gdx.app.log("EntityManager", "Cannot add entity with null/empty ID (ignored)");
            return false;
        }

        // Prevent duplicate IDs in the list
        if (entityMap.containsKey(e.getId())) {
             // If replacing, remove old from list first
             AbstractEntity old = entityMap.get(e.getId());
             entityList.remove(old);
             Gdx.app.log("EntityManager", "Replaced entity with duplicate ID: " + e.getId());
        }
        entityMap.put(e.getId(), e);
        entityList.add(e);
        return true;
    }

    public boolean removeEntity(String id) {
        if (id == null || id.isEmpty()) {
            Gdx.app.log("EntityManager", "Cannot remove entity with null/empty ID (ignored)");
            return false;
        }

        AbstractEntity e = entityMap.remove(id);
        if (e != null) {
            entityList.remove(e);
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

    public void render(ShapeRenderer sr) {
        List<AbstractEntity> renderList = new ArrayList<>(entityList);
        // Sorts by z-index before rendering to control draw order
        renderList.sort(Comparator.comparingInt(AbstractEntity::getZIndex));

        for (AbstractEntity e : renderList) {
            e.render(sr);
        }
    }

    public List<AbstractEntity> getEntities() {
        return Collections.unmodifiableList(entityList);
    }
}
