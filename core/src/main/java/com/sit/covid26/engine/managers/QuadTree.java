package com.sit.covid26.engine.managers;

import com.badlogic.gdx.math.Rectangle;
import com.sit.covid26.engine.EngineConstants;
import com.sit.covid26.engine.interfaces.ICollidable;

import java.util.ArrayList;
import java.util.List;

/**
 * QuadTree for spatial partitioning to optimize collision detection.
 */
public class QuadTree {

    private int level;
    private List<ICollidable> objects;
    private Rectangle bounds;
    private QuadTree[] nodes;

    public QuadTree(int level, Rectangle bounds) {
        this.level = level;
        this.objects = new ArrayList<>();
        this.bounds = bounds;
        this.nodes = new QuadTree[4];
    }

    public void clear() {
        objects.clear();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    private void split() {
        float subWidth = bounds.width / 2f;
        float subHeight = bounds.height / 2f;
        float x = bounds.x;
        float y = bounds.y;

        nodes[0] = new QuadTree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight)); // Top Right
        nodes[1] = new QuadTree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));            // Top Left
        nodes[2] = new QuadTree(level + 1, new Rectangle(x, y, subWidth, subHeight));                        // Bottom Left
        nodes[3] = new QuadTree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));             // Bottom Right
    }

    private int getIndex(Rectangle rect) {
        int index = -1;
        float verticalMidpoint = bounds.x + (bounds.width / 2f);
        float horizontalMidpoint = bounds.y + (bounds.height / 2f);

        boolean topQuadrant = (rect.y >= horizontalMidpoint);
        boolean bottomQuadrant = (rect.y < horizontalMidpoint && (rect.y + rect.height) < horizontalMidpoint);
        boolean leftQuadrant = (rect.x < verticalMidpoint && (rect.x + rect.width) < verticalMidpoint);
        boolean rightQuadrant = (rect.x >= verticalMidpoint);

        if (leftQuadrant) {
            if (topQuadrant) {
                index = 1;
            } else if (bottomQuadrant) {
                index = 2;
            }
        } else if (rightQuadrant) {
            if (topQuadrant) {
                index = 0;
            } else if (bottomQuadrant) {
                index = 3;
            }
        }

        return index;
    }

    public void insert(ICollidable pRect) {
        if (nodes[0] != null) {
            int index = getIndex(pRect.getBounds());
            if (index != -1) {
                nodes[index].insert(pRect);
                return;
            }
        }

        objects.add(pRect);

        if (objects.size() > EngineConstants.QUADTREE_MAX_OBJECTS && level < EngineConstants.QUADTREE_MAX_LEVELS) {
            if (nodes[0] == null) {
                split();
            }

            int i = 0;
            while (i < objects.size()) {
                int index = getIndex(objects.get(i).getBounds());
                if (index != -1) {
                    nodes[index].insert(objects.remove(i));
                } else {
                    i++;
                }
            }
        }
    }

    public List<ICollidable> retrieve(List<ICollidable> returnObjects, Rectangle rect) {
        if (nodes[0] != null) {
            for (int i = 0; i < nodes.length; i++) {
                if (nodes[i].bounds.overlaps(rect)) {
                    nodes[i].retrieve(returnObjects, rect);
                }
            }
        }

        returnObjects.addAll(objects);
        return returnObjects;
    }
}
