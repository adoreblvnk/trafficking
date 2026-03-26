package com.sit.recyclingpinball.logic.entities;

import com.sit.recyclingpinball.engine.physics.OBBCollider;
import com.sit.recyclingpinball.engine.platform.libgdx.PlatformGraphics;
import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformVector2;

public class OBBWallEntity extends WallEntity {
    private static final float FILL_STEP = 2f;
    private final OBBCollider obbCollider;

    public OBBWallEntity(String id, float x, float y, float w, float h, float rotationDegrees) {
        super(id, x, y, w, h);
        this.obbCollider = new OBBCollider(x, y, w, h, w / 2, h / 2, rotationDegrees);
        setCollider(this.obbCollider);
    }

    @Override
    public void render(PlatformGraphics graphics) {
        PlatformVector2[] verts = this.obbCollider.getVertices();

        graphics.setColor(getRed(), getGreen(), getBlue(), getAlpha());

        float edgeLengthA = verts[0].dst(verts[3]);
        float edgeLengthB = verts[1].dst(verts[2]);
        int strips = Math.max(1, (int) (Math.max(edgeLengthA, edgeLengthB) / FILL_STEP));

        for (int i = 0; i <= strips; i++) {
            float t = (float) i / strips;
            PlatformVector2 start = lerp(verts[0], verts[3], t);
            PlatformVector2 end = lerp(verts[1], verts[2], t);
            graphics.drawLine(start.getX(), start.getY(), end.getX(), end.getY(), FILL_STEP);
        }

        for (int i = 0; i < verts.length; i++) {
            PlatformVector2 p1 = verts[i];
            PlatformVector2 p2 = verts[(i + 1) % verts.length];
            graphics.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY(), 0.4f, 0.4f, 0.4f, 1f);
        }
    }

    private PlatformVector2 lerp(PlatformVector2 from, PlatformVector2 to, float t) {
        return new PlatformVector2(from.getX() + (to.getX() - from.getX()) * t,
                from.getY() + (to.getY() - from.getY()) * t);
    }
}
