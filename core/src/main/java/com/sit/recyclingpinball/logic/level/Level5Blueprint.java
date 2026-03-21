package com.sit.recyclingpinball.logic.level;

import com.sit.recyclingpinball.logic.factories.TrashType;

public class Level5Blueprint extends BaseLevelBlueprint {
       @Override
       protected void addCustomElements(BoardBuilder builder) {
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
