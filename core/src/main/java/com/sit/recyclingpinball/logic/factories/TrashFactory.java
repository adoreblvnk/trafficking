package com.sit.recyclingpinball.logic.factories;

import com.sit.recyclingpinball.logic.entities.GlassTrash;
import com.sit.recyclingpinball.logic.entities.PaperTrash;
import com.sit.recyclingpinball.logic.entities.PlasticTrash;
import com.sit.recyclingpinball.logic.entities.TrashEntity;

public final class TrashFactory {
    private TrashFactory() {}
    
    public static TrashEntity createTrash(TrashType type, float x, float y) {
        String id = "trash_" + System.nanoTime();
        switch (type) {
            case PLASTIC: return new PlasticTrash(id, x, y);
            case PAPER: return new PaperTrash(id, x, y);
            case GLASS: return new GlassTrash(id, x, y);
            default: throw new IllegalArgumentException("Unknown trash type");
        }
    }
}
