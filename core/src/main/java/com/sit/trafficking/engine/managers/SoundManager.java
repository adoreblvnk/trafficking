package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.sit.trafficking.utils.Constants;

public final class SoundManager {
    private static SoundManager instance;
    private final AssetManager assetManager;
    private static final String CRASH_SOUND = "audio/car_crash_1.wav";

    private SoundManager() {
        this.assetManager = new AssetManager();
    }

    public static synchronized SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void queueAssets() {
        if (!assetManager.isLoaded(CRASH_SOUND)) {
            assetManager.load(CRASH_SOUND, Sound.class);
            Gdx.app.log("SoundManager", "Queued asset: " + CRASH_SOUND);
        }
    }

    public boolean updateLoading() {
        boolean finished = assetManager.update();
        if (finished) {
            // Gdx.app.log("SoundManager", "Assets finished loading.");
        }
        return finished;
    }

    public float getProgress() {
        return assetManager.getProgress();
    }

    public void playSound() {
        if (assetManager.isLoaded(CRASH_SOUND)) {
            Sound sound = assetManager.get(CRASH_SOUND, Sound.class);
            
            // Use simple volume from Constants (0-100 mapped to 0.0-1.0)
            float masterVol = Constants.VOLUME / 100f;
            
            if (masterVol > 0.01f) {
                sound.play(masterVol);
            }
        } else {
             Gdx.app.error("SoundManager", "Sound not loaded yet: " + CRASH_SOUND);
        }
    }

    public void dispose() {
        assetManager.dispose();
        instance = null;
    }
}
