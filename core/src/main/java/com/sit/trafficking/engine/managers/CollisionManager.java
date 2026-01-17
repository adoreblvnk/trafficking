package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.sit.trafficking.engine.entities.Entity;
import com.sit.trafficking.engine.entities.ICollidable;

public class CollisionManager implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        // Moved logic to postSolve to get impulse intensity
    }

    @Override
    public void endContact(Contact contact) { }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        Object userDataA = fixA.getBody().getUserData();
        Object userDataB = fixB.getBody().getUserData();

        // Get normal impulse (intensity of collision)
        float intensity = 0f;
        float[] normalImpulses = impulse.getNormalImpulses();
        for (float val : normalImpulses) {
            intensity += val;
        }

        // Java 21 Pattern Matching for instanceof
        if (userDataA instanceof ICollidable colA && userDataB instanceof Entity entB) {
            colA.onCollision(entB, intensity);
        }

        if (userDataB instanceof ICollidable colB && userDataA instanceof Entity entA) {
            colB.onCollision(entA, intensity);
        }
    }
}
