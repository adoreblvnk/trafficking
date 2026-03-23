package com.sit.recyclingpinball.logic.level;

import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.logic.events.PinballEventBus;
import com.sit.recyclingpinball.logic.factories.TrashType;

public class DataDrivenLevelBlueprint implements ILevelBlueprint {
    private String text = "";
    private LevelConfig mergedConfig;
    private String name = "Unknown Level";

    public DataDrivenLevelBlueprint(String filepath, IEngineContext context) {
        String baseContent = context.getIO()
                .readInternalText(com.sit.recyclingpinball.logic.LogicConstants.PATH_BASE_LEVEL).orElse("{}");
        LevelConfig baseConfig = context.getIO().fromJson(baseContent, LevelConfig.class);

        String specificContent = context.getIO().readInternalText(filepath).orElse("{}");
        LevelConfig specificConfig = context.getIO().fromJson(specificContent, LevelConfig.class);

        mergedConfig = baseConfig != null ? baseConfig : new LevelConfig();
        if (specificConfig != null) {
            if (specificConfig.name != null)
                this.name = specificConfig.name;

            if (specificConfig.text != null)
                this.text = specificConfig.text.replace("\\n", "\n");

            if (specificConfig.wall != null)
                mergedConfig.wall.addAll(specificConfig.wall);
            if (specificConfig.slantedWall != null)
                mergedConfig.slantedWall.addAll(specificConfig.slantedWall);
            if (specificConfig.flipperLeft != null)
                mergedConfig.flipperLeft = specificConfig.flipperLeft;
            if (specificConfig.flipperRight != null)
                mergedConfig.flipperRight = specificConfig.flipperRight;
            if (specificConfig.shooter != null)
                mergedConfig.shooter = specificConfig.shooter;
            if (specificConfig.trash != null)
                mergedConfig.trash.addAll(specificConfig.trash);
        }
    }

    @Override
    public BoardLayout construct(BoardBuilder builder, PinballEventBus eventBus) {
        if (mergedConfig != null) {
            if (mergedConfig.wall != null) {
                for (LevelConfig.WallConfig wall : mergedConfig.wall) {
                    builder.addWall((int) wall.x, (int) wall.y, (int) wall.width, (int) wall.height);
                }
            }
            if (mergedConfig.slantedWall != null) {
                for (LevelConfig.SlantedWallConfig swall : mergedConfig.slantedWall) {
                    builder.addSlantedWall((int) swall.x, (int) swall.y, (int) swall.width, (int) swall.height,
                            swall.rotation);
                }
            }
            if (mergedConfig.flipperLeft != null) {
                builder.addLeftFlipper((int) mergedConfig.flipperLeft.x, (int) mergedConfig.flipperLeft.y);
            }
            if (mergedConfig.flipperRight != null) {
                builder.addRightFlipper((int) mergedConfig.flipperRight.x, (int) mergedConfig.flipperRight.y);
            }
            if (mergedConfig.shooter != null) {
                builder.setShooterRod((int) mergedConfig.shooter.x, (int) mergedConfig.shooter.y, eventBus);
            }
            if (mergedConfig.trash != null) {
                for (LevelConfig.TrashConfig trashItem : mergedConfig.trash) {
                    TrashType type = TrashType.valueOf(trashItem.type);
                    builder.addTrash(type, (int) trashItem.x, (int) trashItem.y, eventBus);
                }
            }
        }
        return builder.build();
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getLevelName() {
        return name;
    }
}
