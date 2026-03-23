package com.sit.recyclingpinball.engine.managers;

import com.sit.recyclingpinball.engine.interfaces.providers.IAudioProvider;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized manager for loading and tracking game assets. Ensures assets are
 * loaded once at startup.
 */
public class AssetManager {

    private static AssetManager instance;
    private IAudioProvider audioProvider;
    private final Map<String, String> loadedSounds;

    private AssetManager() {
        loadedSounds = new HashMap<>();
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
     * Initializes the AssetManager with the given IAudioProvider.
     *
     * @param provider
     *            the audio provider used to load sounds
     */
    public void initialize(IAudioProvider provider) {
        if (provider == null) {
            throw new IllegalArgumentException("IAudioProvider cannot be null");
        }
        this.audioProvider = provider;
    }

    public void loadSound(String path, String id) {
        audioProvider.loadSound(path, id);
        loadedSounds.put(id, path);
    }

}
