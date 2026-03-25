package com.sit.recyclingpinball.engine.platform.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.sit.recyclingpinball.engine.interfaces.providers.IAudioProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * libGDX implementation of IAudioProvider. Manages sound loading and playback
 * with lazy loading and centralized control.
 */
public class LibGdxAudio implements IAudioProvider {

    private final Map<String, Sound> soundBank;

    public LibGdxAudio() {
        this.soundBank = new HashMap<>();
    }

    @Override
    public boolean loadSound(String path, String name) {
        if (name == null || name.isEmpty()) {
            Gdx.app.log("LibGdxAudio", "Cannot load sound with null/empty name (ignored)");
            return false;
        }
        if (path == null || path.isEmpty()) {
            Gdx.app.log("LibGdxAudio", "Cannot load sound with null/empty path (ignored)");
            return false;
        }

        if (soundBank.containsKey(name)) {
            return true; // Already loaded
        }

        try {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
            soundBank.put(name, sound);
            return true;
        } catch (Exception e) {
            Gdx.app.error("LibGdxAudio", "Failed to load sound: " + path, e);
            return false;
        }
    }

    @Override
    public void playSound(String name, float volume) {
        if (name == null || name.isEmpty()) {
            Gdx.app.log("LibGdxAudio", "Cannot play sound with null/empty name (ignored)");
            return;
        }

        Sound sound = soundBank.get(name);
        if (sound == null) {
            Gdx.app.log("LibGdxAudio", "Sound not found: " + name);
            return;
        }

        float clampedVolume = Math.max(0f, Math.min(1f, volume));
        if (clampedVolume != volume) {
            Gdx.app.log("LibGdxAudio", "Volume clamped to [0,1]: " + volume + " -> " + clampedVolume);
        }

        try {
            sound.play(clampedVolume);
        } catch (Exception e) {
            Gdx.app.error("LibGdxAudio", "Audio playback failed for: " + name, e);
        }
    }

    @Override
    public void dispose() {
        soundBank.values().forEach(Sound::dispose);
        soundBank.clear();
    }
}
