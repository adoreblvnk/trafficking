package com.sit.recyclingpinball.logic.scenes;

import com.sit.recyclingpinball.engine.scenes.AbstractScene;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.engine.interfaces.IEntityManager;
import com.sit.recyclingpinball.engine.interfaces.ICollisionManager;
import com.sit.recyclingpinball.engine.interfaces.IInputManager;
import com.sit.recyclingpinball.engine.interfaces.IMovementManager;
import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.logic.LogicConstants;
import com.sit.recyclingpinball.logic.factories.SceneFactory;
import com.sit.recyclingpinball.logic.ui.Button;

import java.util.ArrayList;
import java.util.List;

public class MenuScene extends AbstractScene implements InputListener {
    private final List<Button> buttons = new ArrayList<>();

    public MenuScene(SceneManager sceneManager, SceneFactory sceneFactory, IEntityManager entityManager,
            ICollisionManager collisionManager, IInputManager inputManager, IMovementManager movementManager) {
        super(sceneManager, entityManager, collisionManager, inputManager, movementManager);

        float titleBtnW = LogicConstants.UI_BTN_WIDTH_DEFAULT;
        float titleBtnH = LogicConstants.UI_BTN_HEIGHT_LARGE;
        float titleBtnX = LogicConstants.UI_MENU_TITLE_POS[0] - titleBtnW / 2;
        float titleBtnY = LogicConstants.UI_MENU_TITLE_POS[1];
        buttons.add(
                new Button(titleBtnX, titleBtnY, titleBtnW, titleBtnH, LogicConstants.TEXT_RECYCLING_PINBALL, null));

        float startBtnW = LogicConstants.UI_BTN_WIDTH_DEFAULT;
        float startBtnH = LogicConstants.UI_BTN_HEIGHT_DEFAULT;
        float startBtnX = LogicConstants.UI_MENU_START_BTN_POS[0] - startBtnW / 2;
        float startBtnY = LogicConstants.UI_MENU_START_BTN_POS[1];
        buttons.add(new Button(startBtnX, startBtnY, startBtnW, startBtnH, LogicConstants.TEXT_START_GAME, () -> {
            sceneManager.setScene(sceneFactory.createLevelSelectScene());
        }));

        float quitBtnW = LogicConstants.UI_BTN_WIDTH_DEFAULT;
        float quitBtnH = LogicConstants.UI_BTN_HEIGHT_DEFAULT;
        float quitBtnX = LogicConstants.UI_MENU_QUIT_BTN_POS[0] - quitBtnW / 2;
        float quitBtnY = LogicConstants.UI_MENU_QUIT_BTN_POS[1];
        buttons.add(new Button(quitBtnX, quitBtnY, quitBtnW, quitBtnH, LogicConstants.TEXT_QUIT, () -> {
            getGraphics().dispose();
            System.exit(0);
        }));
    }

    @Override
    public void create() {
        getInputManager().addListener(this);
    }

    @Override
    public void render() {
        getGraphics().clearScreen(LogicConstants.COLOR_BG[0], LogicConstants.COLOR_BG[1], LogicConstants.COLOR_BG[2]);

        // Full-screen dirty beach background
        getGraphics().drawTexture(LogicConstants.TEX_DIRTY_BEACH, 0, 0, LogicConstants.SCENE_SIZE[0],
                LogicConstants.SCENE_SIZE[1]);

        for (Button button : buttons) {
            button.render(getGraphics());
        }

        super.render();
    }

    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) {
        for (Button button : buttons) {
            if (button.handleTouch(x, y, getAudio()))
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
