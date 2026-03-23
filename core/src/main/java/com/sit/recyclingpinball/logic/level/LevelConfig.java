package com.sit.recyclingpinball.logic.level;

import java.util.ArrayList;
import java.util.List;

public class LevelConfig {
    public String name;
    public String text;
    public List<WallConfig> wall = new ArrayList<>();
    public List<SlantedWallConfig> slantedWall = new ArrayList<>();
    public List<FlipperConfig> flipperLeft = new ArrayList<>();
    public List<FlipperConfig> flipperRight = new ArrayList<>();
    public List<ShooterConfig> shooter = new ArrayList<>();
    public List<TrashConfig> trash = new ArrayList<>();

    public static class WallConfig {
        public float x;
        public float y;
        public float width;
        public float height;
    }

    public static class SlantedWallConfig {
        public float x;
        public float y;
        public float width;
        public float height;
        public float rotation;
    }

    public static class FlipperConfig {
        public float x;
        public float y;
    }

    public static class ShooterConfig {
        public float x;
        public float y;
    }

    public static class TrashConfig {
        public String type;
        public float x;
        public float y;
    }
}
