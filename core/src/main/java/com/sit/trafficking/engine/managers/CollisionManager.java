package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.physics.box2d.*;
import com.sit.trafficking.engine.entities.Entity;
import com.sit.trafficking.engine.entities.ICollidable;

public class CollisionManager implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        Object userDataA = fixA.getBody().getUserData();
        Object userDataB = fixB.getBody().getUserData();
        
        // Java 21 Pattern Matching for instanceof
        if (userDataA instanceof ICollidable colA && userDataB instanceof Entity entB) {
            colA.onCollision(entB);
        }

        if (userDataB instanceof ICollidable colB && userDataA instanceof Entity entA) {
            colB.onCollision(entA);
        }
    }

    @Override
    public void endContact(Contact contact) { }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) { }
}
