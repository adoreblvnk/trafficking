package com.sit.recyclingpinball.engine.interfaces.providers;

/**
 * Platform-independent asset provider interface for retrieving cached assets.
 *
 * Design note: asset values are exposed as opaque {@code Object} handles to
 * keep engine/logic layers decoupled from concrete rendering frameworks. This
 * avoids importing libGDX classes into core modules while still allowing the
 * platform renderer to resolve concrete types at the boundary. In this
 * architecture, logic code passes stable asset IDs (for example via
 * SpriteComponent), while framework-specific casts are intentionally isolated
 * in the platform graphics implementation.
 */
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
