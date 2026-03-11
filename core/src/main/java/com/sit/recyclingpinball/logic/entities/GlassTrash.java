package com.sit.recyclingpinball.logic.entities;
import com.sit.recyclingpinball.logic.factories.TrashType;

public class GlassTrash extends TrashEntity {
    public GlassTrash(String id, float x, float y) {
        super(id, x, y, TrashType.GLASS, "trash_glass", 1);
    }
}
