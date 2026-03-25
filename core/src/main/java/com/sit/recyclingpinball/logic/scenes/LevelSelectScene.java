package com.sit.recyclingpinball.logic.scenes;

import com.sit.recyclingpinball.engine.interfaces.providers.EngineKey;
import com.sit.recyclingpinball.engine.scenes.AbstractScene;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.interfaces.IEntityManager;
import com.sit.recyclingpinball.engine.interfaces.ICollisionManager;
import com.sit.recyclingpinball.engine.interfaces.IInputManager;
import com.sit.recyclingpinball.engine.interfaces.IMovementManager;
import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.logic.LogicConstants;
import com.sit.recyclingpinball.logic.level.DataDrivenLevelBlueprint;
import com.sit.recyclingpinball.logic.factories.SceneFactory;
import com.sit.recyclingpinball.logic.ui.Button;

import java.util.ArrayList;
import java.util.List;

public class LevelSelectScene extends AbstractScene implements InputListener {
    private final SceneManager sceneManager;
    private final List<Button> buttons = new ArrayList<>();
    private final SceneFactory sceneFactory;

    public LevelSelectScene(IEngineContext context, SceneManager sceneManager, SceneFactory sceneFactory,
            IEntityManager entityManager, ICollisionManager collisionManager, IInputManager inputManager,
            IMovementManager movementManager) {
        super(context, entityManager, collisionManager, inputManager, movementManager);
        this.sceneManager = sceneManager;
        this.sceneFactory = sceneFactory;

        float titleBtnW = LogicConstants.UI_BTN_WIDTH_SMALL;
        float titleBtnH = LogicConstants.UI_BTN_HEIGHT_LARGE;
        float titleBtnX = LogicConstants.UI_LEVEL_SELECT_TITLE_POS[0] - titleBtnW / 2;
        float titleBtnY = LogicConstants.UI_LEVEL_SELECT_TITLE_POS[1];
        buttons.add(new Button(titleBtnX, titleBtnY, titleBtnW, titleBtnH, LogicConstants.TEXT_LEVEL_SELECT, null));

        float btnW = LogicConstants.UI_BTN_WIDTH_DEFAULT;
        float btnH = LogicConstants.UI_BTN_HEIGHT_DEFAULT;
        float btnX = LogicConstants.UI_LEVEL_SELECT_BTN_START_POS[0] - btnW / 2;

        java.util.List<String> levelFiles = getContext().getIO().listInternalFiles(LogicConstants.DIR_LEVELS, ".json");
        levelFiles.remove(LogicConstants.PATH_BASE_LEVEL);
        levelFiles.sort(java.util.Comparator.naturalOrder());

        for (int i = 0; i < levelFiles.size(); i++) {
            String path = levelFiles.get(i);
            DataDrivenLevelBlueprint bp = new DataDrivenLevelBlueprint(path, getContext());
            float btnY = LogicConstants.UI_LEVEL_SELECT_BTN_START_POS[1]
                    - (i * LogicConstants.UI_LEVEL_SELECT_BTN_SPACING);
            buttons.add(new Button(btnX, btnY, btnW, btnH, bp.getLevelName(), () -> {
                sceneManager.setScene(
                        this.sceneFactory.createSimulationScene(new DataDrivenLevelBlueprint(path, getContext())));
            }));
        }

        float backBtnY = LogicConstants.UI_LEVEL_SELECT_BTN_START_POS[1]
                - (levelFiles.size() * LogicConstants.UI_LEVEL_SELECT_BTN_SPACING);
        buttons.add(new Button(btnX, backBtnY, btnW, btnH, LogicConstants.TEXT_BACK, () -> {
            sceneManager.setScene(this.sceneFactory.createMenuScene());
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

        // Full-screen dirty beach background
        getContext().getGraphics().drawTexture(LogicConstants.TEX_DIRTY_BEACH, 0, 0, LogicConstants.SCENE_SIZE[0],
                LogicConstants.SCENE_SIZE[1]);

        for (Button button : buttons) {
            button.render(getContext());
        }

        super.render();
    }

    @Override
    public boolean onKeyDown(EngineKey keycode) {
        if (keycode == EngineKey.ESCAPE) {
            getContext().getAudio().playSound(LogicConstants.SOUND_CLICK, LogicConstants.VOLUME_DEFAULT);
            sceneManager.setScene(this.sceneFactory.createMenuScene());
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
