package com.sit.trafficking.engine.factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.sit.trafficking.engine.entities.AbstractEntity;
import com.sit.trafficking.engine.entities.DynamicEntity;
import com.sit.trafficking.engine.entities.StaticEntity;
import com.sit.trafficking.engine.managers.EntityManager;

/**
 * Factory class to load levels from JSON.
 * Demonstrates parsing and Factory pattern.
 */
public class LevelFactory {

    /**
     * Loads entities from a JSON file into the EntityManager.
     * @param em The target EntityManager.
     * @param jsonPath Internal path to the JSON file.
     */
    public void loadLevel(EntityManager em, String jsonPath) {
        JsonReader reader = new JsonReader();
        JsonValue root = reader.parse(Gdx.files.internal(jsonPath));
        JsonValue entities = root.get("entities");

        for (JsonValue val : entities) {
            String id = val.getString("id");
            String type = val.getString("type");
            float x = val.getFloat("x");
            float y = val.getFloat("y");
            float w = val.getFloat("w");
            float h = val.getFloat("h");

            // Java 21 Switch Expression
            AbstractEntity entity = switch (type) {
                case "STATIC" -> new StaticEntity(id, x, y, w, h);
                case "DYNAMIC" -> new DynamicEntity(id, x, y, w, h);
                default -> throw new IllegalArgumentException("Unknown entity type: " + type);
            };

            em.addEntity(entity);
        }
    }
}
