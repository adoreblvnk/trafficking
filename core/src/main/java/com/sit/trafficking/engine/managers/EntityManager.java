package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sit.trafficking.engine.entities.AbstractEntity;
import com.sit.trafficking.exceptions.DuplicateEntityIdException;
import com.sit.trafficking.exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the lifecycle and storage of all entities in a scene.
 * NOT a Singleton - instantiated per Scene.
 * Provides CRUD operations.
 */
public class EntityManager {

    // Map for O(1) lookup by ID
    private final Map<String, AbstractEntity> entityMap;
    // List for ordered iteration/rendering
    private final List<AbstractEntity> entityList;

    public EntityManager() {
        this.entityMap = new LinkedHashMap<>();
        this.entityList = new ArrayList<>();
    }

    /**
     * Adds an entity to the manager.
     * @param e The entity to add.
     * @throws DuplicateEntityIdException if the ID already exists.
     */
    public void addEntity(AbstractEntity e) {
        if (entityMap.containsKey(e.getId())) {
            throw new DuplicateEntityIdException(e.getId());
        }
        entityMap.put(e.getId(), e);
        entityList.add(e);
    }

    /**
     * Removes an entity by ID.
     * @param id The ID of the entity to remove.
     * @throws EntityNotFoundException if the ID is not found.
     */
    public void removeEntity(String id) {
        AbstractEntity e = entityMap.remove(id);
        if (e == null) {
            throw new EntityNotFoundException(id);
        }
        entityList.remove(e);
    }

    /**
     * Retrieves an entity by ID.
     * @param id The ID to search for.
     * @return The entity found.
     * @throws EntityNotFoundException if the ID is not found.
     */
    public AbstractEntity getEntity(String id) {
        AbstractEntity e = entityMap.get(id);
        if (e == null) {
            throw new EntityNotFoundException(id);
        }
        return e;
    }

    /**
     * Replaces an existing entity with a new one, preserving the ID in the map structure
     * but updating the reference.
     * @param id The ID of the entity to replace.
     * @param newEntity The new entity object (Must have the same ID ideally, or acts as the new value).
     */
    public void replaceEntity(String id, AbstractEntity newEntity) {
        if (!entityMap.containsKey(id)) {
            throw new EntityNotFoundException(id);
        }
        
        // Remove old
        AbstractEntity old = entityMap.get(id);
        int index = entityList.indexOf(old);
        
        // Update Map
        entityMap.put(id, newEntity);
        
        // Update List (preserve order)
        if (index != -1) {
            entityList.set(index, newEntity);
        } else {
            entityList.add(newEntity);
        }
    }

    /**
     * Updates all entities.
     * @param dt Delta time.
     */
    public void update(float dt) {
        // Loop backwards in case of removal during update (though typically we'd queue removals)
        // For simplicity in this abstract engine, standard iteration.
        // If concurrent modification happens, we might need a safe copy.
        // Assuming strict single-threaded update loop without internal removals during iteration for now.
        for (AbstractEntity e : entityList) {
            e.update(dt);
        }
    }

    /**
     * Renders all entities.
     * @param sr ShapeRenderer context.
     */
    public void render(ShapeRenderer sr) {
        for (AbstractEntity e : entityList) {
            e.render(sr);
        }
    }

    /**
     * @return A read-only view of the entity list for other managers (like CollisionManager).
     */
    public List<AbstractEntity> getEntities() {
        return Collections.unmodifiableList(entityList);
    }
}
