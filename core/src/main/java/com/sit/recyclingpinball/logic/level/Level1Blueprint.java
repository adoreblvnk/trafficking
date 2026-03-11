package com.sit.recyclingpinball.logic.level;

import com.sit.recyclingpinball.logic.factories.TrashType;

public class Level1Blueprint implements ILevelBlueprint {
    @Override
    public BoardLayout construct(BoardBuilder builder) {
        // play area x=400 to 1900, y=0 to 1000
        builder.addWall(400, 0, 50, 1000) // left wall
               .addWall(1850, 0, 50, 1000) // right wall
               .addWall(400, 950, 1500, 50); // top wall
               
        // Launch Tube
        builder.addWall(1750, 0, 15, 800);
               
        // Bottom Funnels
        builder.addSlantedWall(555, 167, 300, 15, -15f); // left funnel
        builder.addSlantedWall(1335, 180, 430, 15, 15f); // right funnel
        
        // Top Curve
        builder.addSlantedWall(1700, 890, 200, 15, 135f);

        builder.addLeftFlipper(850, 100)
               .addRightFlipper(1300, 100);
               
        builder.addTrash(TrashType.PLASTIC, 800, 600)
               .addTrash(TrashType.PAPER, 1200, 700)
               .addTrash(TrashType.GLASS, 1000, 800);
               
        return builder.build();
    }

    @Override
    public String getLevelName() { return "Level 1: The Basics"; }

    @Override
    public String getEducationalText() { return "Keep the earth clean!\nRecycle plastic, paper, and glass."; }
}
