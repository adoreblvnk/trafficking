package com.sit.covid26.logic.factories;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.sit.covid26.engine.EngineConstants;
import com.sit.covid26.engine.entities.AbstractEntity;
import com.sit.covid26.engine.entities.DynamicEntity;
import com.sit.covid26.engine.entities.StaticEntity;
import com.sit.covid26.engine.interfaces.Movable;
import com.sit.covid26.engine.managers.EntityManager;
import com.sit.covid26.engine.managers.IOManager;
import com.sit.covid26.engine.managers.SoundManager;
import com.sit.covid26.logic.LogicConstants;

import java.util.ArrayList;
import java.util.Optional;

import com.badlogic.gdx.utils.JsonValue;

/**
 * World data factory for loading and saving game world state.
 * No longer directly depends on Gdx - accepts screen dimensions as parameters.
 */
public class World {

    private final IOManager ioManager;
    private final SoundManager soundManager;

    public World(IOManager ioManager, SoundManager soundManager) {
        this.ioManager = ioManager;
        this.soundManager = soundManager;
    }

    public boolean loadWorld(EntityManager em, String jsonPath) {
        Optional<String> jsonString = ioManager.readTextFile(jsonPath);
        if (jsonString.isPresent()) {
            parseAndCreate(jsonString.get(), em, 800, 600); // Default dimensions
            return true;
        }
        return false;
    }

    public boolean loadSaveState(EntityManager em, float screenWidth, float screenHeight) {
        Optional<String> jsonString = ioManager.readSaveFile(LogicConstants.SAVE_FILE_NAME);
        if (jsonString.isPresent()) {
            // create a separate list to avoid concurrent modification of `em`
            java.util.List<AbstractEntity> entities = new java.util.ArrayList<>(em.getEntities());
            for (AbstractEntity e : entities) {
                em.removeEntity(e.getId());
            }

            parseAndCreate(jsonString.get(), em, screenWidth, screenHeight);
            return true;
        }
        return false;
    }

    public boolean saveCurrentState(EntityManager em, float screenWidth, float screenHeight) {
        try {
            Json json = new Json();
            json.setOutputType(OutputType.json);

            ArrayList<EntityData> dataList = new ArrayList<>();

            for (AbstractEntity e : em.getEntities()) {
                // Skip dynamically generated borders
                if (e.getId().startsWith("border_")) continue;

                EntityData data = new EntityData();
                data.id = e.getId();
                data.relX = e.getPosition().x / screenWidth;
                data.relY = e.getPosition().y / screenHeight;
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

            SaveData saveData = new SaveData();
            saveData.metadata = new SaveMetadata();
            saveData.metadata.screenWidth = screenWidth;
            saveData.metadata.screenHeight = screenHeight;
            saveData.entities = dataList;

            String saveString = json.toJson(saveData);
            return ioManager.writeSaveFile(LogicConstants.SAVE_FILE_NAME, saveString);
        } catch (Exception e) {
            return false;
        }
    }

    private void parseAndCreate(String jsonString, EntityManager em, float currentScreenW, float currentScreenH) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return;
        }

        try {
            JsonValue root = new JsonReader().parse(jsonString);

            if (root == null) {
                return;
            }

            JsonValue entities = root.get("entities");

            if (entities == null) {
                return;
            }

            for (JsonValue val : entities) {
                try {
                    if (!val.has("id") || !val.has("type")) {
                        continue;
                    }

                    String id = val.getString("id");
                    String type = val.getString("type");
                    float w = val.getFloat("w");
                    float h = val.getFloat("h");

                    // Support both relative (new) and absolute (legacy) positioning
                    float x, y;
                    if (val.has("relX") && val.has("relY")) {
                        // Relative positioning scales to current screen size
                        x = val.getFloat("relX") * currentScreenW;
                        y = val.getFloat("relY") * currentScreenH;
                    } else if (val.has("x") && val.has("y")) {
                        // Legacy absolute positioning (backward compatibility)
                        x = val.getFloat("x");
                        y = val.getFloat("y");
                    } else {
                        continue;
                    }

                    float vx = val.has("vx") ? val.getFloat("vx") : 0;
                    float vy = val.has("vy") ? val.getFloat("vy") : 0;

                    if (EngineConstants.ENTITY_TYPE_STATIC.equals(type)) {
                        em.addEntity(new StaticEntity(id, x, y, w, h, 0.5f, 0.5f, 0.5f)); // Gray in RGB
                    } else if (EngineConstants.ENTITY_TYPE_DYNAMIC.equals(type)) {
                        DynamicEntity car = new DynamicEntity(id, x, y, w, h);
                        car.setFriction(LogicConstants.VEHICLE_FRICTION);

                        if (vx != 0 || vy != 0) {
                            car.setVelocity(vx, vy);
                        }

                        car.setCollisionListener((source, target) -> {
                            if (source instanceof Movable) {
                                Movable m = (Movable) source;
                                if (m.getVelocity().len2() > LogicConstants.CRASH_SOUND_THRESHOLD) {
                                    soundManager.playSound(LogicConstants.SOUND_CRASH_ID, LogicConstants.DEFAULT_VOLUME);
                                }
                            }
                        });

                        em.addEntity(car);
                    }
                } catch (Exception e) {
                    // Silent fail for individual entities
                }
            }

        } catch (Exception e) {
            // Silent fail for overall parsing
        }
    }

    // Data structures for save/load with relative positioning
    public static class SaveData {
        public SaveMetadata metadata;
        public ArrayList<EntityData> entities;
    }

    public static class SaveMetadata {
        public float screenWidth;
        public float screenHeight;
    }

    public static class EntityData {
        public String id;
        public String type;
        public float relX, relY;  // Relative position (0.0 to 1.0) for cross-screen compatibility
        public float w, h;        // Absolute size (constant)
        public float vx, vy;
    }
}
