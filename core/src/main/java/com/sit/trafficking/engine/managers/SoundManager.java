package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    
    private final Map<String, Sound> soundBank;

    public SoundManager() {
        this.soundBank = new HashMap<>();
    }

    public void loadSound(String id, String path) {
        if (!soundBank.containsKey(id)) {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
            soundBank.put(id, sound);
        }
    }

    public void playSound(String id, float volume) {
        Sound sound = soundBank.get(id);
        if (sound != null) {
            try {
                sound.play(volume);
            } catch (Exception e) {
                Gdx.app.error("SoundManager", "Audio playback failed", e);
            }
        }
    }

    public void dispose() {
        for (Sound sound : soundBank.values()) {
            sound.dispose();
        }
        soundBank.clear();
    }
}
