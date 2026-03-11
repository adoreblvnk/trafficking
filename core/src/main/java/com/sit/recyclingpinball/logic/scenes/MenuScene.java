package com.sit.recyclingpinball.logic.scenes;

import com.sit.recyclingpinball.engine.scenes.AbstractScene;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.managers.*;
import com.sit.recyclingpinball.engine.interfaces.InputListener;

public class MenuScene extends AbstractScene implements InputListener {
    private final SceneManager sceneManager;

    public MenuScene(IEngineContext context, SceneManager sceneManager) {
        super(context, new EntityManager(), new CollisionManager(), new InputManager(), new MovementManager());
        this.sceneManager = sceneManager;
    }

    @Override
    public void create() {
        getInputManager().addListener(this);
    }

    @Override
    public void render() {
        context.getGraphics().clearScreen(0.2f, 0.4f, 0.6f);
        context.getGraphics().begin();
        context.getGraphics().drawText("Recycling Pinball", "Geist-Bold", 800, 600);
        context.getGraphics().drawText("Click anywhere to Start", "Geist-Bold", 800, 500);
        context.getGraphics().end();
        super.render();
    }

    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) {
        sceneManager.setScene(new LevelSelectScene(context, sceneManager));
        return true;
    }
    
    @Override public boolean onDrag(int x, int y, int ptr) { return false; }
    @Override public boolean onTouchUp(int x, int y, int ptr, int btn) { return false; }
}
