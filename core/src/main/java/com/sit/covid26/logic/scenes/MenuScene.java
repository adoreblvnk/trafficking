package com.sit.covid26.logic.scenes;

import com.badlogic.gdx.Input;
import com.sit.covid26.engine.interfaces.InputListener;
import com.sit.covid26.engine.interfaces.providers.IEngineContext;
import com.sit.covid26.engine.managers.CollisionManager;
import com.sit.covid26.engine.managers.EntityManager;
import com.sit.covid26.engine.managers.InputManager;
import com.sit.covid26.engine.managers.MovementManager;
import com.sit.covid26.engine.scenes.AbstractScene;
import com.sit.covid26.engine.scenes.SceneManager;
import com.sit.covid26.logic.LogicConstants;
import com.sit.covid26.logic.factories.SceneFactory;

/**
 * Menu scene for traffic simulation game.
 * No longer directly uses libGDX drawing - uses SpriteBatch for text rendering.
 */
public class MenuScene extends AbstractScene implements InputListener {

    private final SceneManager sceneManager;
    private final SceneFactory sceneFactory;

    public MenuScene(IEngineContext context, SceneManager sceneManager, SceneFactory sceneFactory, EntityManager entityManager, CollisionManager collisionManager, InputManager inputManager, MovementManager movementManager) {
        super(context, entityManager, collisionManager, inputManager, movementManager);
        this.sceneManager = sceneManager;
        this.sceneFactory = sceneFactory;
    }

    //initializes resources, load menu-specific fonts, and register for input events
    @Override
    public void create() {
        loadFont(LogicConstants.FONT_SIZE_MENU);
        getInputManager().addListener(this);
    }

    @Override
    public void update(float dt) {
    }

    //handles scene transitions based on keyboard input
    @Override
    public boolean onKeyDown(int keycode) {
        if (keycode == Input.Keys.ENTER) {
            sceneManager.setScene(sceneFactory.createSimulationScene());
            return true;
        }
        return false;
    }

    //required interface methods - unused for specific scene
    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) { return false; }

    @Override
    public boolean onDrag(int x, int y, int ptr) { return false; }

    @Override
    public boolean onTouchUp(int x, int y, int ptr, int btn) { return false; }

    //draws the menu title and navigation instructions to the screen
    @Override
    public void render() {
        context.getGraphics().clearScreen(0.1f, 0.1f, 0.1f);

        float centerX = context.getDisplay().getWidth() / 2f;
        float centerY = context.getDisplay().getHeight() / 2f;

        context.getGraphics().drawText("covid26 SIMULATION", centerX - 200, centerY + 50);
        context.getGraphics().drawText("Press ENTER to Start", centerX - 150, centerY - 20);
    }

    //cleans up native resources and call superclass disposal logic
    @Override
    public void dispose() {
        super.dispose();
    }
}
