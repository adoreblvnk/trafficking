package com.sit.recyclingpinball.logic.ui;

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
import com.sit.recyclingpinball.logic.scenes.SimulationScene;
import com.sit.recyclingpinball.logic.level.ILevelBlueprint;

import java.util.ArrayList;
import java.util.List;

public class SimulationResultOverlay extends AbstractScene implements InputListener {
    private final boolean isWin;
    private final int score;
    private final int totalTrash;
    private final List<Button> buttons = new ArrayList<>();

    public SimulationResultOverlay(IEngineContext context, SceneManager sceneManager, boolean isWin, int score,
            int totalTrash, ILevelBlueprint blueprint) {
        super(context, new EntityManager(),
                new CollisionManager(
                        new com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle(0, 0, 1920, 1080)),
                new InputManager(), new MovementManager());
        this.isWin = isWin;
        this.score = score;
        this.totalTrash = totalTrash;

        String text = isWin ? LogicConstants.TEXT_YOU_WIN : LogicConstants.TEXT_GAME_OVER;
        float titleBtnW = LogicConstants.UI_BTN_WIDTH_SMALL;
        float titleBtnH = LogicConstants.UI_BTN_HEIGHT_LARGE;
        float titleBtnX = LogicConstants.UI_CENTER_X - titleBtnW / 2;
        float titleBtnY = 600 - titleBtnH / 2;
        buttons.add(new Button(titleBtnX, titleBtnY, titleBtnW, titleBtnH, text, null));

        float retBtnW = LogicConstants.UI_BTN_WIDTH_DEFAULT;
        float retBtnH = LogicConstants.UI_BTN_HEIGHT_DEFAULT;
        float retBtnX = LogicConstants.UI_CENTER_X - retBtnW / 2;
        float retBtnY = 370;
        buttons.add(new Button(retBtnX, retBtnY, retBtnW, retBtnH, LogicConstants.TEXT_MAIN_MENU, () -> {
            sceneManager.setScene(new MenuScene(getContext(), sceneManager));
        }));

        float retryBtnY = 280;
        buttons.add(new Button(retBtnX, retryBtnY, retBtnW, retBtnH, LogicConstants.TEXT_RETRY, () -> {
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, blueprint));
        }));
    }

    @Override
    public void create() {
        getInputManager().addListener(this);
        if (isWin) {
            getContext().getAudio().playSound(LogicConstants.SOUND_WIN, LogicConstants.VOLUME_DEFAULT);
        } else {
            getContext().getAudio().playSound(LogicConstants.SOUND_LOSE, LogicConstants.VOLUME_DEFAULT);
        }
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render() {
        getContext().getGraphics().begin();
        // Semi-transparent dark backdrop
        getContext().getGraphics().fillRectangle(0, 0, LogicConstants.SCENE_WIDTH, LogicConstants.SCENE_HEIGHT,
                LogicConstants.COLOR_DIM_R, LogicConstants.COLOR_DIM_G, LogicConstants.COLOR_DIM_B,
                LogicConstants.COLOR_DIM_OVERLAY_A);

        for (Button button : buttons) {
            button.render(getContext());
        }

        // Star icons showing collected trash
        float starsStartX = LogicConstants.UI_CENTER_X - (totalTrash * 70) / 2.0f;
        float starsY = 480;
        for (int i = 0; i < totalTrash; i++) {
            float starX = starsStartX + i * 70;
            if (i < score) {
                getContext().getGraphics().drawTexture(LogicConstants.TEX_STAR, starX, starsY, 64, 60);
            } else {
                getContext().getGraphics().fillRectangle(starX + 16, starsY + 14, 32, 32, 0.3f, 0.3f, 0.3f, 0.4f);
            }
        }

        // Score text (white on dark overlay — no button behind this)
        getContext().getGraphics().setTextColor(LogicConstants.COLOR_TEXT_LIGHT_R, LogicConstants.COLOR_TEXT_LIGHT_G,
                LogicConstants.COLOR_TEXT_LIGHT_B, LogicConstants.COLOR_TEXT_A);
        // We'll leave the score text non-centered or we could center it if we calculate
        // width.
        getContext().getGraphics()
                .drawText(
                        LogicConstants.TEXT_TRASH_COLLECTED_PREFIX + score + LogicConstants.TEXT_TRASH_DIVIDER
                                + totalTrash + LogicConstants.TEXT_TRASH_COLLECTED_SUFFIX,
                        LogicConstants.FONT_GEIST_BOLD, 850, 470);

        getContext().getGraphics().end();
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
