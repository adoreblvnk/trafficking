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

/**
 * Pause overlay scene for traffic simulation game.
 * Uses IGraphicsProvider for semi-transparent background rendering.
 */
public class PauseOverlay extends AbstractScene implements InputListener {

    private final SceneManager sceneManager;

    public PauseOverlay(IEngineContext context, SceneManager sceneManager, EntityManager entityManager, CollisionManager collisionManager, InputManager inputManager, MovementManager movementManager) {
        super(context, entityManager, collisionManager, inputManager, movementManager);
        this.sceneManager = sceneManager;
    }

    //sets up rendering tools and register for input events
    @Override
    public void create() {
        loadFont(LogicConstants.FONT_SIZE_MENU);
        getInputManager().addListener(this);
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public boolean onKeyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            sceneManager.popScene();
            return true;
        }
        return false;
    }

    //unused touch interactions for the pause screen
    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) { return false; }
    @Override
    public boolean onDrag(int x, int y, int ptr) { return false; }
    @Override
    public boolean onTouchUp(int x, int y, int ptr, int btn) { return false; }

    //renders a semi-transparent background and pause text
    @Override
    public void render() {
        float screenWidth = context.getDisplay().getWidth();
        float screenHeight = context.getDisplay().getHeight();

        context.getGraphics().enableBlend();

        context.getGraphics().beginShapes();
        context.getGraphics().setColor(0, 0, 0, LogicConstants.OVERLAY_ALPHA);
        context.getGraphics().drawRect(0, 0, screenWidth, screenHeight);
        context.getGraphics().endShapes();

        context.getGraphics().disableBlend();

        context.getGraphics().drawText("PAUSED", screenWidth / 2f - 50, screenHeight / 2f + 20);
        context.getGraphics().drawText("Press ESC to Resume", screenWidth / 2f - 100, screenHeight / 2f - 30);
    }

    //disposes of batch resources and call super disposal
    @Override
    public void dispose() {
        super.dispose();
    }
}
