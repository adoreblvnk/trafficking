package com.sit.recyclingpinball.engine.managers;

import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle;
import com.sit.recyclingpinball.engine.EngineConstants;
import com.sit.recyclingpinball.engine.interfaces.ICollidable;

import java.util.ArrayList;
import java.util.List;

/**
 * QuadTree for broad-phase spatial partitioning to optimize collision detection.
 *
 * Design note: this class intentionally does not require a getBounds() method on
 * {@code ICollidable}. Instead, broad-phase bounds are obtained through the
 * polymorphic collider contract: {@code collidable.getCollider().getAABB()}.
 * This keeps {@code ICollidable} focused while still giving QuadTree the AABB it
 * needs, and avoids fragile {@code instanceof} or downcasting to concrete entity
 * classes.
 */
public class QuadTree {

    private int level;
    private List<ICollidable> objects;
    private PlatformRectangle bounds;
    private QuadTree[] nodes;

    public QuadTree(int level, PlatformRectangle bounds) {
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
        float subWidth = bounds.getWidth() / 2f;
        float subHeight = bounds.getHeight() / 2f;
        float x = bounds.getX();
        float y = bounds.getY();

        nodes[0] = new QuadTree(level + 1, new PlatformRectangle(x + subWidth, y + subHeight, subWidth, subHeight)); // Top
                                                                                                                        // Right
        nodes[1] = new QuadTree(level + 1, new PlatformRectangle(x, y + subHeight, subWidth, subHeight)); // Top Left
        nodes[2] = new QuadTree(level + 1, new PlatformRectangle(x, y, subWidth, subHeight)); // Bottom Left
        nodes[3] = new QuadTree(level + 1, new PlatformRectangle(x + subWidth, y, subWidth, subHeight)); // Bottom Right
    }

    private int getIndex(PlatformRectangle rect) {
        int index = -1;
        float verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2f);
        float horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2f);

        boolean topQuadrant = (rect.getY() >= horizontalMidpoint);
        boolean bottomQuadrant = (rect.getY() < horizontalMidpoint
                && (rect.getY() + rect.getHeight()) < horizontalMidpoint);
        boolean leftQuadrant = (rect.getX() < verticalMidpoint && (rect.getX() + rect.getWidth()) < verticalMidpoint);
        boolean rightQuadrant = (rect.getX() >= verticalMidpoint);

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
        // Broad-phase indexing uses collider-provided AABB. No entity downcast is
        // needed, so any ICollidable with a valid collider can be partitioned.
        if (nodes[0] != null) {
            int index = getIndex(pRect.getCollider().getAABB());
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
                // Re-index using each object's collider AABB to keep partition logic
                // independent of concrete entity types.
                int index = getIndex(objects.get(i).getCollider().getAABB());
                if (index != -1) {
                    nodes[index].insert(objects.remove(i));
                } else {
                    i++;
                }
            }
        }
    }

    public List<ICollidable> retrieve(List<ICollidable> returnObjects, PlatformRectangle rect) {
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
