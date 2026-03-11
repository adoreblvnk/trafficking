package com.sit.recyclingpinball.logic.entities;
import com.sit.recyclingpinball.logic.factories.TrashType;

public class PlasticTrash extends TrashEntity {
    public PlasticTrash(String id, float x, float y) {
        super(id, x, y, TrashType.PLASTIC, "trash_plastic", 1);
    }
}
