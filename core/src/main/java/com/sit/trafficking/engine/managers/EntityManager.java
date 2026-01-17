package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sit.trafficking.engine.entities.Entity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class EntityManager {
    private static EntityManager instance;
    private final List<Entity> entities;

    private EntityManager() {
        this.entities = new ArrayList<>();
    }

    public static synchronized EntityManager getInstance() {
        if (instance == null) {
            instance = new EntityManager();
        }
        return instance;
    }

    public void addEntity(Entity e) {
        entities.add(e);
    }

    public void update(float dt) {
        Iterator<Entity> it = entities.iterator();
        while (it.hasNext()) {
            Entity e = it.next();
            if (e.isMarkedForDelete()) {
                e.dispose();
                it.remove();
            } else {
                e.update(dt);
            }
        }
    }

    public void render(ShapeRenderer sr) {
        for (Entity e : entities) {
            e.render(sr);
        }
    }
    
    public void clear() {
        for(Entity e : entities) {
            e.dispose();
        }
        entities.clear();
    }
}
