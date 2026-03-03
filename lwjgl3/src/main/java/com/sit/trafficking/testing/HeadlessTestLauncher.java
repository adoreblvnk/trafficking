package com.sit.trafficking.testing;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.sit.trafficking.engine.entities.DynamicEntity;
import com.sit.trafficking.engine.entities.StaticEntity;
import com.sit.trafficking.engine.managers.CollisionManager;
import com.sit.trafficking.engine.managers.EntityManager;
import com.sit.trafficking.engine.managers.MovementManager;

import java.util.ArrayList;
import java.util.List;

/**
 * HEADLESS INTEGRATION TEST SUITE
 * automated testing for abstract engine using LibGDX headless mode for reproducible deterministic testing
 */
public class HeadlessTestLauncher {

    public static void main(String[] args) {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        config.updatesPerSecond = 60;

        System.out.println("====================================================");
        System.out.println("   STARTING ABSTRACT ENGINE INTEGRATION SUITE");
        System.out.println("====================================================");

        new HeadlessApplication(new TestSuiteListener(), config);
    }

    @FunctionalInterface
    interface TestCase {
        /** @return true if test completed, false if still running */
        boolean run();
    }

    static class TestSuiteListener extends ApplicationAdapter {
        EntityManager entityManager;
        MovementManager movementManager;
        CollisionManager collisionManager;

        int frames = 0;
        int currentTestIndex = 0;
        boolean isExiting = false;

        List<TestCase> tests = new ArrayList<>();

        @Override
        public void create() {
            resetEngine();
            // Register all tests here - add new tests to this list
            tests.add(this::runTest_MovementPhysics);
            tests.add(this::runTest_CollisionResolution);
            tests.add(this::runTest_FaultTolerance);
        }

        private void resetEngine() {
            entityManager = new EntityManager();
            movementManager = new MovementManager();
            collisionManager = new CollisionManager();
        }

        @Override
        public void render() {
            if (isExiting) return;

            float dt = 0.016f; // Simulate 60 FPS lock

            try {
                // manually update the engine loop for headless testing
                entityManager.update(dt);
                movementManager.processMovement(entityManager.getEntities(), dt);
                collisionManager.processCollisions(entityManager.getEntities());

                frames++;

                // execute current test
                if (currentTestIndex < tests.size()) {
                    boolean completed = tests.get(currentTestIndex).run();
                    if (completed) {
                        currentTestIndex++;
                        frames = 0;
                        resetEngine();
                    }
                } else {
                    isExiting = true;
                    Gdx.app.exit();
                }

            } catch (Exception e) {
                isExiting = true;
                System.err.println("  CRITICAL FAILURE: Engine crashed during tests.");
                e.printStackTrace();
                Gdx.app.exit();
            }
        }

        // AT01: test movement physics
        private boolean runTest_MovementPhysics() {
            if (frames == 1) {
                System.out.println("[TEST 1] Movement Physics Check");
                System.out.println("   Input:    Entity 'mover' at position (0, 0) with velocity (100, 0) px/s");
                System.out.println("   Expected: Position.x > 0 after 10 frames (160ms simulated)");
                DynamicEntity car = new DynamicEntity("mover", 0, 0, 10, 10);
                car.setVelocity(100, 0);
                entityManager.addEntity(car);
            }

            if (frames == 10) {
                DynamicEntity car = (DynamicEntity) entityManager.getEntity("mover");
                float actualX = car.getPosition().x;
                float actualY = car.getPosition().y;
                System.out.println("   Actual:   Position = (" + actualX + ", " + actualY + ") after 10 frames");
                if (actualX > 0) {
                    System.out.println("   ->   PASS: Velocity integrated correctly (moved " + actualX + " px in X-axis)");
                    return true;
                } else {
                    throw new RuntimeException("FAIL: Entity did not move. Expected Position.x > 0, Actual: " + actualX);
                }
            }
            return false;
        }

        // AT02: test collision resolution with high-speed entity
        private boolean runTest_CollisionResolution() {
            if (frames == 1) {
                System.out.println("[TEST 2] Collision Resolution Check");
                System.out.println("   Setup:    DynamicEntity 'crasher' at (0, 0), size 10x10, velocity (500, 0) px/s");
                System.out.println("             StaticEntity 'wall' at (20, 0), size 10x100");
                System.out.println("   Expected: Entity.x < 25 (stopped before tunneling through wall)");
                // Place car at 0, moving right
                DynamicEntity car = new DynamicEntity("crasher", 0, 0, 10, 10);
                car.setVelocity(500, 0); // High speed

                // place wall at 20, car should hit it immediately
                StaticEntity wall = new StaticEntity("wall", 20f, 0f, 10f, 100f, 0.5f, 0.5f, 0.5f);

                entityManager.addEntity(car);
                entityManager.addEntity(wall);
            }

            if (frames == 10) {
                DynamicEntity car = (DynamicEntity) entityManager.getEntity("crasher");
                float actualX = car.getPosition().x;
                float actualY = car.getPosition().y;
                // with AABB resollution, the car should be stopped at x=10 (touching left side of wall)
                System.out.println("   Actual:   Entity stopped at position (" + actualX + ", " + actualY + ")");

                if (actualX < 25) {
                    float distanceFromWall = 20 - actualX;
                    System.out.println("   ->   PASS: Collision resolved - entity stopped " + distanceFromWall + " px from wall origin");
                    return true;
                } else {
                    throw new RuntimeException("FAIL: Entity tunneled through wall. Expected Position.x < 25, Actual: " + actualX);
                }
            }
            return false;
        }

        // AT03: test engine fault tolerance
        private boolean runTest_FaultTolerance() {
            if (frames == 1) {
                System.out.println("[TEST 3] Fault Tolerance (Sad Path) Check");

                // AT03: add null entity without crashing
                System.out.println("   Test 3a: Add null entity to EntityManager");
                System.out.println("   Expected: No exception thrown, null ignored");
                try {
                    entityManager.addEntity(null);
                    System.out.println("   Actual:   Operation completed without exception");
                    System.out.println("   ->   PASS: Null entity addition handled gracefully");
                } catch (Exception e) {
                    throw new RuntimeException("FAIL: Crash on null add. Exception: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                }

                // AT04: remove non-existent entity without crashing
                System.out.println("   Test 3b: Remove non-existent entity 'ghost_entity'");
                System.out.println("   Expected: No exception thrown, no-op behavior");
                try {
                    entityManager.removeEntity("ghost_entity");
                    System.out.println("   Actual:   Operation completed without exception");
                    System.out.println("   ->   PASS: Invalid removal handled gracefully");
                } catch (Exception e) {
                    throw new RuntimeException("FAIL: Crash on invalid remove. Exception: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                }
            }

            if (frames == 5) {
                return true;
            }
            return false;
        }
    }
}