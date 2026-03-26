package com.sit.recyclingpinball.logic.ui;

import com.sit.recyclingpinball.engine.platform.libgdx.PlatformGraphics;
import com.sit.recyclingpinball.engine.platform.libgdx.PlatformAudio;
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

    // Passes specific platform providers instead of the full context to keep
    // coupling low.
    public void render(PlatformGraphics graphics) {
        graphics.setTextColor(LogicConstants.COLOR_TEXT_DARK[0], LogicConstants.COLOR_TEXT_DARK[1],
                LogicConstants.COLOR_TEXT_DARK[2], LogicConstants.COLOR_TEXT_DARK[3]);

        graphics.drawTexture(LogicConstants.TEX_BUTTON_RECT_DEPTH_FLAT, x, y, width, height);
        graphics.drawTextCentered(text, LogicConstants.FONT_GEIST_BOLD, x, y, width, height);

        graphics.setTextColor(LogicConstants.COLOR_TEXT_LIGHT[0], LogicConstants.COLOR_TEXT_LIGHT[1],
                LogicConstants.COLOR_TEXT_LIGHT[2], LogicConstants.COLOR_TEXT_LIGHT[3]);
    }

    public boolean handleTouch(int screenX, int screenY, PlatformAudio audio) {
        if (action == null) {
            return false;
        }
        if (screenX >= x && screenX <= x + width && screenY >= y && screenY <= y + height) {
            audio.playSound(LogicConstants.SOUND_CLICK, LogicConstants.VOLUME_DEFAULT);
            action.run();
            return true;
        }
        return false;
    }
}
