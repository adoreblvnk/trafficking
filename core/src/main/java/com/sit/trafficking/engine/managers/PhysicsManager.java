package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public final class PhysicsManager implements Disposable {
    private static PhysicsManager instance;
    private final World world;
    
    // Safety Queue for removing bodies
    private final Array<Body> bodiesToDestroy = new Array<>();

    // Simulation constants
    private static final float TIME_STEP = 1 / 60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;

    private float accumulator = 0f;
    private float timeScale = 1.0f;

    private PhysicsManager() {
        this.world = new World(new Vector2(0, 0), true);
    }

    public static synchronized PhysicsManager getInstance() {
        if (instance == null) {
            instance = new PhysicsManager();
        }
        return instance;
    }

    public World getWorld() {
        return world;
    }

    public void setTimeScale(float scale) {
        this.timeScale = scale;
    }
    
    /**
     * Safely queues a body to be destroyed after the physics step.
     */
    public void destroyBody(Body body) {
        if (!bodiesToDestroy.contains(body, true)) {
            bodiesToDestroy.add(body);
        }
    }

    public void update(float deltaTime) {
        float logicTime = Math.min(deltaTime, 0.25f) * timeScale;
        accumulator += logicTime;

        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }
        
        // SAFE DESTRUCTION PHASE
        if (bodiesToDestroy.size > 0) {
            for (Body b : bodiesToDestroy) {
                if(b != null) world.destroyBody(b);
            }
            bodiesToDestroy.clear();
        }
    }

    @Override
    public void dispose() {
        world.dispose();
        instance = null;
    }
}
