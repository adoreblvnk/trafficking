package com.sit.recyclingpinball.engine.managers;

import com.sit.recyclingpinball.engine.interfaces.providers.IAudioProvider;
import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized manager for loading and tracking game assets (Sounds, Textures, Fonts).
 * Ensures assets are loaded once and shared across the engine (Flyweight Pattern).
 */
public class AssetManager {

    private static AssetManager instance;
    private IAudioProvider audioProvider;
    private IGraphicsProvider graphicsProvider;
    
    private final Map<String, Object> loadedSounds = new HashMap<>();
    private final Map<String, Object> loadedTextures = new HashMap<>();
    private final Map<String, Object> loadedFonts = new HashMap<>();

    private AssetManager() {
    }

    /**
     * Gets the singleton instance of the AssetManager.
     *
     * @return the singleton instance
     */
    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
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
     * Loads a sound and stores it by ID.
     */
    public void loadSound(String path, String id) {
        audioProvider.loadSound(path, id);
    }

    /**
     * Loads a texture and stores it by ID.
     */
    public void loadTexture(String path, String id) {
        Object texture = graphicsProvider.loadTextureResource(path);
        if (texture != null) {
            loadedTextures.put(id, texture);
        }
    }

    /**
     * Loads a font and stores it by ID.
     */
    public void loadFont(String path, int size, String id) {
        Object font = graphicsProvider.loadFontResource(path, size);
        if (font != null) {
            loadedFonts.put(id, font);
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
        // Implementation will call provider disposals
        loadedTextures.clear();
        loadedFonts.clear();
        loadedSounds.clear();
    }
}
