package com.sit.trafficking.logic.factories;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import java.util.ArrayList;
import com.sit.trafficking.engine.entities.AbstractEntity;

import com.badlogic.gdx.utils.JsonValue;
import com.sit.trafficking.engine.EngineConstants;
import com.sit.trafficking.engine.entities.DynamicEntity;
import com.sit.trafficking.engine.entities.StaticEntity;
import com.sit.trafficking.engine.interfaces.Movable;
import com.sit.trafficking.engine.managers.EntityManager;
import com.sit.trafficking.engine.scenes.SceneManager;
import com.sit.trafficking.logic.LogicConstants;

public class World {

    public World() {
    }

    public void loadWorld(EntityManager em, String jsonPath) {
        String jsonString = SceneManager.getInstance().getIOManager().readTextFile(jsonPath);
        parseAndCreate(jsonString, em);
    }

    public void loadSaveState(EntityManager em) {
        String jsonString = SceneManager.getInstance().getIOManager().readSaveFile(LogicConstants.SAVE_FILE_NAME);
        if (jsonString != null) {
            // Clear existing logic - simple clear by ID iteration since we don't have
            // clear()
            // We need a list copy to avoid concurrent modification exception if remove
            // modifies the collection
            java.util.List<AbstractEntity> entities = new java.util.ArrayList<>(em.getEntities());
            for (AbstractEntity e : entities) {
                em.removeEntity(e.getId());
            }

            parseAndCreate(jsonString, em);
        }
    }

    public void saveCurrentState(EntityManager em) {
        Json json = new Json();
        json.setOutputType(OutputType.json);

        // We create a simple data structure to hold the list for serialization
        ArrayList<EntityData> dataList = new ArrayList<>();

        for (AbstractEntity e : em.getEntities()) {
            EntityData data = new EntityData();
            data.id = e.getId();
            data.x = e.getPosition().x;
            data.y = e.getPosition().y;
            data.w = e.getWidth();
            data.h = e.getHeight();

            if (e.isStatic()) {
                data.type = EngineConstants.ENTITY_TYPE_STATIC;
            } else {
                data.type = EngineConstants.ENTITY_TYPE_DYNAMIC;
                if (e instanceof Movable) {
                    data.vx = ((Movable) e).getVelocity().x;
                    data.vy = ((Movable) e).getVelocity().y;
                }
            }
            dataList.add(data);
        }

        // Wrapper object
        WorldData world = new WorldData();
        world.entities = dataList;

        String saveString = json.toJson(world);
        SceneManager.getInstance().getIOManager().writeSaveFile(LogicConstants.SAVE_FILE_NAME, saveString);
    }

    private void parseAndCreate(String jsonString, EntityManager em) {
        JsonValue root = new JsonReader().parse(jsonString);
        JsonValue entities = root.get("entities");

        for (JsonValue val : entities) {
            String id = val.getString("id");
            String type = val.getString("type");
            float x = val.getFloat("x");
            float y = val.getFloat("y");
            float w = val.getFloat("w");
            float h = val.getFloat("h");

            // Optional velocity loading for saves
            float vx = val.has("vx") ? val.getFloat("vx") : 0;
            float vy = val.has("vy") ? val.getFloat("vy") : 0;

            if (EngineConstants.ENTITY_TYPE_STATIC.equals(type)) {
                em.addEntity(new StaticEntity(id, x, y, w, h));
            } else if (EngineConstants.ENTITY_TYPE_DYNAMIC.equals(type)) {
                DynamicEntity car = new DynamicEntity(id, x, y, w, h);
                car.setFriction(LogicConstants.VEHICLE_FRICTION);

                // If loading from save, use saved velocity.
                if (vx != 0 || vy != 0) {
                    car.setVelocity(vx, vy);
                }

                // Inject Logic (Crash Sound)
                car.setCollisionListener((source, target) -> {
                    if (source instanceof Movable) {
                        Movable m = (Movable) source;
                        if (m.getVelocity().len2() > LogicConstants.CRASH_SOUND_THRESHOLD) {
                            SceneManager.getInstance().getSoundManager().playSound(LogicConstants.SOUND_CRASH_ID, LogicConstants.DEFAULT_VOLUME);
                        }
                    }
                });

                em.addEntity(car);
            }
        }
    }

    // Inner classes for JSON serialization helper
    public static class WorldData {
        public ArrayList<EntityData> entities;
    }

    public static class EntityData {
        public String id;
        public String type;
        public float x, y, w, h;
        public float vx, vy;
    }
}
