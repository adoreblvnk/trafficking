package com.sit.recyclingpinball.engine.components;

public record SpriteComponent(String textureId, float width, float height) {

    public SpriteComponent {
        if (textureId == null || textureId.isBlank()) {
            throw new IllegalArgumentException("textureId cannot be null or blank");
        }
        if (width <= 0f || height <= 0f) {
            throw new IllegalArgumentException("Sprite dimensions must be positive");
        }
    }
}
