package com.sit.recyclingpinball.engine.physics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class OBBCollider implements ICollider {
    private float x;
    private float y;
    private float width;
    private float height;
    private float originX;
    private float originY;
    private float rotationDegrees;

    public OBBCollider(float x, float y, float width, float height, float originX, float originY, float rotationDegrees) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.originX = originX;
        this.originY = originY;
        this.rotationDegrees = rotationDegrees;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setRotation(float rotationDegrees) {
        this.rotationDegrees = rotationDegrees;
    }

    public Vector2[] getVertices() {
        Vector2[] vertices = new Vector2[4];
        float rad = rotationDegrees * MathUtils.degreesToRadians;
        float cos = MathUtils.cos(rad);
        float sin = MathUtils.sin(rad);

        float dx1 = -originX;
        float dy1 = -originY;
        float dx2 = width - originX;
        float dy2 = height - originY;

        float p1x = dx1 * cos - dy1 * sin;
        float p1y = dx1 * sin + dy1 * cos;

        float p2x = dx2 * cos - dy1 * sin;
        float p2y = dx2 * sin + dy1 * cos;

        float p3x = dx2 * cos - dy2 * sin;
        float p3y = dx2 * sin + dy2 * cos;

        float p4x = dx1 * cos - dy2 * sin;
        float p4y = dx1 * sin + dy2 * cos;

        vertices[0] = new Vector2(x + originX + p1x, y + originY + p1y);
        vertices[1] = new Vector2(x + originX + p2x, y + originY + p2y);
        vertices[2] = new Vector2(x + originX + p3x, y + originY + p3y);
        vertices[3] = new Vector2(x + originX + p4x, y + originY + p4y);

        return vertices;
    }

    public Vector2[] getAxes() {
        Vector2[] axes = new Vector2[2];
        Vector2[] vertices = getVertices();
        axes[0] = SATMathUtils.normalize(new Vector2(vertices[1].x - vertices[0].x, vertices[1].y - vertices[0].y));
        axes[1] = SATMathUtils.normalize(new Vector2(vertices[2].x - vertices[1].x, vertices[2].y - vertices[1].y));
        return axes;
    }

    @Override
    public Rectangle getAABB() {
        Vector2[] vertices = getVertices();
        float minX = vertices[0].x;
        float minY = vertices[0].y;
        float maxX = vertices[0].x;
        float maxY = vertices[0].y;

        for (int i = 1; i < vertices.length; i++) {
            if (vertices[i].x < minX) minX = vertices[i].x;
            if (vertices[i].y < minY) minY = vertices[i].y;
            if (vertices[i].x > maxX) maxX = vertices[i].x;
            if (vertices[i].y > maxY) maxY = vertices[i].y;
        }

        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public boolean intersects(ICollider other) {
        if (other instanceof CircleCollider) {
            return checkOBBVsCircle(this, (CircleCollider) other);
        } else if (other instanceof BoxCollider) {
            return checkOBBVsBox(this, (BoxCollider) other);
        } else if (other instanceof OBBCollider) {
            return checkOBBVsOBB(this, (OBBCollider) other);
        }
        return false;
    }

    @Override
    public boolean contains(float px, float py) {
        Vector2[] vertices = getVertices();
        boolean result = false;
        for (int i = 0, j = vertices.length - 1; i < vertices.length; j = i++) {
            if ((vertices[i].y > py) != (vertices[j].y > py) &&
                (px < (vertices[j].x - vertices[i].x) * (py - vertices[i].y) / (vertices[j].y - vertices[i].y) + vertices[i].x)) {
                result = !result;
            }
        }
        return result;
    }

    private boolean checkOBBVsCircle(OBBCollider obb, CircleCollider circle) {
        Vector2[] vertices = obb.getVertices();
        Vector2 center = new Vector2(circle.getCircle().x, circle.getCircle().y);
        float radius = circle.getCircle().radius;

        Vector2[] axes = obb.getAxes();
        
        Vector2 closestVertex = vertices[0];
        float minDst2 = center.dst2(vertices[0]);
        for (int i = 1; i < vertices.length; i++) {
            float dst2 = center.dst2(vertices[i]);
            if (dst2 < minDst2) {
                minDst2 = dst2;
                closestVertex = vertices[i];
            }
        }
        Vector2 circleAxis = SATMathUtils.normalize(new Vector2(closestVertex.x - center.x, closestVertex.y - center.y));

        Vector2[] allAxes = new Vector2[axes.length + 1];
        System.arraycopy(axes, 0, allAxes, 0, axes.length);
        allAxes[axes.length] = circleAxis;

        for (Vector2 axis : allAxes) {
            float[] proj1 = SATMathUtils.projectPolygon(axis, vertices);
            float[] proj2 = SATMathUtils.projectCircle(axis, center, radius);

            if (!SATMathUtils.overlap(proj1, proj2)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkOBBVsBox(OBBCollider obb, BoxCollider box) {
        Rectangle r = box.getAABB();
        Vector2[] boxVertices = new Vector2[] {
            new Vector2(r.x, r.y),
            new Vector2(r.x + r.width, r.y),
            new Vector2(r.x + r.width, r.y + r.height),
            new Vector2(r.x, r.y + r.height)
        };
        Vector2[] boxAxes = new Vector2[] {
            new Vector2(1, 0),
            new Vector2(0, 1)
        };

        Vector2[] obbVertices = obb.getVertices();
        Vector2[] obbAxes = obb.getAxes();

        Vector2[] allAxes = new Vector2[boxAxes.length + obbAxes.length];
        System.arraycopy(boxAxes, 0, allAxes, 0, boxAxes.length);
        System.arraycopy(obbAxes, 0, allAxes, boxAxes.length, obbAxes.length);

        for (Vector2 axis : allAxes) {
            float[] proj1 = SATMathUtils.projectPolygon(axis, obbVertices);
            float[] proj2 = SATMathUtils.projectPolygon(axis, boxVertices);

            if (!SATMathUtils.overlap(proj1, proj2)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkOBBVsOBB(OBBCollider obb1, OBBCollider obb2) {
        Vector2[] vertices1 = obb1.getVertices();
        Vector2[] vertices2 = obb2.getVertices();
        Vector2[] axes1 = obb1.getAxes();
        Vector2[] axes2 = obb2.getAxes();

        Vector2[] allAxes = new Vector2[axes1.length + axes2.length];
        System.arraycopy(axes1, 0, allAxes, 0, axes1.length);
        System.arraycopy(axes2, 0, allAxes, axes1.length, axes2.length);

        for (Vector2 axis : allAxes) {
            float[] proj1 = SATMathUtils.projectPolygon(axis, vertices1);
            float[] proj2 = SATMathUtils.projectPolygon(axis, vertices2);

            if (!SATMathUtils.overlap(proj1, proj2)) {
                return false;
            }
        }
        return true;
    }
}
