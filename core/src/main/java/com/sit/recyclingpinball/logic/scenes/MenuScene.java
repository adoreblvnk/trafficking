package com.sit.recyclingpinball.logic.scenes;

import com.sit.recyclingpinball.engine.scenes.AbstractScene;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.managers.*;
import com.sit.recyclingpinball.engine.interfaces.InputListener;

public class MenuScene extends AbstractScene implements InputListener {
    private final SceneManager sceneManager;

    public MenuScene(IEngineContext context, SceneManager sceneManager) {
        super(context, new EntityManager(),
                new CollisionManager(
                        new com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle(0, 0, 1920, 1080)),
                new InputManager(), new MovementManager());
        this.sceneManager = sceneManager;
    }

    @Override
    public void create() {
        getInputManager().addListener(this);
    }

    @Override
    public void render() {
        getContext().getGraphics().clearScreen(com.sit.recyclingpinball.logic.LogicConstants.COLOR_BG_R,
                com.sit.recyclingpinball.logic.LogicConstants.COLOR_BG_G,
                com.sit.recyclingpinball.logic.LogicConstants.COLOR_BG_B);
        getContext().getGraphics().begin();

        // Full-screen dirty beach background
        getContext().getGraphics().drawTexture(com.sit.recyclingpinball.logic.LogicConstants.TEX_DIRTY_BEACH, 0, 0,
                com.sit.recyclingpinball.logic.LogicConstants.SCENE_WIDTH,
                com.sit.recyclingpinball.logic.LogicConstants.SCENE_HEIGHT);

        // Dark text for button labels
        getContext().getGraphics().setTextColor(com.sit.recyclingpinball.logic.LogicConstants.COLOR_TEXT_DARK_R,
                com.sit.recyclingpinball.logic.LogicConstants.COLOR_TEXT_DARK_G,
                com.sit.recyclingpinball.logic.LogicConstants.COLOR_TEXT_DARK_B,
                com.sit.recyclingpinball.logic.LogicConstants.COLOR_TEXT_A);

        // Title with button background
        float titleBtnW = com.sit.recyclingpinball.logic.LogicConstants.UI_BTN_WIDTH_DEFAULT;
        float titleBtnH = com.sit.recyclingpinball.logic.LogicConstants.UI_BTN_HEIGHT_LARGE;
        float titleBtnX = com.sit.recyclingpinball.logic.LogicConstants.UI_CENTER_X - titleBtnW / 2;
        float titleBtnY = 560;
        getContext().getGraphics().drawTexture(com.sit.recyclingpinball.logic.LogicConstants.TEX_BUTTON_RECT_DEPTH_FLAT,
                titleBtnX, titleBtnY, titleBtnW, titleBtnH);
        getContext().getGraphics().drawTextCentered(
                com.sit.recyclingpinball.logic.LogicConstants.TEXT_RECYCLING_PINBALL,
                com.sit.recyclingpinball.logic.LogicConstants.FONT_GEIST_BOLD, titleBtnX, titleBtnY, titleBtnW,
                titleBtnH);

        // Start prompt with button background
        float startBtnW = com.sit.recyclingpinball.logic.LogicConstants.UI_BTN_WIDTH_DEFAULT;
        float startBtnH = com.sit.recyclingpinball.logic.LogicConstants.UI_BTN_HEIGHT_DEFAULT;
        float startBtnX = com.sit.recyclingpinball.logic.LogicConstants.UI_CENTER_X - startBtnW / 2;
        float startBtnY = 460;
        getContext().getGraphics().drawTexture(com.sit.recyclingpinball.logic.LogicConstants.TEX_BUTTON_RECT_DEPTH_FLAT,
                startBtnX, startBtnY, startBtnW, startBtnH);
        getContext().getGraphics().drawTextCentered(com.sit.recyclingpinball.logic.LogicConstants.TEXT_START_GAME,
                com.sit.recyclingpinball.logic.LogicConstants.FONT_GEIST_BOLD, startBtnX, startBtnY, startBtnW,
                startBtnH);

        // Quit prompt with button background
        float quitBtnW = com.sit.recyclingpinball.logic.LogicConstants.UI_BTN_WIDTH_DEFAULT;
        float quitBtnH = com.sit.recyclingpinball.logic.LogicConstants.UI_BTN_HEIGHT_DEFAULT;
        float quitBtnX = com.sit.recyclingpinball.logic.LogicConstants.UI_CENTER_X - quitBtnW / 2;
        float quitBtnY = 380;
        getContext().getGraphics().drawTexture(com.sit.recyclingpinball.logic.LogicConstants.TEX_BUTTON_RECT_DEPTH_FLAT,
                quitBtnX, quitBtnY, quitBtnW, quitBtnH);
        getContext().getGraphics().drawTextCentered(com.sit.recyclingpinball.logic.LogicConstants.TEXT_QUIT,
                com.sit.recyclingpinball.logic.LogicConstants.FONT_GEIST_BOLD, quitBtnX, quitBtnY, quitBtnW, quitBtnH);

        // Reset text color to white
        getContext().getGraphics().setTextColor(com.sit.recyclingpinball.logic.LogicConstants.COLOR_TEXT_LIGHT_R,
                com.sit.recyclingpinball.logic.LogicConstants.COLOR_TEXT_LIGHT_G,
                com.sit.recyclingpinball.logic.LogicConstants.COLOR_TEXT_LIGHT_B,
                com.sit.recyclingpinball.logic.LogicConstants.COLOR_TEXT_A);

        getContext().getGraphics().end();
        super.render();
    }

    private boolean isClicked(int screenX, int screenY, float btnX, float btnY, float btnW, float btnH) {
        float mappedY = getContext().getDisplay().getHeight() - screenY;
        return screenX >= btnX && screenX <= btnX + btnW && mappedY >= btnY && mappedY <= btnY + btnH;
    }

    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) {
        float startBtnW = com.sit.recyclingpinball.logic.LogicConstants.UI_BTN_WIDTH_DEFAULT;
        float startBtnH = com.sit.recyclingpinball.logic.LogicConstants.UI_BTN_HEIGHT_DEFAULT;
        float startBtnX = com.sit.recyclingpinball.logic.LogicConstants.UI_CENTER_X - startBtnW / 2;
        float startBtnY = 460;

        float quitBtnW = com.sit.recyclingpinball.logic.LogicConstants.UI_BTN_WIDTH_DEFAULT;
        float quitBtnH = com.sit.recyclingpinball.logic.LogicConstants.UI_BTN_HEIGHT_DEFAULT;
        float quitBtnX = com.sit.recyclingpinball.logic.LogicConstants.UI_CENTER_X - quitBtnW / 2;
        float quitBtnY = 380;

        if (isClicked(x, y, startBtnX, startBtnY, startBtnW, startBtnH)) {
            getContext().getAudio().playSound(com.sit.recyclingpinball.logic.LogicConstants.SOUND_CLICK,
                    com.sit.recyclingpinball.logic.LogicConstants.VOLUME_DEFAULT);
            sceneManager.setScene(new LevelSelectScene(getContext(), sceneManager));
            return true;
        }

        if (isClicked(x, y, quitBtnX, quitBtnY, quitBtnW, quitBtnH)) {
            getContext().getAudio().playSound(com.sit.recyclingpinball.logic.LogicConstants.SOUND_CLICK,
                    com.sit.recyclingpinball.logic.LogicConstants.VOLUME_DEFAULT);
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
