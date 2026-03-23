package com.sit.recyclingpinball.logic.scenes;

import com.sit.recyclingpinball.engine.interfaces.providers.EngineKey;
import com.sit.recyclingpinball.engine.scenes.AbstractScene;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.managers.*;
import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.logic.LogicConstants;
import com.sit.recyclingpinball.logic.level.DataDrivenLevelBlueprint;
import com.sit.recyclingpinball.logic.ui.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LevelSelectScene extends AbstractScene implements InputListener {
    private final SceneManager sceneManager;
    private final List<Button> buttons = new ArrayList<>();

    public LevelSelectScene(IEngineContext context, SceneManager sceneManager) {
        super(context, new EntityManager(),
                new CollisionManager(
                        new com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle(0, 0, 1920, 1080)),
                new InputManager(), new MovementManager());
        this.sceneManager = sceneManager;

        float titleBtnW = LogicConstants.UI_BTN_WIDTH_SMALL;
        float titleBtnH = LogicConstants.UI_BTN_HEIGHT_LARGE;
        float titleBtnX = LogicConstants.UI_CENTER_X - titleBtnW / 2;
        float titleBtnY = 670;
        buttons.add(new Button(titleBtnX, titleBtnY, titleBtnW, titleBtnH, LogicConstants.TEXT_LEVEL_SELECT, null));

        float btnW = LogicConstants.UI_BTN_WIDTH_DEFAULT;
        float btnH = LogicConstants.UI_BTN_HEIGHT_DEFAULT;
        float btnX = LogicConstants.UI_CENTER_X - btnW / 2;

        java.util.List<String> levelFiles = getContext().getIO().listInternalFiles(LogicConstants.DIR_LEVELS, ".json");
        levelFiles.remove(LogicConstants.PATH_BASE_LEVEL);
        Collections.sort(levelFiles);

        for (int i = 0; i < levelFiles.size(); i++) {
            String path = levelFiles.get(i);
            DataDrivenLevelBlueprint bp = new DataDrivenLevelBlueprint(path, getContext());
            float btnY = 570 - (i * 90);
            buttons.add(new Button(btnX, btnY, btnW, btnH, bp.getLevelName(), () -> {
                sceneManager.setScene(new SimulationScene(getContext(), sceneManager,
                        new DataDrivenLevelBlueprint(path, getContext())));
            }));
        }

        float backBtnY = 570 - (levelFiles.size() * 90);
        buttons.add(new Button(btnX, backBtnY, btnW, btnH, LogicConstants.TEXT_BACK, () -> {
            sceneManager.setScene(new MenuScene(getContext(), sceneManager));
        }));
    }

    @Override
    public void create() {
        getInputManager().addListener(this);
    }

    @Override
    public void render() {
        getContext().getGraphics().clearScreen(LogicConstants.COLOR_BG_R, LogicConstants.COLOR_BG_G,
                LogicConstants.COLOR_BG_B);
        getContext().getGraphics().begin();

        // Full-screen dirty beach background
        getContext().getGraphics().drawTexture(LogicConstants.TEX_DIRTY_BEACH, 0, 0, LogicConstants.SCENE_WIDTH,
                LogicConstants.SCENE_HEIGHT);

        for (Button button : buttons) {
            button.render(getContext());
        }

        getContext().getGraphics().end();
        super.render();
    }

    @Override
    public boolean onKeyDown(EngineKey keycode) {
        if (keycode == EngineKey.ESCAPE) {
            getContext().getAudio().playSound(LogicConstants.SOUND_CLICK, LogicConstants.VOLUME_DEFAULT);
            sceneManager.setScene(new MenuScene(getContext(), sceneManager));
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) {
        for (Button button : buttons) {
            if (button.handleTouch(x, y, getContext()))
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
