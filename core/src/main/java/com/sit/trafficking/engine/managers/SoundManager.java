package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
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

    public void playSound(float impactVelocity) {
        // Ignore tiny jitters
        if (impactVelocity < 1.0f) return;

        if (assetManager.isLoaded(CRASH_SOUND)) {
            Sound sound = assetManager.get(CRASH_SOUND, Sound.class);
            
            // Map velocity (0 to 20) to Pitch (0.8f to 1.5f)
            float pitch = MathUtils.map(0f, 20f, 0.8f, 1.5f, impactVelocity);
            pitch = MathUtils.clamp(pitch, 0.5f, 2.0f); // Safety clamp

            // Map velocity (0 to 20) to Volume (0.2f to 1.0f)
            // Apply Master Volume (0-100 -> 0.0-1.0)
            float masterVol = Constants.VOLUME / 100f;
            float volume = MathUtils.map(0f, 20f, 0.2f, 1.0f, impactVelocity);
            volume = MathUtils.clamp(volume, 0f, 1f) * masterVol;

            if (volume > 0.01f) {
                sound.play(volume, pitch, 0);
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
