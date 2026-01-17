package com.sit.trafficking;

import com.badlogic.gdx.Game;
import com.sit.trafficking.engine.managers.CollisionManager;
import com.sit.trafficking.engine.managers.PhysicsManager;
import com.sit.trafficking.engine.managers.SceneManager;
import com.sit.trafficking.screens.LoadingScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    @Override
    public void create() {
        // Initialize SceneManager with this Game instance
        SceneManager.getInstance().setGame(this);

        // Initialize Physics Collision Listener
        PhysicsManager.getInstance().getWorld().setContactListener(new CollisionManager());

        // Push initial screen
        SceneManager.getInstance().pushScreen(new LoadingScreen());
    }
    
    @Override
    public void render() {
        // Delegate render to SceneManager (which peeks stack) 
        // OR standard Game.render() which calls currentScreen.render().
        // Since SceneManager sets the game screen, super.render() works fine.
        // But the prompt asked SceneManager to have a render() method.
        // To be safe and compliant:
        super.render(); 
    }
}
