package com.sit.recyclingpinball.logic.level;

import com.sit.recyclingpinball.logic.factories.TrashType;

public class Level2Blueprint extends BaseLevelBlueprint {
    @Override
    protected void addCustomElements(BoardBuilder builder) {
        builder.addSlantedWall(900, 400, 200, 15, 15f); // obstacle wall
        builder.addSlantedWall(1200, 600, 200, 15, -25f); // obstacle wall

        builder.addTrash(TrashType.PLASTIC, 700, 500).addTrash(TrashType.PAPER, 1400, 500)
                .addTrash(TrashType.GLASS, 1000, 900).addTrash(TrashType.PLASTIC, 1200, 800);
    }

    @Override
    public String getLevelName() {
        return "Level 2: Trash Obstacles";
    }

    @Override
    public String getText() {
        return "Keep the earth clean!\nSorting trash saves wildlife.";
    }
}
