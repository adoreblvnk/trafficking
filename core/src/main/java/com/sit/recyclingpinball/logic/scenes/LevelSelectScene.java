package com.sit.recyclingpinball.logic.scenes;

import com.sit.recyclingpinball.engine.interfaces.providers.EngineKey;

import com.sit.recyclingpinball.engine.scenes.AbstractScene;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.managers.*;
import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.logic.LogicConstants;
import com.sit.recyclingpinball.logic.level.Level1Blueprint;
import com.sit.recyclingpinball.logic.level.Level2Blueprint;
import com.sit.recyclingpinball.logic.level.Level3Blueprint;
import com.sit.recyclingpinball.logic.level.Level4Blueprint;
import com.sit.recyclingpinball.logic.level.Level5Blueprint;

public class LevelSelectScene extends AbstractScene implements InputListener {
    private final SceneManager sceneManager;

    public LevelSelectScene(IEngineContext context, SceneManager sceneManager) {
        super(context, new EntityManager(), new CollisionManager(new com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle(0, 0, 1920, 1080)), new InputManager(), new MovementManager());
        this.sceneManager = sceneManager;
    }

    @Override
    public void create() {
        getInputManager().addListener(this);
    }

    @Override
    public void render() {
        getContext().getGraphics().clearScreen(LogicConstants.COLOR_BG_R, LogicConstants.COLOR_BG_G, LogicConstants.COLOR_BG_B);
        getContext().getGraphics().begin();

        // Full-screen dirty beach background
        getContext().getGraphics().drawTexture(LogicConstants.TEX_DIRTY_BEACH, 0, 0, LogicConstants.SCENE_WIDTH, LogicConstants.SCENE_HEIGHT);

        // Dark text for button labels
        getContext().getGraphics().setTextColor(LogicConstants.COLOR_TEXT_DARK_R, LogicConstants.COLOR_TEXT_DARK_G, LogicConstants.COLOR_TEXT_DARK_B, LogicConstants.COLOR_TEXT_A);

        // Title with button background
        float titleBtnW = LogicConstants.UI_BTN_WIDTH_SMALL;
        float titleBtnH = LogicConstants.UI_BTN_HEIGHT_LARGE;
        float titleBtnX = LogicConstants.UI_CENTER_X - titleBtnW / 2;
        float titleBtnY = 670;
        getContext().getGraphics().drawTexture(LogicConstants.TEX_BUTTON_RECT_DEPTH_FLAT, titleBtnX, titleBtnY, titleBtnW, titleBtnH);
        getContext().getGraphics().drawTextCentered(LogicConstants.TEXT_LEVEL_SELECT, LogicConstants.FONT_GEIST_BOLD, titleBtnX, titleBtnY, titleBtnW, titleBtnH);

        // Level 1 button
        float btnW = LogicConstants.UI_BTN_WIDTH_DEFAULT;
        float btnH = LogicConstants.UI_BTN_HEIGHT_DEFAULT;
        float btnX = LogicConstants.UI_CENTER_X - btnW / 2;
        getContext().getGraphics().drawTexture(LogicConstants.TEX_BUTTON_RECT_DEPTH_FLAT, btnX, 570, btnW, btnH);
        getContext().getGraphics().drawTextCentered(LogicConstants.TEXT_LEVEL_1, LogicConstants.FONT_GEIST_BOLD, btnX, 570, btnW, btnH);

        // Level 2 button
        getContext().getGraphics().drawTexture(LogicConstants.TEX_BUTTON_RECT_DEPTH_FLAT, btnX, 480, btnW, btnH);
        getContext().getGraphics().drawTextCentered(LogicConstants.TEXT_LEVEL_2, LogicConstants.FONT_GEIST_BOLD, btnX, 480, btnW, btnH);

        // Level 3 button
        getContext().getGraphics().drawTexture(LogicConstants.TEX_BUTTON_RECT_DEPTH_FLAT, btnX, 390, btnW, btnH);
        getContext().getGraphics().drawTextCentered(LogicConstants.TEXT_LEVEL_3, LogicConstants.FONT_GEIST_BOLD, btnX, 390, btnW, btnH);

        // Level 4 button
        getContext().getGraphics().drawTexture(LogicConstants.TEX_BUTTON_RECT_DEPTH_FLAT, btnX, 300, btnW, btnH);
        getContext().getGraphics().drawTextCentered(LogicConstants.TEXT_LEVEL_4, LogicConstants.FONT_GEIST_BOLD, btnX, 300, btnW, btnH);

        // Level 5 button
        getContext().getGraphics().drawTexture(LogicConstants.TEX_BUTTON_RECT_DEPTH_FLAT, btnX, 210, btnW, btnH);
        getContext().getGraphics().drawTextCentered(LogicConstants.TEXT_LEVEL_5, LogicConstants.FONT_GEIST_BOLD, btnX, 210, btnW, btnH);

        // Back button
        getContext().getGraphics().drawTexture(LogicConstants.TEX_BUTTON_RECT_DEPTH_FLAT, btnX, 120, btnW, btnH);
        getContext().getGraphics().drawTextCentered(LogicConstants.TEXT_BACK, LogicConstants.FONT_GEIST_BOLD, btnX, 120, btnW, btnH);

        // Reset text color to white
        getContext().getGraphics().setTextColor(LogicConstants.COLOR_TEXT_LIGHT_R, LogicConstants.COLOR_TEXT_LIGHT_G, LogicConstants.COLOR_TEXT_LIGHT_B, LogicConstants.COLOR_TEXT_A);

        getContext().getGraphics().end();
        super.render();
    }

    private boolean isClicked(int screenX, int screenY, float btnX, float btnY, float btnW, float btnH) {
        float mappedY = getContext().getDisplay().getHeight() - screenY;
        return screenX >= btnX && screenX <= btnX + btnW && mappedY >= btnY && mappedY <= btnY + btnH;
    }

    @Override
    public boolean onKeyDown(EngineKey keycode) {
        if (keycode == EngineKey.NUM_1) {
            getContext().getAudio().playSound(LogicConstants.SOUND_CLICK, LogicConstants.VOLUME_DEFAULT);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, new Level1Blueprint()));
            return true;
        } else if (keycode == EngineKey.NUM_2) {
            getContext().getAudio().playSound(LogicConstants.SOUND_CLICK, LogicConstants.VOLUME_DEFAULT);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, new Level2Blueprint()));
            return true;
        } else if (keycode == EngineKey.NUM_3) {
            getContext().getAudio().playSound(LogicConstants.SOUND_CLICK, LogicConstants.VOLUME_DEFAULT);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, new Level3Blueprint()));
            return true;
        } else if (keycode == EngineKey.NUM_4) {
            getContext().getAudio().playSound(LogicConstants.SOUND_CLICK, LogicConstants.VOLUME_DEFAULT);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, new Level4Blueprint()));
            return true;
        } else if (keycode == EngineKey.NUM_5) {
            getContext().getAudio().playSound(LogicConstants.SOUND_CLICK, LogicConstants.VOLUME_DEFAULT);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, new Level5Blueprint()));
            return true;
        } else if (keycode == EngineKey.ESCAPE) {
            getContext().getAudio().playSound(LogicConstants.SOUND_CLICK, LogicConstants.VOLUME_DEFAULT);
            sceneManager.setScene(new MenuScene(getContext(), sceneManager));
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) {
        float btnW = LogicConstants.UI_BTN_WIDTH_DEFAULT;
        float btnH = LogicConstants.UI_BTN_HEIGHT_DEFAULT;
        float btnX = LogicConstants.UI_CENTER_X - btnW / 2;

        if (isClicked(x, y, btnX, 570, btnW, btnH)) {
            getContext().getAudio().playSound(LogicConstants.SOUND_CLICK, LogicConstants.VOLUME_DEFAULT);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, new Level1Blueprint()));
            return true;
        }
        if (isClicked(x, y, btnX, 480, btnW, btnH)) {
            getContext().getAudio().playSound(LogicConstants.SOUND_CLICK, LogicConstants.VOLUME_DEFAULT);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, new Level2Blueprint()));
            return true;
        }
        if (isClicked(x, y, btnX, 390, btnW, btnH)) {
            getContext().getAudio().playSound(LogicConstants.SOUND_CLICK, LogicConstants.VOLUME_DEFAULT);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, new Level3Blueprint()));
            return true;
        }
        if (isClicked(x, y, btnX, 300, btnW, btnH)) {
            getContext().getAudio().playSound(LogicConstants.SOUND_CLICK, LogicConstants.VOLUME_DEFAULT);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, new Level4Blueprint()));
            return true;
        }
        if (isClicked(x, y, btnX, 210, btnW, btnH)) {
            getContext().getAudio().playSound(LogicConstants.SOUND_CLICK, LogicConstants.VOLUME_DEFAULT);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, new Level5Blueprint()));
            return true;
        }
        if (isClicked(x, y, btnX, 120, btnW, btnH)) {
            getContext().getAudio().playSound(LogicConstants.SOUND_CLICK, LogicConstants.VOLUME_DEFAULT);
            sceneManager.setScene(new MenuScene(getContext(), sceneManager));
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
