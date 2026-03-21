package com.sit.recyclingpinball.logic.level;

import com.sit.recyclingpinball.logic.events.PinballEventBus;
import com.sit.recyclingpinball.logic.factories.TrashType;

public class Level5Blueprint implements ILevelBlueprint {
       @Override
       public BoardLayout construct(BoardBuilder builder, PinballEventBus eventBus) {

              builder.addWall(400, 0, 50, 1000) // left wall
                            .addWall(1850, 0, 50, 1000) // right wall
                            .addWall(400, 950, 1500, 50); // top wall

              // Launch Tube
              builder.addWall(1750, 0, 15, 800);

              // Top Curve
              builder.addSlantedWall(1700, 890, 190, 15, 135f);

              /* --- TIER 3 (BASE) --- */
              // Funnels
              builder.addSlantedWall(555, 167, 300, 15, -15f); // left funnel
              builder.addSlantedWall(1330, 180, 430, 15, 15f); // right funnel
              // Flippers
              builder.addLeftFlipper(850, 100)
                            .addRightFlipper(1300, 100);

              /* --- TIER 2 (MIDDLE) --- */
              // Funnels
              builder.addSlantedWall(555, 467, 300, 15, -15f); // middle left funnel
              builder.addSlantedWall(1380, 480, 300, 15, 15f); // middle right funnel

              /* --- TIER 1 (TOP) --- */
              // Funnels (Top right is extra wide to safely catch ball from launch curve)
              builder.addSlantedWall(555, 767, 300, 15, -15f); // top left funnel
              builder.addSlantedWall(1450, 780, 300, 15, 15f); // top right funnel

              // Trash Layout
              // Tier 1 Trash
              builder.addTrash(TrashType.PLASTIC, 950, 850)
                            .addTrash(TrashType.PAPER, 1075, 850)
                            .addTrash(TrashType.GLASS, 1200, 850);

              // Tier 2 Trash
              builder.addTrash(TrashType.GLASS, 900, 550)
                            .addTrash(TrashType.PLASTIC, 1075, 550)
                            .addTrash(TrashType.PAPER, 1250, 550);

              // Tier 3 Trash
              builder.addTrash(TrashType.PAPER, 950, 250)
                            .addTrash(TrashType.GLASS, 1075, 250)
                            .addTrash(TrashType.PLASTIC, 1200, 250);

              builder.setShooterRod(1775, 100, eventBus);
              return builder.build();
       }

       @Override
       public String getLevelName() {
              return "Level 5: The Triple Tower";
       }

       @Override
       public String getText() {
              return "Three layers of flippers!\nKeep the earth clean!";
       }
}
