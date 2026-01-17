package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

/**
 * Singleton manager for the Box2D physics world.
 * Wraps com.badlogic.gdx.physics.box2d.World.
 */
public final class PhysicsManager implements Disposable {
    private static PhysicsManager instance;
    private final World world;

    // Simulation constants
    private static final float TIME_STEP = 1 / 60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;

    private float accumulator = 0f;
    private float timeScale = 1.0f;

    private PhysicsManager() {
        // Default to no gravity (top-down view) as implied by "Trafficking" context.
        // Sleep is allowed for performance.
        this.world = new World(new Vector2(0, 0), true);
    }

    /**
     * Returns the singleton instance of PhysicsManager.
     * @return The instance.
     */
    public static synchronized PhysicsManager getInstance() {
        if (instance == null) {
            instance = new PhysicsManager();
        }
        return instance;
    }

    /**
     * Accessor for the underlying Box2D World.
     * @return The Box2D World object.
     */
    public World getWorld() {
        return world;
    }

    public void setTimeScale(float scale) {
        this.timeScale = scale;
    }

    /**
     * Steps the physics simulation using a fixed time step.
     * @param deltaTime Time elapsed since the last frame.
     */
    public void update(float deltaTime) {
        // Cap frame time to prevent "spiral of death" on slow frames
        // Apply time scale for slow-mo
        float logicTime = Math.min(deltaTime, 0.25f) * timeScale;
        accumulator += logicTime;

        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }
    }

    @Override
    public void dispose() {
        world.dispose();
        // Reset instance to ensure a fresh World is created if the game restarts in the same JVM context
        instance = null;
    }
}
