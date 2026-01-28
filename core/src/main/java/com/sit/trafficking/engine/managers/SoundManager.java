package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
    
    private static SoundManager instance;
    private AssetManager assetManager;
    private static final String HIT_SOUND = "sounds/car_crash_1.wav";
    private float volume = 1.0f;

    private SoundManager() {
        assetManager = new AssetManager();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void load() {
        // Debug Check: Does the file actually exist?
        if (!Gdx.files.internal(HIT_SOUND).exists()) {
            Gdx.app.error("SoundManager", "CRITICAL ERROR: Sound file NOT FOUND at: " + HIT_SOUND);
            Gdx.app.error("SoundManager", "Please check your 'assets' folder structure.");
            return;
        }

        assetManager.load(HIT_SOUND, Sound.class);
        Gdx.app.log("SoundManager", "Queued sound for loading: " + HIT_SOUND);
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

    public void playImpact(float intensity) {
        if (assetManager.isLoaded(HIT_SOUND)) {
            // Check cooldown
            if (intensity < 20f) return;

            Sound sound = assetManager.get(HIT_SOUND, Sound.class);
            
            // no pitch or pan, WSL does not support it
            long id = sound.play(volume);
            
            if (id == -1) {
                Gdx.app.error("SoundManager", "Sound Failed (ID -1)");
            }
        }
    }
    
    public void dispose() {
        assetManager.dispose();
    }
}