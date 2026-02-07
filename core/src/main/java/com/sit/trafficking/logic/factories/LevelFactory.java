package com.sit.trafficking.logic.factories;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.sit.trafficking.engine.entities.DynamicEntity;
import com.sit.trafficking.engine.entities.StaticEntity;
import com.sit.trafficking.engine.interfaces.Movable;
import com.sit.trafficking.engine.managers.EntityManager;
import com.sit.trafficking.engine.scenes.SceneManager;
import com.sit.trafficking.logic.LogicConstants;

public class LevelFactory {

    public LevelFactory() {
    }

    public void loadLevel(EntityManager em, String jsonPath) {
        String jsonString = SceneManager.getInstance().getIOManager().readTextFile(jsonPath);
        parseAndCreate(jsonString, em);
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

            if ("STATIC".equals(type)) {
                em.addEntity(new StaticEntity(id, x, y, w, h));
            } else if ("DYNAMIC".equals(type)) {
                DynamicEntity car = new DynamicEntity(id, x, y, w, h);

                car.setFriction(LogicConstants.VEHICLE_FRICTION);
                
                // === LOGIC INJECTION START ===
                // Here we inject the specific behavior for the "Trafficking" game.
                car.setCollisionListener((source, target) -> {
                    if (source instanceof Movable) {
                         // Check impact magnitude
                         Movable m = (Movable) source;
                         if (m.getVelocity().len2() > LogicConstants.CRASH_SOUND_THRESHOLD) {
                             SceneManager.getInstance().getSoundManager().playSound("crash", LogicConstants.DEFAULT_VOLUME);
                         }
                    }
                });
                // === LOGIC INJECTION END ===
                
                em.addEntity(car);
            }
        }
    }
}
