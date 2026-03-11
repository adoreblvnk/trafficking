package com.sit.recyclingpinball.logic.entities;

import com.sit.recyclingpinball.engine.physics.OBBCollider;
import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;
import com.badlogic.gdx.math.Vector2;

public class OBBWallEntity extends WallEntity {
    private final float rotationDegrees;

    public OBBWallEntity(String id, float x, float y, float w, float h, float rotationDegrees) {
        super(id, x, y, w, h);
        this.rotationDegrees = rotationDegrees;
        this.collider = new OBBCollider(x, y, w, h, w / 2, h / 2, rotationDegrees);
    }

    @Override
    public void render(IGraphicsProvider graphics) {
        if (collider instanceof OBBCollider) {
            Vector2[] verts = ((OBBCollider) collider).getVertices();
            for(int i = 0; i < verts.length; i++){
                Vector2 p1 = verts[i];
                Vector2 p2 = verts[(i+1)%verts.length];
                graphics.drawLine(p1.x, p1.y, p2.x, p2.y, 0.4f, 0.4f, 0.4f, 1f);
            }
        }
    }
}
