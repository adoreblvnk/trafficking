package com.sit.trafficking.engine.managers;

import com.sit.trafficking.engine.interfaces.providers.IAudioProvider;

/**
 * Game-level sound manager wrapper.
 * Delegates all audio operations to the injected IAudioProvider.
 * No longer directly depends on libGDX.
 */
public class SoundManager {

    private final IAudioProvider audioProvider;

    public SoundManager(IAudioProvider audioProvider) {
        if (audioProvider == null) {
            throw new IllegalArgumentException("AudioProvider cannot be null");
        }
        this.audioProvider = audioProvider;
    }

    /**
     * Loads a sound from the given path and stores it under the given name.
     *
     * @param id the identifier for the sound
     * @param path the file path to the sound resource
     * @return true if loaded successfully, false otherwise
     */
    public boolean loadSound(String id, String path) {
        if (id == null || id.isEmpty()) {
            return false;
        }
        if (path == null || path.isEmpty()) {
            return false;
        }
        return audioProvider.loadSound(path, id);
    }

    /**
     * Plays a previously loaded sound by its identifier.
     *
     * @param id the identifier of the sound to play
     * @param volume the volume to play at (0.0 to 1.0)
     */
    public void playSound(String id, float volume) {
        if (id == null || id.isEmpty()) {
            return;
        }
        audioProvider.playSound(id, volume);
    }

    /**
     * Disposes all audio resources.
     */
    public void dispose() {
        audioProvider.dispose();
    }
}
