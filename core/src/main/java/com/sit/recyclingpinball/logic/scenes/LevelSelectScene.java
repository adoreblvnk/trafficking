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
    private final com.sit.recyclingpinball.logic.factories.AssemblyFactory assemblyFactory;

    public LevelSelectScene(IEngineContext context, SceneManager sceneManager,
            com.sit.recyclingpinball.logic.factories.AssemblyFactory assemblyFactory, EntityManager entityManager,
            CollisionManager collisionManager, InputManager inputManager, MovementManager movementManager) {
        super(context, entityManager, collisionManager, inputManager, movementManager);
        this.sceneManager = sceneManager;
        this.assemblyFactory = assemblyFactory;

        float titleBtnW = LogicConstants.UI_BTN_WIDTH_SMALL;
        float titleBtnH = LogicConstants.UI_BTN_HEIGHT_LARGE;
        float titleBtnX = LogicConstants.UI_CENTER_X - titleBtnW / 2;
        float titleBtnY = LogicConstants.UI_LEVEL_SELECT_TITLE_Y;
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
            float btnY = LogicConstants.UI_LEVEL_SELECT_BTN_START_Y - (i * LogicConstants.UI_LEVEL_SELECT_BTN_SPACING);
            buttons.add(new Button(btnX, btnY, btnW, btnH, bp.getLevelName(), () -> {
                sceneManager.setScene(
                        this.assemblyFactory.createSimulationScene(new DataDrivenLevelBlueprint(path, getContext())));
            }));
        }

        float backBtnY = LogicConstants.UI_LEVEL_SELECT_BTN_START_Y
                - (levelFiles.size() * LogicConstants.UI_LEVEL_SELECT_BTN_SPACING);
        buttons.add(new Button(btnX, backBtnY, btnW, btnH, LogicConstants.TEXT_BACK, () -> {
            sceneManager.setScene(this.assemblyFactory.createMenuScene());
        }));
    }

    @Override
    public void create() {
        getInputManager().addListener(this);
    }

    @Override
    public void render() {
        getContext().getGraphics().clearScreen(LogicConstants.COLOR_BG[0], LogicConstants.COLOR_BG[1],
                LogicConstants.COLOR_BG[2]);
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
            sceneManager.setScene(this.assemblyFactory.createMenuScene());
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
