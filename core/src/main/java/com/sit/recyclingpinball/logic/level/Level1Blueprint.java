package com.sit.recyclingpinball.logic.level;

import com.sit.recyclingpinball.logic.factories.TrashType;

public class Level1Blueprint extends BaseLevelBlueprint {
       @Override
       protected void addCustomElements(BoardBuilder builder) {
              builder.addTrash(TrashType.PLASTIC, 800, 600)
                            .addTrash(TrashType.PAPER, 1200, 700)
                            .addTrash(TrashType.GLASS, 1000, 800);
       }

       @Override
       public String getLevelName() {
              return "Level 1: The Basics";
       }

       @Override
       public String getText() {
              return "Keep the earth clean!\nRecycle plastic, paper, and glass.";
       }
}
