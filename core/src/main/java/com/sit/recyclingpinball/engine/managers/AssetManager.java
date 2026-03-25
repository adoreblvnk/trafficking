package com.sit.recyclingpinball.engine.managers;

import com.sit.recyclingpinball.engine.interfaces.providers.IAudioProvider;
import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized manager for loading and tracking game assets (Sounds, Textures,
 * Fonts). Ensures assets are loaded once and shared across the engine
 * (Flyweight Pattern).
 */
public class AssetManager implements com.sit.recyclingpinball.engine.interfaces.providers.IAssetProvider {

    private IAudioProvider audioProvider;
    private IGraphicsProvider graphicsProvider;

    private final Map<String, Object> loadedTextures = new HashMap<>();
    private final Map<String, Object> loadedFonts = new HashMap<>();

    public AssetManager() {
    }

    /**
     * Initializes the AssetManager with the required providers.
     */
    public void initialize(IAudioProvider audioProvider, IGraphicsProvider graphicsProvider) {
        if (audioProvider == null || graphicsProvider == null) {
            throw new IllegalArgumentException("Providers cannot be null");
        }
        this.audioProvider = audioProvider;
        this.graphicsProvider = graphicsProvider;
    }

    /**
     * Loads a sound and stores it in the internal bank.
     */
    public void loadSound(String path) {
        audioProvider.loadSound(path, path);
    }

    /**
     * Loads a texture and stores it using its path as the ID.
     */
    public void loadTexture(String path) {
        Object texture = graphicsProvider.loadTextureResource(path);
        if (texture != null) {
            loadedTextures.put(path, texture);
        }
    }

    /**
     * Loads a font and stores it using its path as the ID.
     */
    public void loadFont(String path, int size) {
        Object font = graphicsProvider.loadFontResource(path, size);
        if (font != null) {
            loadedFonts.put(path, font);
        }
    }

    public Object getTexture(String id) {
        return loadedTextures.get(id);
    }

    public Object getFont(String id) {
        return loadedFonts.get(id);
    }

    /**
     * Disposes of all managed assets to prevent memory leaks.
     */
    public void dispose() {
        if (graphicsProvider != null) {
            loadedTextures.values().forEach(graphicsProvider::disposeTextureResource);
            loadedFonts.values().forEach(graphicsProvider::disposeFontResource);
        }
        loadedTextures.clear();
        loadedFonts.clear();
    }
}
