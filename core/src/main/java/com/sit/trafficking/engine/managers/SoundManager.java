package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages audio resource lifecycle with lazy loading and centralized playback control.
 */
public class SoundManager {

    private final Map<String, Sound> soundBank;

    public SoundManager() {
        this.soundBank = new HashMap<>();
    }

    public boolean loadSound(String id, String path) {
        if (id == null || id.isEmpty()) {
            Gdx.app.log("SoundManager", "Cannot load sound with null/empty ID (ignored)");
            return false;
        }
        if (path == null || path.isEmpty()) {
            Gdx.app.log("SoundManager", "Cannot load sound with null/empty path (ignored)");
            return false;
        }

        if (soundBank.containsKey(id)) {
            return true; // Already loaded
        }

        try {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
            soundBank.put(id, sound);
            return true;
        } catch (Exception e) {
            Gdx.app.error("SoundManager", "Failed to load sound: " + path, e);
            return false;
        }
    }

    public void playSound(String id, float volume) {
        if (id == null || id.isEmpty()) {
            Gdx.app.log("SoundManager", "Cannot play sound with null/empty ID (ignored)");
            return;
        }

        Sound sound = soundBank.get(id);
        if (sound == null) {
            Gdx.app.log("SoundManager", "Sound not found: " + id);
            return;
        }

        float clampedVolume = MathUtils.clamp(volume, 0f, 1f);
        if (clampedVolume != volume) {
            Gdx.app.log("SoundManager", "Volume clamped to [0,1]: " + volume + " -> " + clampedVolume);
        }

        try {
            sound.play(clampedVolume);
        } catch (Exception e) {
            Gdx.app.error("SoundManager", "Audio playback failed for: " + id, e);
        }
    }

    public void dispose() {
        for (Sound sound : soundBank.values()) {
            sound.dispose();
        }
        soundBank.clear();
    }
}
