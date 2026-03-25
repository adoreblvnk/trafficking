package com.sit.recyclingpinball.logic.ui;

import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.interfaces.IEntityManager;
import com.sit.recyclingpinball.engine.interfaces.ICollisionManager;
import com.sit.recyclingpinball.engine.interfaces.IInputManager;
import com.sit.recyclingpinball.engine.interfaces.IMovementManager;
import com.sit.recyclingpinball.engine.scenes.AbstractScene;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.logic.LogicConstants;
import com.sit.recyclingpinball.logic.factories.SceneFactory;
import com.sit.recyclingpinball.logic.level.ILevelBlueprint;

import java.util.ArrayList;
import java.util.List;

public class SimulationResultOverlay extends AbstractScene implements InputListener {
    private final boolean isWin;
    private final int score;
    private final int totalTrash;
    private final List<Button> buttons = new ArrayList<>();

    public SimulationResultOverlay(IEngineContext context, SceneManager sceneManager, SceneFactory sceneFactory,
            boolean isWin, int score, int totalTrash, ILevelBlueprint blueprint, IEntityManager entityManager,
            ICollisionManager collisionManager, IInputManager inputManager, IMovementManager movementManager) {
        super(context, entityManager, collisionManager, inputManager, movementManager);
        this.isWin = isWin;
        this.score = score;
        this.totalTrash = totalTrash;

        String text = isWin ? LogicConstants.TEXT_YOU_WIN : LogicConstants.TEXT_GAME_OVER;
        float titleBtnW = LogicConstants.UI_BTN_WIDTH_SMALL;
        float titleBtnH = LogicConstants.UI_BTN_HEIGHT_LARGE;
        float titleBtnX = LogicConstants.UI_RESULT_TITLE_POS[0] - titleBtnW / 2;
        float titleBtnY = LogicConstants.UI_RESULT_TITLE_POS[1] - titleBtnH / 2;
        buttons.add(new Button(titleBtnX, titleBtnY, titleBtnW, titleBtnH, text, null));

        float retBtnW = LogicConstants.UI_BTN_WIDTH_DEFAULT;
        float retBtnH = LogicConstants.UI_BTN_HEIGHT_DEFAULT;
        float retBtnX = LogicConstants.UI_RESULT_MENU_BTN_POS[0] - retBtnW / 2;
        float retBtnY = LogicConstants.UI_RESULT_MENU_BTN_POS[1];
        buttons.add(new Button(retBtnX, retBtnY, retBtnW, retBtnH, LogicConstants.TEXT_MAIN_MENU, () -> {
            sceneManager.setScene(sceneFactory.createMenuScene());
        }));

        float retryBtnY = LogicConstants.UI_RESULT_RETRY_BTN_POS[1];
        buttons.add(new Button(retBtnX, retryBtnY, retBtnW, retBtnH, LogicConstants.TEXT_RETRY, () -> {
            sceneManager.setScene(sceneFactory.createSimulationScene(blueprint));
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
        // Semi-transparent dark backdrop
        getContext().getGraphics().fillRectangle(0, 0, LogicConstants.SCENE_SIZE[0], LogicConstants.SCENE_SIZE[1],
                LogicConstants.COLOR_DIM[0], LogicConstants.COLOR_DIM[1], LogicConstants.COLOR_DIM[2],
                LogicConstants.COLOR_DIM_OVERLAY_A);

        for (Button button : buttons) {
            button.render(getContext());
        }

        // Star icons showing collected trash
        float starsStartX = LogicConstants.UI_RESULT_STARS_POS[0]
                - (totalTrash * LogicConstants.UI_STAR_SPACING) / 2.0f;
        float starsY = LogicConstants.UI_RESULT_STARS_POS[1];
        for (int i = 0; i < totalTrash; i++) {
            float starX = starsStartX + i * LogicConstants.UI_STAR_SPACING;
            if (i < score) {
                getContext().getGraphics().drawTexture(LogicConstants.TEX_STAR, starX, starsY, 64, 60);
            } else {
                getContext().getGraphics().fillRectangle(starX + 16, starsY + 14, 32, 32, 0.3f, 0.3f, 0.3f, 0.4f);
            }
        }

        // Score text (white on dark overlay — no button behind this)
        getContext().getGraphics().setTextColor(LogicConstants.COLOR_TEXT_LIGHT[0], LogicConstants.COLOR_TEXT_LIGHT[1],
                LogicConstants.COLOR_TEXT_LIGHT[2], LogicConstants.COLOR_TEXT_LIGHT[3]);
        getContext().getGraphics().drawText(
                LogicConstants.TEXT_TRASH_COLLECTED_PREFIX + score + LogicConstants.TEXT_TRASH_DIVIDER + totalTrash,
                LogicConstants.FONT_GEIST_BOLD, LogicConstants.UI_RESULT_SCORE_POS[0],
                LogicConstants.UI_RESULT_SCORE_POS[1]);
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
