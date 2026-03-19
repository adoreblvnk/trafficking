package com.sit.recyclingpinball.logic.level;

import com.sit.recyclingpinball.logic.events.PinballEventBus;
import com.sit.recyclingpinball.logic.factories.TrashType;

public class Level4Blueprint implements ILevelBlueprint {
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

              // Bottom Deck Flippers
              builder.addLeftFlipper(850, 100)
                            .addRightFlipper(1300, 100);

              // Pachinko Grid (Pegs)
              int[] rowY = { 800, 700, 600, 500, 400 };
              int[] startX1 = { 650, 850, 1050, 1250, 1450 }; // for even rows
              int[] startX2 = { 750, 950, 1150, 1350, 1550 }; // for odd rows

              for (int i = 0; i < rowY.length; i++) {
                     int[] xCoords = (i % 2 == 0) ? startX1 : startX2;
                     for (int x : xCoords) {
                            float angle = ((x + rowY[i]) % 100 == 0) ? 45f : -45f;
                            builder.addSlantedWall(x, rowY[i], 30, 20, angle);
                     }
              }

              // Thrash interspersed between pegs
              builder.addTrash(TrashType.PLASTIC, 850, 750)
                            .addTrash(TrashType.PAPER, 1050, 750)
                            .addTrash(TrashType.GLASS, 1250, 750)
                            .addTrash(TrashType.PLASTIC, 750, 650)
                            .addTrash(TrashType.PAPER, 950, 650)
                            .addTrash(TrashType.GLASS, 1150, 650)
                            .addTrash(TrashType.PLASTIC, 850, 550)
                            .addTrash(TrashType.PAPER, 1050, 550)
                            .addTrash(TrashType.GLASS, 1250, 550)
                            .addTrash(TrashType.PLASTIC, 950, 450);

              builder.setShooterRod(1775, 100, eventBus);
              return builder.build();
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
