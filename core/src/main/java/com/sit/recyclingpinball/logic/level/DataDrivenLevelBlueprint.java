package com.sit.recyclingpinball.logic.level;

import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.logic.events.PinballEventBus;
import com.sit.recyclingpinball.logic.factories.TrashType;

import java.util.ArrayList;
import java.util.List;

public class DataDrivenLevelBlueprint implements ILevelBlueprint {
    private String text = "";
    private final LevelConfig mergedConfig;
    private String name = "Unknown Level";

    public DataDrivenLevelBlueprint(String filepath, IEngineContext context) {
        String baseContent = context.getIO()
                .readInternalText(com.sit.recyclingpinball.logic.LogicConstants.PATH_BASE_LEVEL).orElse("{}");
        LevelConfig baseConfig = context.getIO().fromJson(baseContent, LevelConfig.class);

        String specificContent = context.getIO().readInternalText(filepath).orElse("{}");
        LevelConfig specificConfig = context.getIO().fromJson(specificContent, LevelConfig.class);

        mergedConfig = merge(baseConfig, specificConfig);
        if (specificConfig != null) {
            if (specificConfig.getName() != null) {
                this.name = specificConfig.getName();
            }

            if (specificConfig.getText() != null) {
                this.text = specificConfig.getText().replace("\\n", "\n");
            }
        }
    }

    private static LevelConfig merge(LevelConfig baseConfig, LevelConfig specificConfig) {
        LevelConfig base = baseConfig != null ? baseConfig : new LevelConfig();

        if (specificConfig == null) {
            return base;
        }

        LevelConfig merged = new LevelConfig();
        merged.setName(specificConfig.getName() != null ? specificConfig.getName() : base.getName());
        merged.setText(specificConfig.getText() != null ? specificConfig.getText() : base.getText());
        merged.setWall(concat(base.getWall(), specificConfig.getWall()));
        merged.setSlantedWall(concat(base.getSlantedWall(), specificConfig.getSlantedWall()));
        merged.setFlipperLeft(
                specificConfig.getFlipperLeft() != null ? specificConfig.getFlipperLeft() : base.getFlipperLeft());
        merged.setFlipperRight(
                specificConfig.getFlipperRight() != null ? specificConfig.getFlipperRight() : base.getFlipperRight());
        merged.setShooter(specificConfig.getShooter() != null ? specificConfig.getShooter() : base.getShooter());
        merged.setTrash(concat(base.getTrash(), specificConfig.getTrash()));
        return merged;
    }

    private static <T> List<T> concat(List<T> first, List<T> second) {
        List<T> merged = new ArrayList<>();
        if (first != null) {
            merged.addAll(first);
        }
        if (second != null) {
            merged.addAll(second);
        }
        return merged;
    }

    @Override
    public BoardLayout construct(BoardBuilder builder, PinballEventBus eventBus) {
        if (mergedConfig.getWall() != null) {
            for (LevelConfig.WallConfig wall : mergedConfig.getWall()) {
                builder.addWall((int) wall.getX(), (int) wall.getY(), (int) wall.getWidth(), (int) wall.getHeight());
            }
        }
        if (mergedConfig.getSlantedWall() != null) {
            for (LevelConfig.SlantedWallConfig swall : mergedConfig.getSlantedWall()) {
                builder.addSlantedWall((int) swall.getX(), (int) swall.getY(), (int) swall.getWidth(),
                        (int) swall.getHeight(), swall.getRotation());
            }
        }
        if (mergedConfig.getFlipperLeft() != null) {
            builder.addLeftFlipper((int) mergedConfig.getFlipperLeft().getX(), (int) mergedConfig.getFlipperLeft().getY());
        }
        if (mergedConfig.getFlipperRight() != null) {
            builder.addRightFlipper((int) mergedConfig.getFlipperRight().getX(), (int) mergedConfig.getFlipperRight().getY());
        }
        if (mergedConfig.getShooter() != null) {
            builder.setShooterRod((int) mergedConfig.getShooter().getX(), (int) mergedConfig.getShooter().getY(), eventBus);
        }
        if (mergedConfig.getTrash() != null) {
            for (LevelConfig.TrashConfig trashItem : mergedConfig.getTrash()) {
                TrashType type = TrashType.valueOf(trashItem.getType());
                builder.addTrash(type, (int) trashItem.getX(), (int) trashItem.getY(), eventBus);
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
