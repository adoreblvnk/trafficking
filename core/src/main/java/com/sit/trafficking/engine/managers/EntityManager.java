package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sit.trafficking.engine.entities.AbstractEntity;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EntityManager {

    private final Map<String, AbstractEntity> entityMap;
    private final List<AbstractEntity> entityList;

    public EntityManager() {
        this.entityMap = new ConcurrentHashMap<>();
        this.entityList = new CopyOnWriteArrayList<>();
    }

    public void addEntity(AbstractEntity e) {
        if (e == null) return;
        // Prevent duplicate IDs in the list
        if (entityMap.containsKey(e.getId())) {
             // If replacing, remove old from list first
             AbstractEntity old = entityMap.get(e.getId());
             entityList.remove(old);
        }
        entityMap.put(e.getId(), e);
        entityList.add(e);
    }

    public void removeEntity(String id) {
        AbstractEntity e = entityMap.remove(id);
        if (e != null) {
            entityList.remove(e);
        }
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
        for (AbstractEntity e : entityList) {
            e.render(sr);
        }
    }

    public List<AbstractEntity> getEntities() {
        return Collections.unmodifiableList(entityList);
    }
}
