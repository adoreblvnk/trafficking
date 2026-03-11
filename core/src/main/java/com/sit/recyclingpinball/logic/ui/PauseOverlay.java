package com.sit.recyclingpinball.logic.ui;

import com.badlogic.gdx.Input;
import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.managers.CollisionManager;
import com.sit.recyclingpinball.engine.managers.EntityManager;
import com.sit.recyclingpinball.engine.managers.InputManager;
import com.sit.recyclingpinball.engine.managers.MovementManager;
import com.sit.recyclingpinball.engine.scenes.AbstractScene;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.logic.scenes.MenuScene;

public class PauseOverlay extends AbstractScene implements InputListener {
    private final SceneManager sceneManager;

    public PauseOverlay(IEngineContext context, SceneManager sceneManager) {
        super(context, new EntityManager(), new CollisionManager(), new InputManager(), new MovementManager());
        this.sceneManager = sceneManager;
    }

    @Override
    public void create() {
        getInputManager().addListener(this);
    }

    @Override
    public void update(float dt) {
        // Pauses game time by not calling super.update(dt) or simply freezing physics
    }

    @Override
    public void render() {
        context.getGraphics().begin();
        context.getGraphics().fillRectangle(0, 0, 1900, 1000, 0, 0, 0, 0.5f);
        context.getGraphics().drawText("PAUSED", "Geist-Bold", 800, 600);
        context.getGraphics().drawText("Press ESC to Resume", "Geist-Bold", 800, 500);
        context.getGraphics().drawText("Press M for Main Menu", "Geist-Bold", 800, 450);
        context.getGraphics().end();
    }

    @Override
    public boolean onKeyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            sceneManager.popScene();
            return true;
        } else if (keycode == Input.Keys.M) {
            sceneManager.setScene(new MenuScene(context, sceneManager));
            return true;
        }
        return false;
    }

    @Override public boolean onTouchDown(int x, int y, int ptr, int btn) { return false; }
    @Override public boolean onDrag(int x, int y, int ptr) { return false; }
    @Override public boolean onTouchUp(int x, int y, int ptr, int btn) { return false; }
}
