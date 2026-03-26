package com.sit.recyclingpinball.engine.managers;

import com.sit.recyclingpinball.engine.platform.libgdx.PlatformAudio;
import com.sit.recyclingpinball.engine.platform.libgdx.PlatformAssetManager;

/**
 */
// Delegates to PlatformAssetManager to separate the engine API from platform
// memory lifecycle.
public class AssetManager implements com.sit.recyclingpinball.engine.interfaces.providers.IAssetProvider {

    private final PlatformAudio audioProvider;
    private final PlatformAssetManager assetProvider;

    /**
     * Initializes the AssetManager with the required providers via constructor
     * injection.
     */
    public AssetManager(PlatformAudio audioProvider, PlatformAssetManager assetProvider) {
        if (audioProvider == null || assetProvider == null) {
            throw new IllegalArgumentException("Providers cannot be null");
        }
        this.audioProvider = audioProvider;
        this.assetProvider = assetProvider;
    }

    /**
     * Loads a sound and stores it in the internal bank.
     */
    public void loadSound(String path) {
        audioProvider.loadSound(path, path);
    }

    /**
     * Loads a texture and stores it using its path as the ID.
     */
    public void loadTexture(String path) {
        loadTexture(path, path);
    }

    /**
     * Loads a texture and stores it in the platform cache using a caller-provided
     * ID.
     */
    public void loadTexture(String path, String id) {
        assetProvider.loadTextureResource(path, id);
    }

    /**
     * Loads a font and stores it using its path as the ID.
     */
    public void loadFont(String path, int size) {
        assetProvider.loadFontResource(path, size);
    }

    public Object getTexture(String id) {
        return assetProvider == null ? null : assetProvider.getTextureResource(id);
    }

    public Object getFont(String id) {
        return assetProvider == null ? null : assetProvider.getFontResource(id);
    }

    /**
     * Disposes of all managed assets to prevent memory leaks.
     */
    public void dispose() {
        // No-op: PlatformContext owns provider disposal lifecycle.
    }
}
