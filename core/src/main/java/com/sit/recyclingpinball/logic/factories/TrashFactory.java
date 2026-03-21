package com.sit.recyclingpinball.logic.factories;

import com.sit.recyclingpinball.logic.entities.TrashEntity;

public final class TrashFactory {
    private TrashFactory() {}
    
    public static TrashEntity createTrash(TrashType type, float x, float y) {
        String id = com.sit.recyclingpinball.logic.LogicConstants.ID_TRASH_PREFIX + System.nanoTime();
        switch (type) {
            case PLASTIC: return new TrashEntity(id, x, y, TrashType.PLASTIC, com.sit.recyclingpinball.logic.LogicConstants.ID_TRASH_PLASTIC, 1);
            case PAPER: return new TrashEntity(id, x, y, TrashType.PAPER, com.sit.recyclingpinball.logic.LogicConstants.ID_TRASH_PAPER, 1);
            case GLASS: return new TrashEntity(id, x, y, TrashType.GLASS, com.sit.recyclingpinball.logic.LogicConstants.ID_TRASH_GLASS, 1);
            default: throw new IllegalArgumentException("Unknown trash type");
        }
    }
}
