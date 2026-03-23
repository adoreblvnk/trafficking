package com.sit.recyclingpinball.logic.scenes;

import com.sit.recyclingpinball.engine.scenes.AbstractScene;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.managers.*;
import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.logic.LogicConstants;
import com.sit.recyclingpinball.logic.ui.Button;

import java.util.ArrayList;
import java.util.List;

public class MenuScene extends AbstractScene implements InputListener {
    private final List<Button> buttons = new ArrayList<>();

    public MenuScene(IEngineContext context, SceneManager sceneManager,
            com.sit.recyclingpinball.logic.factories.AssemblyFactory assemblyFactory, EntityManager entityManager,
            CollisionManager collisionManager, InputManager inputManager, MovementManager movementManager) {
        super(context, entityManager, collisionManager, inputManager, movementManager);

        float titleBtnW = LogicConstants.UI_BTN_WIDTH_DEFAULT;
        float titleBtnH = LogicConstants.UI_BTN_HEIGHT_LARGE;
        float titleBtnX = LogicConstants.UI_CENTER_X - titleBtnW / 2;
        float titleBtnY = LogicConstants.UI_MENU_TITLE_Y;
        buttons.add(
                new Button(titleBtnX, titleBtnY, titleBtnW, titleBtnH, LogicConstants.TEXT_RECYCLING_PINBALL, null));

        float startBtnW = LogicConstants.UI_BTN_WIDTH_DEFAULT;
        float startBtnH = LogicConstants.UI_BTN_HEIGHT_DEFAULT;
        float startBtnX = LogicConstants.UI_CENTER_X - startBtnW / 2;
        float startBtnY = LogicConstants.UI_MENU_START_BTN_Y;
        buttons.add(new Button(startBtnX, startBtnY, startBtnW, startBtnH, LogicConstants.TEXT_START_GAME, () -> {
            sceneManager.setScene(assemblyFactory.createLevelSelectScene());
        }));

        float quitBtnW = LogicConstants.UI_BTN_WIDTH_DEFAULT;
        float quitBtnH = LogicConstants.UI_BTN_HEIGHT_DEFAULT;
        float quitBtnX = LogicConstants.UI_CENTER_X - quitBtnW / 2;
        float quitBtnY = LogicConstants.UI_MENU_QUIT_BTN_Y;
        buttons.add(new Button(quitBtnX, quitBtnY, quitBtnW, quitBtnH, LogicConstants.TEXT_QUIT, () -> {
            getContext().exit();
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
