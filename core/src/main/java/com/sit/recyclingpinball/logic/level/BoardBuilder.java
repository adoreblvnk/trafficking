package com.sit.recyclingpinball.logic.level;

import com.sit.recyclingpinball.logic.LogicConstants;
import com.sit.recyclingpinball.logic.entities.FlipperEntity;
import com.sit.recyclingpinball.logic.entities.OBBWallEntity;
import com.sit.recyclingpinball.logic.entities.WallEntity;
import com.sit.recyclingpinball.logic.entities.ShooterRodEntity;
import com.sit.recyclingpinball.logic.events.PinballEventBus;
import com.sit.recyclingpinball.logic.factories.TrashFactory;
import com.sit.recyclingpinball.logic.factories.TrashType;

public class BoardBuilder {
    private final BoardLayout layout;

    public BoardBuilder() {
        this.layout = new BoardLayout();
    }

    public BoardBuilder addSlantedWall(float x, float y, float w, float h, float rotationDegrees) {
        layout.addWall(new OBBWallEntity(LogicConstants.ID_WALL_PREFIX + System.nanoTime(), x, y, w, h, rotationDegrees));
        return this;
    }

    public BoardBuilder addWall(float x, float y, float w, float h) {
        layout.addWall(new WallEntity(LogicConstants.ID_WALL_PREFIX + System.nanoTime(), x, y, w, h));
        return this;
    }

    public BoardBuilder addLeftFlipper(float x, float y) {
        layout.addFlipper(new FlipperEntity(LogicConstants.ID_FLIPPER_L_PREFIX + System.nanoTime(), x, y, true));
        return this;
    }

    public BoardBuilder addRightFlipper(float x, float y) {
        layout.addFlipper(new FlipperEntity(LogicConstants.ID_FLIPPER_R_PREFIX + System.nanoTime(), x, y, false));
        return this;
    }

    public BoardBuilder addTrash(TrashType type, float x, float y, PinballEventBus eventBus) {
        layout.addTrash(TrashFactory.createTrash(type, x, y, eventBus));
        return this;
    }

    public BoardBuilder setShooterRod(float x, float y, PinballEventBus eventBus) {
        ShooterRodEntity rod = new ShooterRodEntity(LogicConstants.ID_SHOOTER_ROD_PREFIX + System.nanoTime(), x, y,
                eventBus);
        layout.setShooterRod(rod);
        return this;
    }

    public BoardLayout build() {
        return layout;
    }
}
