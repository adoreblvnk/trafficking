package com.sit.recyclingpinball.engine.platform.libgdx;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.sit.recyclingpinball.engine.interfaces.providers.EngineKey;
import com.sit.recyclingpinball.engine.interfaces.IInputManager;
import com.sit.recyclingpinball.engine.interfaces.providers.IDisplay;

/**
 * libGDX input bridge that forwards framework callbacks to the pure Java
 * IInputManager. Performs Y-flip to align screen coordinates with bottom-up
 * world coordinates.
 */
public class LibGdxInputAdapter extends InputAdapter {

    private final IInputManager inputManager;
    private final IDisplay display;

    public LibGdxInputAdapter(IInputManager inputManager, IDisplay display) {
        this.inputManager = inputManager;
        this.display = display;
    }

    private EngineKey mapKey(int keycode) {
        return switch (keycode) {
            case Keys.LEFT -> EngineKey.LEFT;
            case Keys.RIGHT -> EngineKey.RIGHT;
            case Keys.UP -> EngineKey.UP;
            case Keys.DOWN -> EngineKey.DOWN;
            case Keys.SPACE -> EngineKey.SPACE;
            case Keys.ENTER -> EngineKey.ENTER;
            case Keys.ESCAPE -> EngineKey.ESCAPE;
            case Keys.A -> EngineKey.A;
            case Keys.B -> EngineKey.B;
            case Keys.C -> EngineKey.C;
            case Keys.D -> EngineKey.D;
            case Keys.E -> EngineKey.E;
            case Keys.F -> EngineKey.F;
            case Keys.G -> EngineKey.G;
            case Keys.H -> EngineKey.H;
            case Keys.I -> EngineKey.I;
            case Keys.J -> EngineKey.J;
            case Keys.K -> EngineKey.K;
            case Keys.L -> EngineKey.L;
            case Keys.M -> EngineKey.M;
            case Keys.N -> EngineKey.N;
            case Keys.O -> EngineKey.O;
            case Keys.P -> EngineKey.P;
            case Keys.Q -> EngineKey.Q;
            case Keys.R -> EngineKey.R;
            case Keys.S -> EngineKey.S;
            case Keys.T -> EngineKey.T;
            case Keys.U -> EngineKey.U;
            case Keys.V -> EngineKey.V;
            case Keys.W -> EngineKey.W;
            case Keys.X -> EngineKey.X;
            case Keys.Y -> EngineKey.Y;
            case Keys.Z -> EngineKey.Z;
            case Keys.NUM_0 -> EngineKey.NUM_0;
            case Keys.NUM_1 -> EngineKey.NUM_1;
            case Keys.NUM_2 -> EngineKey.NUM_2;
            case Keys.NUM_3 -> EngineKey.NUM_3;
            case Keys.NUM_4 -> EngineKey.NUM_4;
            case Keys.NUM_5 -> EngineKey.NUM_5;
            case Keys.NUM_6 -> EngineKey.NUM_6;
            case Keys.NUM_7 -> EngineKey.NUM_7;
            case Keys.NUM_8 -> EngineKey.NUM_8;
            case Keys.NUM_9 -> EngineKey.NUM_9;
            case Keys.ANY_KEY -> EngineKey.ANY_KEY;
            default -> EngineKey.UNKNOWN;
        };
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int worldY = display.getHeight() - screenY;
        return inputManager != null && inputManager.touchDown(screenX, worldY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        int worldY = display.getHeight() - screenY;
        return inputManager != null && inputManager.touchDragged(screenX, worldY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        int worldY = display.getHeight() - screenY;
        return inputManager != null && inputManager.touchUp(screenX, worldY, pointer, button);
    }

    @Override
    public boolean keyDown(int keycode) {
        return inputManager != null && inputManager.keyDown(mapKey(keycode));
    }

    @Override
    public boolean keyUp(int keycode) {
        return inputManager != null && inputManager.keyUp(mapKey(keycode));
    }
}
