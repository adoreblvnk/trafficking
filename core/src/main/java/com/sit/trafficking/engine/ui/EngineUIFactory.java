package com.sit.trafficking.engine.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Factory for creating UI styles programmatically without JSON skins.
 */
public class EngineUIFactory {

    private static BitmapFont font;

    /**
     * @return A standard TextButtonStyle with generated background textures.
     */
    public static TextButtonStyle getButtonStyle() {
        TextButtonStyle style = new TextButtonStyle();
        style.font = getFont();
        
        // Generate Drawable for Up state (Gray)
        style.up = getDrawable(Color.GRAY);
        // Generate Drawable for Down/Over state (Dark Gray)
        style.down = getDrawable(Color.DARK_GRAY);
        style.over = getDrawable(Color.LIGHT_GRAY);
        
        return style;
    }

    /**
     * @return A standard LabelStyle.
     */
    public static LabelStyle getLabelStyle() {
        LabelStyle style = new LabelStyle();
        style.font = getFont();
        style.fontColor = Color.WHITE;
        return style;
    }

    private static TextureRegionDrawable getDrawable(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(texture));
    }

    private static BitmapFont getFont() {
        if (font == null) {
            try {
                if (Gdx.files.internal("fonts/Geist-Regular.ttf").exists()) {
                    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Geist-Regular.ttf"));
                    FreeTypeFontParameter parameter = new FreeTypeFontParameter();
                    parameter.size = 18;
                    parameter.color = Color.WHITE;
                    font = generator.generateFont(parameter);
                    generator.dispose();
                } else {
                    font = new BitmapFont();
                }
            } catch (Exception e) {
                Gdx.app.error("EngineUIFactory", "Failed to load TTF font", e);
                font = new BitmapFont();
            }
        }
        return font;
    }
}
