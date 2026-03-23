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

import java.util.ArrayList;
import java.util.List;

public class PauseOverlay extends AbstractScene implements InputListener {
    private final SceneManager sceneManager;
    private final List<Button> buttons = new ArrayList<>();

    public PauseOverlay(IEngineContext context, SceneManager sceneManager) {
        super(context, new EntityManager(),
                new CollisionManager(
                        new com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle(0, 0, 1920, 1080)),
                new InputManager(), new MovementManager());
        this.sceneManager = sceneManager;

        float pauseBtnW = LogicConstants.UI_BTN_WIDTH_SMALL;
        float pauseBtnH = LogicConstants.UI_BTN_HEIGHT_LARGE;
        float pauseBtnX = LogicConstants.UI_CENTER_X - pauseBtnW / 2;
        float pauseBtnY = 580;
        buttons.add(new Button(pauseBtnX, pauseBtnY, pauseBtnW, pauseBtnH, LogicConstants.TEXT_PAUSED, null));

        float btnW = LogicConstants.UI_BTN_WIDTH_SMALL;
        float btnH = LogicConstants.UI_BTN_HEIGHT_DEFAULT;
        float btnX = LogicConstants.UI_CENTER_X - btnW / 2;

        buttons.add(new Button(btnX, 490, btnW, btnH, LogicConstants.TEXT_RESUME, () -> {
            sceneManager.popScene();
        }));
        buttons.add(new Button(btnX, 410, btnW, btnH, LogicConstants.TEXT_MAIN_MENU, () -> {
            sceneManager.setScene(new MenuScene(getContext(), sceneManager));
        }));
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
        // 60% opacity — dark enough to communicate "paused", light enough to see game
        // state.
        getContext().getGraphics().fillRectangle(0, 0, LogicConstants.SCENE_WIDTH, LogicConstants.SCENE_HEIGHT,
                LogicConstants.COLOR_DIM_R, LogicConstants.COLOR_DIM_G, LogicConstants.COLOR_DIM_B,
                LogicConstants.COLOR_DIM_PAUSED_A);

        for (Button button : buttons) {
            button.render(getContext());
        }

        getContext().getGraphics().end();
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
