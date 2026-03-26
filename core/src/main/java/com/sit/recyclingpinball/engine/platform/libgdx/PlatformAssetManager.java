package com.sit.recyclingpinball.engine.platform.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Owns platform-native asset lifecycle and lookup tables.
 *
 * This keeps caching/disposal out of IGraphics so rendering and resource
 * management remain separate responsibilities.
 */
public class PlatformAssetManager {

    private final Map<String, Texture> textures;
    private final Map<String, BitmapFont> fonts;

    public PlatformAssetManager() {
        this.textures = new HashMap<>();
        this.fonts = new HashMap<>();
    }

    public boolean loadTextureResource(String path) {
        return loadTextureResource(path, path);
    }

    public boolean loadTextureResource(String path, String id) {
        if (textures.containsKey(id)) {
            return true;
        }

        try {
            Texture texture = new Texture(Gdx.files.internal(path));
            textures.put(id, texture);
            return true;
        } catch (Exception e) {
            Gdx.app.error("PlatformAssetManager", "Failed to load texture: " + path, e);
            return false;
        }
    }

    public boolean loadFontResource(String path, int size) {
        if (fonts.containsKey(path)) {
            return true;
        }

        FreeTypeFontGenerator generator = null;
        try {
            generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = size;
            BitmapFont font = generator.generateFont(parameter);
            fonts.put(path, font);
            return true;
        } catch (Exception e) {
            Gdx.app.error("PlatformAssetManager", "Failed to load font: " + path, e);
            return false;
        } finally {
            if (generator != null) {
                generator.dispose();
            }
        }
    }

    public Texture getTextureResource(String id) {
        return textures.get(id);
    }

    public BitmapFont getFontResource(String id) {
        return fonts.get(id);
    }

    public void dispose() {
        textures.values().forEach(Texture::dispose);
        fonts.values().forEach(BitmapFont::dispose);
        textures.clear();
        fonts.clear();
    }
}
