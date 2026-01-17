package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import java.util.Stack;

public final class SceneManager {
    private static SceneManager instance;
    private final Stack<Screen> screens;
    private Game game;

    private SceneManager() {
        this.screens = new Stack<>();
    }

    public static synchronized SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void pushScreen(Screen screen) {
        screens.push(screen);
        if (game != null) {
            game.setScreen(screen);
        }
    }

    public void popScreen() {
        if (!screens.isEmpty()) {
            Screen popped = screens.pop();
            popped.dispose();
        }
        
        if (!screens.isEmpty()) {
            game.setScreen(screens.peek());
        } else {
             Gdx.app.exit();
        }
    }
    
    // Optional manual render delegation if we weren't using Game.setScreen fully,
    // but using Game.setScreen allows LibGDX to handle resize/pause/resume events correctly.
    // However, for the "render(dt)" requirement peeking at stack:
    public void render(float dt) {
        if (!screens.isEmpty()) {
            screens.peek().render(dt);
        }
    }
}
