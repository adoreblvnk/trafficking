package com.sit.recyclingpinball.logic.entities;

import com.sit.recyclingpinball.engine.entities.StaticEntity;

public class WallEntity extends StaticEntity {
    public WallEntity(String id, float x, float y, float w, float h) {
        super(id, x, y, w, h, 0.3f, 0.3f, 0.3f);
    }
}
