package com.sit.recyclingpinball.logic.ui;

import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.logic.LogicConstants;

public class Button {
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final String text;
    private final Runnable action;

    public Button(float x, float y, float width, float height, String text, Runnable action) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.action = action;
    }

    public void render(IEngineContext context) {
        context.getGraphics().setTextColor(LogicConstants.COLOR_TEXT_DARK_R, LogicConstants.COLOR_TEXT_DARK_G,
                LogicConstants.COLOR_TEXT_DARK_B, LogicConstants.COLOR_TEXT_A);

        context.getGraphics().drawTexture(LogicConstants.TEX_BUTTON_RECT_DEPTH_FLAT, x, y, width, height);
        context.getGraphics().drawTextCentered(text, LogicConstants.FONT_GEIST_BOLD, x, y, width, height);

        context.getGraphics().setTextColor(LogicConstants.COLOR_TEXT_LIGHT_R, LogicConstants.COLOR_TEXT_LIGHT_G,
                LogicConstants.COLOR_TEXT_LIGHT_B, LogicConstants.COLOR_TEXT_A);
    }

    public boolean handleTouch(int screenX, int screenY, IEngineContext context) {
        if (action == null) {
            return false;
        }
        float mappedY = context.getDisplay().getHeight() - screenY;
        if (screenX >= x && screenX <= x + width && mappedY >= y && mappedY <= y + height) {
            context.getAudio().playSound(LogicConstants.SOUND_CLICK, LogicConstants.VOLUME_DEFAULT);
            action.run();
            return true;
        }
        return false;
    }
}
