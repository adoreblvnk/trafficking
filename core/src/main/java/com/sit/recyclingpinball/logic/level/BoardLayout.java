package com.sit.recyclingpinball.logic.level;

import com.sit.recyclingpinball.logic.entities.FlipperEntity;
import com.sit.recyclingpinball.logic.entities.TrashEntity;
import com.sit.recyclingpinball.logic.entities.WallEntity;
import com.sit.recyclingpinball.logic.entities.ShooterRodEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoardLayout {
    private final List<WallEntity> walls;
    private final List<FlipperEntity> flippers;
    private final List<TrashEntity> trashes;
    private ShooterRodEntity shooterRod;

    public BoardLayout() {
        this.walls = new ArrayList<>();
        this.flippers = new ArrayList<>();
        this.trashes = new ArrayList<>();
    }

    public void addWall(WallEntity wall) {
        walls.add(wall);
    }
    public void addFlipper(FlipperEntity flipper) {
        flippers.add(flipper);
    }
    public void addTrash(TrashEntity trash) {
        trashes.add(trash);
    }

    public ShooterRodEntity getShooterRod() {
        return shooterRod;
    }
    public void setShooterRod(ShooterRodEntity shooterRod) {
        this.shooterRod = shooterRod;
    }

    public List<WallEntity> getWalls() {
        return Collections.unmodifiableList(walls);
    }
    public List<FlipperEntity> getFlippers() {
        return Collections.unmodifiableList(flippers);
    }
    public List<TrashEntity> getTrashes() {
        return Collections.unmodifiableList(trashes);
    }
}
