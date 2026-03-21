package com.sit.recyclingpinball.logic.entities;

import com.sit.recyclingpinball.engine.entities.StaticEntity;

public class WallEntity extends StaticEntity {
    public WallEntity(String id, float x, float y, float w, float h) {
        // Since wall.png is missing from the texture folder, rendering walls
        // as a visible gray filled rectangle (which utilizes StaticEntity's base
        // render)
        super(id, x, y, w, h, 0.3f, 0.3f, 0.3f);
    }
}
