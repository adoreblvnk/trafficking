package com.sit.recyclingpinball.engine.managers;

import com.sit.recyclingpinball.engine.interfaces.providers.IAudioProvider;
import com.sit.recyclingpinball.engine.EngineConstants;
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

    /**
     * Loads all required game assets.
     */
    public void loadAll() {
        if (audioProvider == null) {
            throw new IllegalStateException(
                    "AssetManager must be initialized with an IAudioProvider before loading assets");
        }

        loadSound(EngineConstants.SOUNDS_DIR + EngineConstants.SOUND_CLICK + EngineConstants.SOUND_EXTENSION,
                EngineConstants.SOUND_CLICK);
        loadSound(EngineConstants.SOUNDS_DIR + EngineConstants.SOUND_COLLECT + EngineConstants.SOUND_EXTENSION,
                EngineConstants.SOUND_COLLECT);
        loadSound(EngineConstants.SOUNDS_DIR + EngineConstants.SOUND_LOSE + EngineConstants.SOUND_EXTENSION,
                EngineConstants.SOUND_LOSE);
        loadSound(EngineConstants.SOUNDS_DIR + EngineConstants.SOUND_WIN + EngineConstants.SOUND_EXTENSION,
                EngineConstants.SOUND_WIN);
        loadSound(EngineConstants.SOUNDS_DIR + EngineConstants.SOUND_BOUNCE + EngineConstants.SOUND_EXTENSION,
                EngineConstants.SOUND_BOUNCE);
        loadSound(EngineConstants.SOUNDS_DIR + EngineConstants.SOUND_FLIP + EngineConstants.SOUND_EXTENSION,
                EngineConstants.SOUND_FLIP);
        loadSound(EngineConstants.SOUNDS_DIR + EngineConstants.SOUND_STRETCH + EngineConstants.SOUND_EXTENSION,
                EngineConstants.SOUND_STRETCH);
    }

    private void loadSound(String path, String id) {
        audioProvider.loadSound(path, id);
        loadedSounds.put(id, path);
    }

    /**
     * Retrieves the path of a loaded sound by its ID.
     *
     * @param id
     *            the sound identifier
     * @return the file path of the sound, or null if not loaded
     */
    public String getSound(String id) {
        return loadedSounds.get(id);
    }
}
