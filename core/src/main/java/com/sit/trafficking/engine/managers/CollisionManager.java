package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sit.trafficking.engine.EngineConstants;
import com.sit.trafficking.engine.interfaces.ICollidable;
import com.sit.trafficking.engine.interfaces.Movable;
import java.util.List;

public class CollisionManager {

    public CollisionManager() {
    }

    public void processCollisions(List<? extends ICollidable> entities) {
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                ICollidable a = entities.get(i);
                ICollidable b = entities.get(j);

                if (checkAABB(a.getBounds(), b.getBounds())) {
                    resolveCollision(a, b);
                    a.onCollision(b);
                    b.onCollision(a);
                }
            }
        }
    }

    private boolean checkAABB(Rectangle rectA, Rectangle rectB) {
        return rectA.overlaps(rectB);
    }

    private void resolveCollision(ICollidable a, ICollidable b) {
        if (a.isStatic() && b.isStatic()) return;
        if (a.isTrigger() || b.isTrigger()) return;

        resolvePhysics(a, b);
    }

    private void resolvePhysics(ICollidable a, ICollidable b) {
        Rectangle rA = a.getBounds();
        Rectangle rB = b.getBounds();
        Rectangle intersection = new Rectangle();
        Intersector.intersectRectangles(rA, rB, intersection);

        float overlapX = intersection.width;
        float overlapY = intersection.height;
        float pushX = 0, pushY = 0;

        if (overlapX < overlapY) {
            if (rA.x < rB.x) pushX = -overlapX;
            else pushX = overlapX;
        } else {
            if (rA.y < rB.y) pushY = -overlapY;
            else pushY = overlapY;
        }

        if (!a.isStatic() && b.isStatic()) {
            a.getPosition().add(pushX, pushY);
            if (a instanceof Movable) {
                Vector2 vel = ((Movable) a).getVelocity();
                if (pushX != 0) vel.x *= -EngineConstants.DEFAULT_BOUNCE;
                if (pushY != 0) vel.y *= -EngineConstants.DEFAULT_BOUNCE;
            }
        } else if (a.isStatic() && !b.isStatic()) {
            b.getPosition().add(-pushX, -pushY);
            if (b instanceof Movable) {
                Vector2 vel = ((Movable) b).getVelocity();
                if (pushX != 0) vel.x *= -EngineConstants.DEFAULT_BOUNCE;
                if (pushY != 0) vel.y *= -EngineConstants.DEFAULT_BOUNCE;
            }
        } else if (!a.isStatic() && !b.isStatic()) {
            a.getPosition().add(pushX * EngineConstants.PUSH_OUT_FACTOR, pushY * EngineConstants.PUSH_OUT_FACTOR);
            b.getPosition().add(-pushX * EngineConstants.PUSH_OUT_FACTOR, -pushY * EngineConstants.PUSH_OUT_FACTOR);

            if (a instanceof Movable && b instanceof Movable) {
                Vector2 vA = ((Movable) a).getVelocity();
                Vector2 vB = ((Movable) b).getVelocity();

                if (pushX != 0) {
                    float temp = vA.x;
                    vA.x = vB.x * EngineConstants.DEFAULT_BOUNCE;
                    vB.x = temp * EngineConstants.DEFAULT_BOUNCE;
                }
                if (pushY != 0) {
                    float temp = vA.y;
                    vA.y = vB.y * EngineConstants.DEFAULT_BOUNCE;
                    vB.y = temp * EngineConstants.DEFAULT_BOUNCE;
                }
            }
        }
    }
}
