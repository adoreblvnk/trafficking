package com.sit.recyclingpinball.logic.level;

import java.util.ArrayList;
import java.util.List;

public class LevelConfig {
    private String name;
    private String text;
    private List<WallConfig> wall = new ArrayList<>();
    private List<SlantedWallConfig> slantedWall = new ArrayList<>();
    private FlipperConfig flipperLeft;
    private FlipperConfig flipperRight;
    private ShooterConfig shooter;
    private List<TrashConfig> trash = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<WallConfig> getWall() {
        return wall;
    }

    public void setWall(List<WallConfig> wall) {
        this.wall = wall != null ? wall : new ArrayList<>();
    }

    public List<SlantedWallConfig> getSlantedWall() {
        return slantedWall;
    }

    public void setSlantedWall(List<SlantedWallConfig> slantedWall) {
        this.slantedWall = slantedWall != null ? slantedWall : new ArrayList<>();
    }

    public FlipperConfig getFlipperLeft() {
        return flipperLeft;
    }

    public void setFlipperLeft(FlipperConfig flipperLeft) {
        this.flipperLeft = flipperLeft;
    }

    public FlipperConfig getFlipperRight() {
        return flipperRight;
    }

    public void setFlipperRight(FlipperConfig flipperRight) {
        this.flipperRight = flipperRight;
    }

    public ShooterConfig getShooter() {
        return shooter;
    }

    public void setShooter(ShooterConfig shooter) {
        this.shooter = shooter;
    }

    public List<TrashConfig> getTrash() {
        return trash;
    }

    public void setTrash(List<TrashConfig> trash) {
        this.trash = trash != null ? trash : new ArrayList<>();
    }

    public static class WallConfig {
        private float x;
        private float y;
        private float width;
        private float height;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getWidth() {
            return width;
        }

        public void setWidth(float width) {
            this.width = Math.max(0f, width);
        }

        public float getHeight() {
            return height;
        }

        public void setHeight(float height) {
            this.height = Math.max(0f, height);
        }
    }

    public static class SlantedWallConfig {
        private float x;
        private float y;
        private float width;
        private float height;
        private float rotation;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getWidth() {
            return width;
        }

        public void setWidth(float width) {
            this.width = Math.max(0f, width);
        }

        public float getHeight() {
            return height;
        }

        public void setHeight(float height) {
            this.height = Math.max(0f, height);
        }

        public float getRotation() {
            return rotation;
        }

        public void setRotation(float rotation) {
            this.rotation = rotation;
        }
    }

    public static class FlipperConfig {
        private float x;
        private float y;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }

    public static class ShooterConfig {
        private float x;
        private float y;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }

    public static class TrashConfig {
        private String type;
        private float x;
        private float y;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }
}
