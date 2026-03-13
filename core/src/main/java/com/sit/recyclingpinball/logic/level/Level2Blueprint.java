package com.sit.recyclingpinball.logic.level;

import com.sit.recyclingpinball.logic.events.PinballEventBus;
import com.sit.recyclingpinball.logic.factories.TrashType;

public class Level2Blueprint implements ILevelBlueprint {
    @Override
    public BoardLayout construct(BoardBuilder builder, PinballEventBus eventBus) {
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

        builder.addSlantedWall(900, 400, 200, 15, 15f); // obstacle wall
        builder.addSlantedWall(1200, 600, 200, 15, -25f); // obstacle wall

        builder.addLeftFlipper(850, 100)
               .addRightFlipper(1300, 100);
               
        builder.addTrash(TrashType.PLASTIC, 700, 500)
               .addTrash(TrashType.PAPER, 1400, 500)
               .addTrash(TrashType.GLASS, 1000, 900)
               .addTrash(TrashType.PLASTIC, 1200, 800);
               
        builder.setShooterRod(1760, 100, eventBus);
        return builder.build();
    }

    @Override
    public String getLevelName() { return "Level 2: Trash Obstacles"; }

    @Override
    public String getEducationalText() { return "Keep the earth clean!\nSorting trash saves wildlife."; }
}
