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
        context.getGraphics().clearScreen(0.1f, 0.1f, 0.1f);
        context.getGraphics().begin();

        // Full-screen dirty beach background
        context.getGraphics().drawTexture("dirty_beach", 0, 0, 1900, 1000);

        // Dark text for button labels
        context.getGraphics().setTextColor(0.2f, 0.15f, 0.1f, 1f);

        // Title with button background
        float titleBtnW = 480;
        float titleBtnH = 80;
        float titleBtnX = 950 - titleBtnW / 2;
        float titleBtnY = 560;
        context.getGraphics().drawTexture("button_rectangle_depth_flat", titleBtnX, titleBtnY, titleBtnW, titleBtnH);
        context.getGraphics().drawText("Recycling Pinball", "Geist-Bold", 830, 610);

        // Start prompt with button background
        float startBtnW = 480;
        float startBtnH = 64;
        float startBtnX = 950 - startBtnW / 2;
        float startBtnY = 460;
        context.getGraphics().drawTexture("button_rectangle_depth_flat", startBtnX, startBtnY, startBtnW, startBtnH);
        context.getGraphics().drawText("Start Game", "Geist-Bold", 890, 500);

        // Reset text color to white
        context.getGraphics().setTextColor(1f, 1f, 1f, 1f);

        context.getGraphics().end();
        super.render();
    }

    private boolean isClicked(int screenX, int screenY, float btnX, float btnY, float btnW, float btnH) {
        float mappedY = context.getDisplay().getHeight() - screenY;
        return screenX >= btnX && screenX <= btnX + btnW && mappedY >= btnY && mappedY <= btnY + btnH;
    }

    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) {
        float startBtnW = 480;
        float startBtnH = 64;
        float startBtnX = 950 - startBtnW / 2;
        float startBtnY = 460;

        if (isClicked(x, y, startBtnX, startBtnY, startBtnW, startBtnH)) {
            sceneManager.setScene(new LevelSelectScene(context, sceneManager));
            return true;
        }
        return false;
    }
    
    @Override public boolean onDrag(int x, int y, int ptr) { return false; }
    @Override public boolean onTouchUp(int x, int y, int ptr, int btn) { return false; }
}
