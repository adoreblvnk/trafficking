package com.sit.recyclingpinball.logic.ui;

import com.sit.recyclingpinball.engine.interfaces.providers.EngineKey;

import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.managers.CollisionManager;
import com.sit.recyclingpinball.engine.managers.EntityManager;
import com.sit.recyclingpinball.engine.managers.InputManager;
import com.sit.recyclingpinball.engine.managers.MovementManager;
import com.sit.recyclingpinball.engine.scenes.AbstractScene;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.logic.LogicConstants;
import com.sit.recyclingpinball.logic.scenes.MenuScene;

public class PauseOverlay extends AbstractScene implements InputListener {
    private final SceneManager sceneManager;

    public PauseOverlay(IEngineContext context, SceneManager sceneManager) {
        super(context, new EntityManager(), new CollisionManager(new com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle(0, 0, 1920, 1080)), new InputManager(), new MovementManager());
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
        getContext().getGraphics().begin();
        // 60% opacity — dark enough to communicate "paused", light enough to see game state.
        // Button backgrounds behind text already ensure readability, so we don't need heavier dimming.
        getContext().getGraphics().fillRectangle(0, 0, LogicConstants.SCENE_WIDTH, LogicConstants.SCENE_HEIGHT, LogicConstants.COLOR_DIM_R, LogicConstants.COLOR_DIM_G, LogicConstants.COLOR_DIM_B, LogicConstants.COLOR_DIM_PAUSED_A);

        // Dark text on light buttons
        getContext().getGraphics().setTextColor(LogicConstants.COLOR_TEXT_DARK_R, LogicConstants.COLOR_TEXT_DARK_G, LogicConstants.COLOR_TEXT_DARK_B, LogicConstants.COLOR_TEXT_A);

        // PAUSED title with button background
        float pauseBtnW = LogicConstants.UI_BTN_WIDTH_SMALL;
        float pauseBtnH = LogicConstants.UI_BTN_HEIGHT_LARGE;
        float pauseBtnX = LogicConstants.UI_CENTER_X - pauseBtnW / 2;
        float pauseBtnY = 580;
        getContext().getGraphics().drawTexture(LogicConstants.TEX_BUTTON_RECT_DEPTH_FLAT, pauseBtnX, pauseBtnY, pauseBtnW, pauseBtnH);
        getContext().getGraphics().drawTextCentered(LogicConstants.TEXT_PAUSED, LogicConstants.FONT_GEIST_BOLD, pauseBtnX, pauseBtnY, pauseBtnW, pauseBtnH);

        // Resume button
        float btnW = LogicConstants.UI_BTN_WIDTH_SMALL;
        float btnH = LogicConstants.UI_BTN_HEIGHT_DEFAULT;
        float btnX = LogicConstants.UI_CENTER_X - btnW / 2;
        getContext().getGraphics().drawTexture(LogicConstants.TEX_BUTTON_RECT_DEPTH_FLAT, btnX, 490, btnW, btnH);
        getContext().getGraphics().drawTextCentered(LogicConstants.TEXT_RESUME, LogicConstants.FONT_GEIST_BOLD, btnX, 490, btnW, btnH);

        // Main Menu button
        getContext().getGraphics().drawTexture(LogicConstants.TEX_BUTTON_RECT_DEPTH_FLAT, btnX, 410, btnW, btnH);
        getContext().getGraphics().drawTextCentered(LogicConstants.TEXT_MAIN_MENU, LogicConstants.FONT_GEIST_BOLD, btnX, 410, btnW, btnH);

        // Reset text color to white
        getContext().getGraphics().setTextColor(LogicConstants.COLOR_TEXT_LIGHT_R, LogicConstants.COLOR_TEXT_LIGHT_G, LogicConstants.COLOR_TEXT_LIGHT_B, LogicConstants.COLOR_TEXT_A);

        getContext().getGraphics().end();
    }

    private boolean isClicked(int screenX, int screenY, float btnX, float btnY, float btnW, float btnH) {
        float mappedY = getContext().getDisplay().getHeight() - screenY;
        return screenX >= btnX && screenX <= btnX + btnW && mappedY >= btnY && mappedY <= btnY + btnH;
    }

    @Override
    public boolean onKeyDown(EngineKey keycode) {
        if (keycode == EngineKey.ESCAPE) {
            getContext().getAudio().playSound(LogicConstants.SOUND_CLICK, LogicConstants.VOLUME_DEFAULT);
            sceneManager.popScene();
            return true;
        } else if (keycode == EngineKey.M) {
            getContext().getAudio().playSound(LogicConstants.SOUND_CLICK, LogicConstants.VOLUME_DEFAULT);
            sceneManager.setScene(new MenuScene(getContext(), sceneManager));
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) {
        float btnW = LogicConstants.UI_BTN_WIDTH_SMALL;
        float btnH = LogicConstants.UI_BTN_HEIGHT_DEFAULT;
        float btnX = LogicConstants.UI_CENTER_X - btnW / 2;

        if (isClicked(x, y, btnX, 490, btnW, btnH)) {
            getContext().getAudio().playSound(LogicConstants.SOUND_CLICK, LogicConstants.VOLUME_DEFAULT);
            sceneManager.popScene();
            return true;
        }
        if (isClicked(x, y, btnX, 410, btnW, btnH)) {
            getContext().getAudio().playSound(LogicConstants.SOUND_CLICK, LogicConstants.VOLUME_DEFAULT);
            sceneManager.setScene(new MenuScene(getContext(), sceneManager));
            return true;
        }
        return false;
    }
    @Override public boolean onDrag(int x, int y, int ptr) { return false; }
    @Override public boolean onTouchUp(int x, int y, int ptr, int btn) { return false; }
}
