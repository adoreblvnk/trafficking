package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    
    private static SoundManager instance;
    private final AssetManager assetManager;
    private final Map<String, String> soundBank;
    private float volume = 1.0f;

    private SoundManager() {
        assetManager = new AssetManager();
        soundBank = new HashMap<>();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void loadSound(String id, String internalPath) {
        if (!soundBank.containsKey(id)) {
            soundBank.put(id, internalPath);
        }
        if (!assetManager.isLoaded(internalPath)) {
            assetManager.load(internalPath, Sound.class);
        }
    }

    public boolean update() {
        return assetManager.update();
    }

    public float getProgress() {
        return assetManager.getProgress();
    }

    public void setVolume(float volume) {
        this.volume = Math.max(0f, Math.min(1f, volume));
    }

    public float getVolume() {
        return volume;
    }

    public void playSound(String id, float pitch, float pan) {
        String path = soundBank.get(id);
        if (path == null || !assetManager.isLoaded(path)) {
            return;
        }

        Sound sound = assetManager.get(path, Sound.class);
        long soundId = sound.play(volume, pitch, pan);
        if (soundId == -1) {
            return;
        }
    }
    
    public void dispose() {
        assetManager.dispose();
    }
}
