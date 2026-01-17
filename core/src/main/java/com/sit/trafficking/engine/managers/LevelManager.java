package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.sit.trafficking.engine.entities.StaticEntity;
import com.sit.trafficking.utils.Constants;

public final class LevelManager {
    private static LevelManager instance;

    private LevelManager() { }

    public static synchronized LevelManager getInstance() {
        if (instance == null) {
            instance = new LevelManager();
        }
        return instance;
    }

    public void loadLevel(String path) {
        try {
            JsonReader reader = new JsonReader();
            JsonValue root = reader.parse(Gdx.files.internal(path));
            
            // Expected format: { "walls": [ { "x": 0, "y": 0, "width": 1280, "height": 20 } ] }
            JsonValue walls = root.get("walls");
            if (walls != null) {
                for (JsonValue wall : walls) {
                    float w = wall.getFloat("width") / Constants.PPM;
                    float h = wall.getFloat("height") / Constants.PPM;
                    // Convert bottom-left (pixels) to center (meters)
                    float x = (wall.getFloat("x") / Constants.PPM) + (w / 2);
                    float y = (wall.getFloat("y") / Constants.PPM) + (h / 2);
                    
                    createWall(x, y, w, h);
                }
            }

        } catch (Exception e) {
            Gdx.app.error("LevelManager", "Failed to load level: " + path + ". Using fallback.");
            e.printStackTrace();
            createFallbackWalls();
        }
    }

    private void createFallbackWalls() {
        // Screen size in meters (1280/100 = 12.8, 720/100 = 7.2)
        float sw = 12.8f;
        float sh = 7.2f;
        float thickness = 0.5f;

        // Bottom
        createWall(sw / 2, thickness / 2, sw, thickness);
        // Top
        createWall(sw / 2, sh - thickness / 2, sw, thickness);
        // Left
        createWall(thickness / 2, sh / 2, thickness, sh);
        // Right
        createWall(sw - thickness / 2, sh / 2, thickness, sh);
    }

    private void createWall(float x, float y, float w, float h) {
        World world = PhysicsManager.getInstance().getWorld();
        
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.type = BodyDef.BodyType.StaticBody;
        
        Body body = world.createBody(bdef);
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(w / 2, h / 2);
        
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = Constants.BIT_WALL;
        fdef.filter.maskBits = Constants.BIT_PLAYER; // Collides with player/dynamic
        
        body.createFixture(fdef);
        shape.dispose();
        
        StaticEntity entity = new StaticEntity(body, w, h);
        EntityManager.getInstance().addEntity(entity);
    }
}
