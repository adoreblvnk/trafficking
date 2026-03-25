package com.sit.recyclingpinball.logic.factories;

import com.sit.recyclingpinball.logic.entities.TrashEntity;
import com.sit.recyclingpinball.logic.events.PinballEventBus;

public final class TrashFactory {
    private TrashFactory() {
    }

    public static TrashEntity createTrash(TrashType type, float x, float y, PinballEventBus eventBus) {
        String id = com.sit.recyclingpinball.logic.LogicConstants.ID_TRASH_PREFIX + System.nanoTime();
        return switch (type) {
            case PLASTIC -> new TrashEntity(id, x, y, com.sit.recyclingpinball.logic.LogicConstants.TEX_TRASH_PLASTIC, eventBus);
            case PAPER -> new TrashEntity(id, x, y, com.sit.recyclingpinball.logic.LogicConstants.TEX_TRASH_PAPER, eventBus);
            case GLASS -> new TrashEntity(id, x, y, com.sit.recyclingpinball.logic.LogicConstants.TEX_TRASH_GLASS, eventBus);
        };
    }
}
