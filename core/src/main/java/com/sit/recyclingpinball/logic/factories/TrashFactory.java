package com.sit.recyclingpinball.logic.factories;

import com.sit.recyclingpinball.logic.entities.TrashEntity;

public final class TrashFactory {
    private TrashFactory() {}
    
    public static TrashEntity createTrash(TrashType type, float x, float y) {
        String id = "trash_" + System.nanoTime();
        switch (type) {
            case PLASTIC: return new TrashEntity(id, x, y, TrashType.PLASTIC, "trash_plastic", 1);
            case PAPER: return new TrashEntity(id, x, y, TrashType.PAPER, "trash_paper", 1);
            case GLASS: return new TrashEntity(id, x, y, TrashType.GLASS, "trash_glass", 1);
            default: throw new IllegalArgumentException("Unknown trash type");
        }
    }
}
