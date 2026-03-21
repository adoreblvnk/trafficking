package com.sit.recyclingpinball.logic.level;

import com.sit.recyclingpinball.logic.factories.TrashType;

public class Level3Blueprint extends BaseLevelBlueprint {
    @Override
    protected void addCustomElements(BoardBuilder builder) {
        // New Obstacles: Blocks to bounce off
        builder.addWall(800, 300, 60, 60).addWall(1200, 300, 60, 60);

        // Additional slanted walls near the center
        builder.addSlantedWall(700, 500, 150, 15, 45f).addSlantedWall(1400, 500, 150, 15, -45f);

        // Bumper block near the top
        builder.addWall(1000, 750, 100, 100);

        // Add lots of trash to collect
        builder.addTrash(TrashType.PLASTIC, 850, 650).addTrash(TrashType.PLASTIC, 1250, 650)
                .addTrash(TrashType.PLASTIC, 1050, 550).addTrash(TrashType.PAPER, 700, 400)
                .addTrash(TrashType.PAPER, 1400, 400).addTrash(TrashType.PAPER, 1050, 880)
                .addTrash(TrashType.GLASS, 1050, 400).addTrash(TrashType.GLASS, 900, 800)
                .addTrash(TrashType.GLASS, 1200, 800);
    }

    @Override
    public String getLevelName() {
        return "Level 3: Advanced Recycling";
    }

    @Override
    public String getText() {
        return "More obstacles, more trash!\nKeep the earth clean!";
    }
}
