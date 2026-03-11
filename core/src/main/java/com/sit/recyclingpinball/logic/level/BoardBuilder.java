package com.sit.recyclingpinball.logic.level;

import com.sit.recyclingpinball.logic.entities.FlipperEntity;
import com.sit.recyclingpinball.logic.entities.WallEntity;
import com.sit.recyclingpinball.logic.factories.TrashFactory;
import com.sit.recyclingpinball.logic.factories.TrashType;

public class BoardBuilder {
    private final BoardLayout layout;
    
    public BoardBuilder() {
        this.layout = new BoardLayout();
    }
    
    public BoardBuilder addSlantedWall(float x, float y, float w, float h, float rotationDegrees) {
        layout.addWall(new com.sit.recyclingpinball.logic.entities.OBBWallEntity("wall_" + System.nanoTime(), x, y, w, h, rotationDegrees));
        return this;
    }

    public BoardBuilder addWall(float x, float y, float w, float h) {
        layout.addWall(new WallEntity("wall_" + System.nanoTime(), x, y, w, h));
        return this;
    }
    
    public BoardBuilder addLeftFlipper(float x, float y) {
        layout.addFlipper(new FlipperEntity("flipper_l_" + System.nanoTime(), x, y, true));
        return this;
    }
    
    public BoardBuilder addRightFlipper(float x, float y) {
        layout.addFlipper(new FlipperEntity("flipper_r_" + System.nanoTime(), x, y, false));
        return this;
    }
    
    public BoardBuilder addTrash(TrashType type, float x, float y) {
        layout.addTrash(TrashFactory.createTrash(type, x, y));
        return this;
    }
    
    public BoardLayout build() {
        return layout;
    }
}
