package com.sit.recyclingpinball.logic.scenes;

import com.sit.recyclingpinball.engine.scenes.AbstractScene;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.managers.*;
import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.logic.level.Level1Blueprint;
import com.sit.recyclingpinball.logic.level.Level2Blueprint;

public class LevelSelectScene extends AbstractScene implements InputListener {
    private final SceneManager sceneManager;

    public LevelSelectScene(IEngineContext context, SceneManager sceneManager) {
        super(context, new EntityManager(), new CollisionManager(), new InputManager(), new MovementManager());
        this.sceneManager = sceneManager;
    }

    @Override
    public void create() {
        getInputManager().addListener(this);
    }

    @Override
    public void render() {
        context.getGraphics().clearScreen(0.2f, 0.6f, 0.4f);
        context.getGraphics().begin();
        context.getGraphics().drawText("Level Select", "Geist-Bold", 800, 700);
        context.getGraphics().drawText("1: " + new Level1Blueprint().getLevelName(), "Geist-Bold", 800, 600);
        context.getGraphics().drawText("2: " + new Level2Blueprint().getLevelName(), "Geist-Bold", 800, 500);
        context.getGraphics().drawText("Press 1 or 2 to play.", "Geist-Bold", 800, 400);
        context.getGraphics().end();
        super.render();
    }

    @Override
    public boolean onKeyDown(int keycode) {
        if (keycode == com.badlogic.gdx.Input.Keys.NUM_1) {
            sceneManager.setScene(new SimulationScene(context, sceneManager, new Level1Blueprint()));
            return true;
        } else if (keycode == com.badlogic.gdx.Input.Keys.NUM_2) {
            sceneManager.setScene(new SimulationScene(context, sceneManager, new Level2Blueprint()));
            return true;
        }
        return false;
    }

    @Override public boolean onTouchDown(int x, int y, int ptr, int btn) { return false; }
    @Override public boolean onDrag(int x, int y, int ptr) { return false; }
    @Override public boolean onTouchUp(int x, int y, int ptr, int btn) { return false; }
}
