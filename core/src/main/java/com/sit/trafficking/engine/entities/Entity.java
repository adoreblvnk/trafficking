package com.sit.trafficking.engine.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.sit.trafficking.engine.managers.PhysicsManager;

public abstract class Entity {
    protected Body body;
    protected Vector2 position;
    protected boolean markedForDelete;
    protected Color color;

    public Entity(Body body) {
        this.body = body;
        this.position = new Vector2(body.getPosition());
        this.markedForDelete = false;
        this.color = Color.WHITE;
        
        // Link Entity to Body for collision handling
        this.body.setUserData(this);
    }

    public void update(float dt) {
        this.position.set(body.getPosition());
    }

    public abstract void render(ShapeRenderer shapeRenderer);

    public void dispose() {
        PhysicsManager.getInstance().getWorld().destroyBody(body);
    }

    public boolean isMarkedForDelete() {
        return markedForDelete;
    }
    
    public Body getBody() {
        return body;
    }
}
