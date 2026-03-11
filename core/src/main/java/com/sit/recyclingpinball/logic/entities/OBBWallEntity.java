package com.sit.recyclingpinball.logic.entities;

import com.sit.recyclingpinball.engine.physics.OBBCollider;
import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;
import com.badlogic.gdx.math.Vector2;

public class OBBWallEntity extends WallEntity {
    private final float rotationDegrees;
    private static final float FILL_STEP = 2f;

    public OBBWallEntity(String id, float x, float y, float w, float h, float rotationDegrees) {
        super(id, x, y, w, h);
        this.rotationDegrees = rotationDegrees;
        this.collider = new OBBCollider(x, y, w, h, w / 2, h / 2, rotationDegrees);
    }

    @Override
    public void render(IGraphicsProvider graphics) {
        if (collider instanceof OBBCollider) {
            Vector2[] verts = ((OBBCollider) collider).getVertices();

            graphics.setColor(getRed(), getGreen(), getBlue(), getAlpha());

            float edgeLengthA = verts[0].dst(verts[3]);
            float edgeLengthB = verts[1].dst(verts[2]);
            int strips = Math.max(1, (int) (Math.max(edgeLengthA, edgeLengthB) / FILL_STEP));

            for (int i = 0; i <= strips; i++) {
                float t = (float) i / strips;
                Vector2 start = lerp(verts[0], verts[3], t);
                Vector2 end = lerp(verts[1], verts[2], t);
                graphics.drawLine(start.x, start.y, end.x, end.y, FILL_STEP);
            }

            for (int i = 0; i < verts.length; i++) {
                Vector2 p1 = verts[i];
                Vector2 p2 = verts[(i + 1) % verts.length];
                graphics.drawLine(p1.x, p1.y, p2.x, p2.y, 0.4f, 0.4f, 0.4f, 1f);
            }
        }
    }

    private Vector2 lerp(Vector2 from, Vector2 to, float t) {
        return new Vector2(
                from.x + (to.x - from.x) * t,
                from.y + (to.y - from.y) * t
        );
    }
}
