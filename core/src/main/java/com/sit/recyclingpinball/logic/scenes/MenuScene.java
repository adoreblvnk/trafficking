package com.sit.recyclingpinball.logic.scenes;

import com.sit.recyclingpinball.engine.scenes.AbstractScene;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.managers.*;
import com.sit.recyclingpinball.engine.interfaces.InputListener;

public class MenuScene extends AbstractScene implements InputListener {
    private final SceneManager sceneManager;

    public MenuScene(IEngineContext context, SceneManager sceneManager) {
        super(context, new EntityManager(), new CollisionManager(new com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle(0, 0, 1920, 1080)), new InputManager(), new MovementManager());
        this.sceneManager = sceneManager;
    }

    @Override
    public void create() {
        getInputManager().addListener(this);
    }

    @Override
    public void render() {
        getContext().getGraphics().clearScreen(0.1f, 0.1f, 0.1f);
        getContext().getGraphics().begin();

        // Full-screen dirty beach background
        getContext().getGraphics().drawTexture("dirty_beach", 0, 0, 1900, 1000);

        // Dark text for button labels
        getContext().getGraphics().setTextColor(0.2f, 0.15f, 0.1f, 1f);

        // Title with button background
        float titleBtnW = 480;
        float titleBtnH = 80;
        float titleBtnX = 950 - titleBtnW / 2;
        float titleBtnY = 560;
        getContext().getGraphics().drawTexture("button_rectangle_depth_flat", titleBtnX, titleBtnY, titleBtnW, titleBtnH);
        getContext().getGraphics().drawTextCentered("Recycling Pinball", "Geist-Bold", titleBtnX, titleBtnY, titleBtnW, titleBtnH);

        // Start prompt with button background
        float startBtnW = 480;
        float startBtnH = 64;
        float startBtnX = 950 - startBtnW / 2;
        float startBtnY = 460;
        getContext().getGraphics().drawTexture("button_rectangle_depth_flat", startBtnX, startBtnY, startBtnW, startBtnH);
        getContext().getGraphics().drawTextCentered("Start Game", "Geist-Bold", startBtnX, startBtnY, startBtnW, startBtnH);

        // Quit prompt with button background
        float quitBtnW = 480;
        float quitBtnH = 64;
        float quitBtnX = 950 - quitBtnW / 2;
        float quitBtnY = 380;
        getContext().getGraphics().drawTexture("button_rectangle_depth_flat", quitBtnX, quitBtnY, quitBtnW, quitBtnH);
        getContext().getGraphics().drawTextCentered("Quit", "Geist-Bold", quitBtnX, quitBtnY, quitBtnW, quitBtnH);

        // Reset text color to white
        getContext().getGraphics().setTextColor(1f, 1f, 1f, 1f);

        getContext().getGraphics().end();
        super.render();
    }

    private boolean isClicked(int screenX, int screenY, float btnX, float btnY, float btnW, float btnH) {
        float mappedY = getContext().getDisplay().getHeight() - screenY;
        return screenX >= btnX && screenX <= btnX + btnW && mappedY >= btnY && mappedY <= btnY + btnH;
    }

    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) {
        float startBtnW = 480;
        float startBtnH = 64;
        float startBtnX = 950 - startBtnW / 2;
        float startBtnY = 460;

        float quitBtnW = 480;
        float quitBtnH = 64;
        float quitBtnX = 950 - quitBtnW / 2;
        float quitBtnY = 380;

        if (isClicked(x, y, startBtnX, startBtnY, startBtnW, startBtnH)) {
            getContext().getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new LevelSelectScene(getContext(), sceneManager));
            return true;
        }

        if (isClicked(x, y, quitBtnX, quitBtnY, quitBtnW, quitBtnH)) {
            getContext().getAudio().playSound("click", 1.0f);
            getContext().exit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onDrag(int x, int y, int ptr) {
        return false;
    }

    @Override
    public boolean onTouchUp(int x, int y, int ptr, int btn) {
        return false;
    }
}
