package com.sit.recyclingpinball.engine.managers;

import com.sit.recyclingpinball.engine.platform.libgdx.PlatformAudio;
import com.sit.recyclingpinball.engine.platform.libgdx.PlatformAssetManager;

/**
 * ARCHITECTURE JUSTIFICATION: Separation of API and Lifecycle.
 *
 * <p>This class delegates entirely to PlatformAssetManager. The Engine Core
 * exposes a unified, framework-agnostic API to the Logic layer, while the
 * Platform layer strictly owns memory lifecycle and disposal of native LibGDX
 * assets (SRP compliance).</p>
 */
public class AssetManager implements com.sit.recyclingpinball.engine.interfaces.providers.IAssetProvider {

    private PlatformAudio audioProvider;
    private PlatformAssetManager assetProvider;

    public AssetManager() {
    }

    /**
     * Initializes the AssetManager with the required providers.
     */
    public void initialize(PlatformAudio audioProvider, PlatformAssetManager assetProvider) {
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
     * Loads a texture and stores it in the platform cache using a caller-provided ID.
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
