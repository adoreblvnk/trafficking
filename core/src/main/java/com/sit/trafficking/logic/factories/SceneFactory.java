package com.sit.trafficking.logic.factories;

import com.sit.trafficking.engine.interfaces.providers.IEngineContext;
import com.sit.trafficking.engine.managers.CollisionManager;
import com.sit.trafficking.engine.managers.EntityManager;
import com.sit.trafficking.engine.managers.InputManager;
import com.sit.trafficking.engine.managers.MovementManager;
import com.sit.trafficking.engine.scenes.SceneManager;
import com.sit.trafficking.logic.scenes.MenuScene;
import com.sit.trafficking.logic.scenes.PauseOverlay;
import com.sit.trafficking.logic.scenes.SimulationScene;

/**
 * Factory for creating game scenes with dependency injection.
 * Accepts IEngineContext for platform-independent scene creation.
 */
public class SceneFactory {
    private final SceneManager sceneManager;
    private final IEngineContext context;

    public SceneFactory(SceneManager sceneManager, IEngineContext context) {
        if (sceneManager == null) {
            throw new IllegalArgumentException("SceneManager cannot be null");
        }
        if (context == null) {
            throw new IllegalArgumentException("EngineContext cannot be null");
        }
        this.sceneManager = sceneManager;
        this.context = context;
    }

    public MenuScene createMenuScene() {
        return new MenuScene(
            context,
            sceneManager,
            this,
            new EntityManager(),
            new CollisionManager(),
            new InputManager(),
            new MovementManager()
        );
    }

    public SimulationScene createSimulationScene() {
        return new SimulationScene(
            context,
            sceneManager,
            this,
            new EntityManager(),
            new CollisionManager(),
            new InputManager(),
            new MovementManager()
        );
    }

    public PauseOverlay createPauseOverlay() {
        return new PauseOverlay(
            context,
            sceneManager,
            new EntityManager(),
            new CollisionManager(),
            new InputManager(),
            new MovementManager()
        );
    }
}
