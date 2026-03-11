package com.sit.recyclingpinball.logic.entities;
import com.sit.recyclingpinball.logic.factories.TrashType;

public class PaperTrash extends TrashEntity {
    public PaperTrash(String id, float x, float y) {
        super(id, x, y, TrashType.PAPER, "trash_paper", 1);
    }
}
