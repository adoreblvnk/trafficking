package com.sit.trafficking.logic.factories;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sit.trafficking.engine.managers.CollisionManager;
import com.sit.trafficking.engine.managers.EntityManager;
import com.sit.trafficking.engine.managers.InputManager;
import com.sit.trafficking.engine.managers.MovementManager;
import com.sit.trafficking.engine.scenes.SceneManager;
import com.sit.trafficking.logic.scenes.MenuScene;
import com.sit.trafficking.logic.scenes.PauseOverlay;
import com.sit.trafficking.logic.scenes.SimulationScene;

public class SceneFactory {
    private final SceneManager sceneManager;

    public SceneFactory(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public MenuScene createMenuScene() {
        return new MenuScene(
            sceneManager,
            this,
            new EntityManager(),
            new CollisionManager(),
            new InputManager(),
            new MovementManager(),
            new ShapeRenderer()
        );
    }

    public SimulationScene createSimulationScene() {
        return new SimulationScene(
            sceneManager,
            this,
            new EntityManager(),
            new CollisionManager(),
            new InputManager(),
            new MovementManager(),
            new ShapeRenderer()
        );
    }

    public PauseOverlay createPauseOverlay() {
        return new PauseOverlay(
            sceneManager,
            new EntityManager(),
            new CollisionManager(),
            new InputManager(),
            new MovementManager(),
            new ShapeRenderer()
        );
    }
}
