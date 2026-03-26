package com.sit.recyclingpinball.engine.platform.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class PlatformAudio {

    private final Map<String, Sound> soundBank;

    public PlatformAudio() {
        this.soundBank = new HashMap<>();
    }

    public boolean loadSound(String path, String name) {
        if (name == null || name.isEmpty()) {
            Gdx.app.log("PlatformAudio", "Cannot load sound with null/empty name (ignored)");
            return false;
        }
        if (path == null || path.isEmpty()) {
            Gdx.app.log("PlatformAudio", "Cannot load sound with null/empty path (ignored)");
            return false;
        }

        if (soundBank.containsKey(name)) {
            return true;
        }

        try {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
            soundBank.put(name, sound);
            return true;
        } catch (Exception e) {
            Gdx.app.error("PlatformAudio", "Failed to load sound: " + path, e);
            return false;
        }
    }

    public void playSound(String name, float volume) {
        if (name == null || name.isEmpty()) {
            Gdx.app.log("PlatformAudio", "Cannot play sound with null/empty name (ignored)");
            return;
        }

        Sound sound = soundBank.get(name);
        if (sound == null) {
            Gdx.app.log("PlatformAudio", "Sound not found: " + name);
            return;
        }

        float clampedVolume = Math.max(0f, Math.min(1f, volume));
        if (clampedVolume != volume) {
            Gdx.app.log("PlatformAudio", "Volume clamped to [0,1]: " + volume + " -> " + clampedVolume);
        }

        try {
            sound.play(clampedVolume);
        } catch (Exception e) {
            Gdx.app.error("PlatformAudio", "Audio playback failed for: " + name, e);
        }
    }

    public void dispose() {
        soundBank.values().forEach(Sound::dispose);
        soundBank.clear();
    }
}
