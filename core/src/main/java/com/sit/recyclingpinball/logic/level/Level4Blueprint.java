package com.sit.recyclingpinball.logic.level;

import com.sit.recyclingpinball.logic.factories.TrashType;

public class Level4Blueprint extends BaseLevelBlueprint {
    @Override
    protected void addCustomElements(BoardBuilder builder) {
        // Pachinko Grid (Pegs)
        int[] rowY = {800, 700, 600, 500, 400};
        int[] startX1 = {650, 850, 1050, 1250, 1450}; // for even rows
        int[] startX2 = {750, 950, 1150, 1350, 1550}; // for odd rows

        for (int i = 0; i < rowY.length; i++) {
            int[] xCoords = (i % 2 == 0) ? startX1 : startX2;
            for (int x : xCoords) {
                float angle = ((x + rowY[i]) % 100 == 0) ? 45f : -45f;
                builder.addSlantedWall(x, rowY[i], 30, 20, angle);
            }
        }

        // Thrash interspersed between pegs
        builder.addTrash(TrashType.PLASTIC, 850, 750).addTrash(TrashType.PAPER, 1050, 750)
                .addTrash(TrashType.GLASS, 1250, 750).addTrash(TrashType.PLASTIC, 750, 650)
                .addTrash(TrashType.PAPER, 950, 650).addTrash(TrashType.GLASS, 1150, 650)
                .addTrash(TrashType.PLASTIC, 850, 550).addTrash(TrashType.PAPER, 1050, 550)
                .addTrash(TrashType.GLASS, 1250, 550).addTrash(TrashType.PLASTIC, 950, 450);
    }

    @Override
    public String getLevelName() {
        return "Level 4: Pachinko Paradise";
    }

    @Override
    public String getText() {
        return "Chaotic bounces! May luck be on your side.\nKeep the earth clean!";
    }
}
