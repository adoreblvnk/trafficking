package com.sit.recyclingpinball.logic.level;

import com.sit.recyclingpinball.logic.events.PinballEventBus;
import com.sit.recyclingpinball.logic.factories.TrashType;

public class Level3Blueprint implements ILevelBlueprint {
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
              builder.addSlantedWall(1330, 180, 430, 15, 15f); // right funnel

              // Top Curve
              builder.addSlantedWall(1700, 890, 190, 15, 135f);

              builder.addLeftFlipper(850, 100)
                            .addRightFlipper(1300, 100);

              // New Obstacles: Blocks to bounce off
              builder.addWall(800, 300, 60, 60)
                     .addWall(1200, 300, 60, 60);

              // Additional slanted walls near the center
              builder.addSlantedWall(700, 500, 150, 15, 45f)
                     .addSlantedWall(1400, 500, 150, 15, -45f);

              // Bumper block near the top
              builder.addWall(1000, 750, 100, 100);

              // Add lots of trash to collect
              builder.addTrash(TrashType.PLASTIC, 850, 650)
                     .addTrash(TrashType.PLASTIC, 1250, 650)
                     .addTrash(TrashType.PLASTIC, 1050, 550)
                     .addTrash(TrashType.PAPER, 700, 400)
                     .addTrash(TrashType.PAPER, 1400, 400)
                     .addTrash(TrashType.PAPER, 1050, 880)
                     .addTrash(TrashType.GLASS, 1050, 400)
                     .addTrash(TrashType.GLASS, 900, 800)
                     .addTrash(TrashType.GLASS, 1200, 800);

              builder.setShooterRod(1775, 100, eventBus);
              return builder.build();
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
