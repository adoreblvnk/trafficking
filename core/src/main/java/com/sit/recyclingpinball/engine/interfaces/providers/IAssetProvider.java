package com.sit.recyclingpinball.engine.interfaces.providers;

/**
 * Platform-independent asset provider interface for retrieving cached assets.
 */
public interface IAssetProvider {
    Object getTexture(String id);
    Object getFont(String id);
}
