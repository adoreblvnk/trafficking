package com.sit.recyclingpinball.engine.interfaces.providers;

/**
 * Platform-independent asset provider interface for retrieving cached assets.
 *
 */
// Exposes assets as opaque Object handles to decouple logic from concrete LibGDX types.
public interface IAssetProvider {

    /**
     * Returns an opaque handle for a texture associated with the given ID.
     */
    Object getTexture(String id);

    /**
     * Returns an opaque handle for a font associated with the given ID.
     */
    Object getFont(String id);
}
