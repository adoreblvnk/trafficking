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

import java.util.ArrayList;
import java.util.List;

public class PauseOverlay extends AbstractScene implements InputListener {
    private final SceneManager sceneManager;
    private final List<Button> buttons = new ArrayList<>();
    private final com.sit.recyclingpinball.logic.factories.AssemblyFactory assemblyFactory;

    public PauseOverlay(IEngineContext context, SceneManager sceneManager,
            com.sit.recyclingpinball.logic.factories.AssemblyFactory assemblyFactory, EntityManager entityManager,
            CollisionManager collisionManager, InputManager inputManager, MovementManager movementManager) {
        super(context, entityManager, collisionManager, inputManager, movementManager);
        this.sceneManager = sceneManager;
        this.assemblyFactory = assemblyFactory;

        float pauseBtnW = LogicConstants.UI_BTN_WIDTH_SMALL;
        float pauseBtnH = LogicConstants.UI_BTN_HEIGHT_LARGE;
        float pauseBtnX = LogicConstants.UI_PAUSE_TITLE_POS[0] - pauseBtnW / 2;
        float pauseBtnY = LogicConstants.UI_PAUSE_TITLE_POS[1];
        buttons.add(new Button(pauseBtnX, pauseBtnY, pauseBtnW, pauseBtnH, LogicConstants.TEXT_PAUSED, null));

        float btnW = LogicConstants.UI_BTN_WIDTH_SMALL;
        float btnH = LogicConstants.UI_BTN_HEIGHT_DEFAULT;
        float btnX = LogicConstants.UI_PAUSE_RESUME_BTN_POS[0] - btnW / 2;

        buttons.add(new Button(btnX, LogicConstants.UI_PAUSE_RESUME_BTN_POS[1], btnW, btnH, LogicConstants.TEXT_RESUME,
                () -> {
                    sceneManager.popScene();
                }));
        buttons.add(new Button(btnX, LogicConstants.UI_PAUSE_MENU_BTN_POS[1], btnW, btnH, LogicConstants.TEXT_MAIN_MENU,
                () -> {
                    sceneManager.setScene(assemblyFactory.createMenuScene());
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
        getContext().getGraphics().fillRectangle(0, 0, LogicConstants.SCENE_SIZE[0], LogicConstants.SCENE_SIZE[1],
                LogicConstants.COLOR_DIM[0], LogicConstants.COLOR_DIM[1], LogicConstants.COLOR_DIM[2],
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
            sceneManager.setScene(assemblyFactory.createMenuScene());
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
