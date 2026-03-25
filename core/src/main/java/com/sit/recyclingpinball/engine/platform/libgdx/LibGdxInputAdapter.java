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
        switch (keycode) {
            case Keys.LEFT :
                return EngineKey.LEFT;
            case Keys.RIGHT :
                return EngineKey.RIGHT;
            case Keys.UP :
                return EngineKey.UP;
            case Keys.DOWN :
                return EngineKey.DOWN;
            case Keys.SPACE :
                return EngineKey.SPACE;
            case Keys.ENTER :
                return EngineKey.ENTER;
            case Keys.ESCAPE :
                return EngineKey.ESCAPE;
            case Keys.A :
                return EngineKey.A;
            case Keys.B :
                return EngineKey.B;
            case Keys.C :
                return EngineKey.C;
            case Keys.D :
                return EngineKey.D;
            case Keys.E :
                return EngineKey.E;
            case Keys.F :
                return EngineKey.F;
            case Keys.G :
                return EngineKey.G;
            case Keys.H :
                return EngineKey.H;
            case Keys.I :
                return EngineKey.I;
            case Keys.J :
                return EngineKey.J;
            case Keys.K :
                return EngineKey.K;
            case Keys.L :
                return EngineKey.L;
            case Keys.M :
                return EngineKey.M;
            case Keys.N :
                return EngineKey.N;
            case Keys.O :
                return EngineKey.O;
            case Keys.P :
                return EngineKey.P;
            case Keys.Q :
                return EngineKey.Q;
            case Keys.R :
                return EngineKey.R;
            case Keys.S :
                return EngineKey.S;
            case Keys.T :
                return EngineKey.T;
            case Keys.U :
                return EngineKey.U;
            case Keys.V :
                return EngineKey.V;
            case Keys.W :
                return EngineKey.W;
            case Keys.X :
                return EngineKey.X;
            case Keys.Y :
                return EngineKey.Y;
            case Keys.Z :
                return EngineKey.Z;
            case Keys.NUM_0 :
                return EngineKey.NUM_0;
            case Keys.NUM_1 :
                return EngineKey.NUM_1;
            case Keys.NUM_2 :
                return EngineKey.NUM_2;
            case Keys.NUM_3 :
                return EngineKey.NUM_3;
            case Keys.NUM_4 :
                return EngineKey.NUM_4;
            case Keys.NUM_5 :
                return EngineKey.NUM_5;
            case Keys.NUM_6 :
                return EngineKey.NUM_6;
            case Keys.NUM_7 :
                return EngineKey.NUM_7;
            case Keys.NUM_8 :
                return EngineKey.NUM_8;
            case Keys.NUM_9 :
                return EngineKey.NUM_9;
            case Keys.ANY_KEY :
                return EngineKey.ANY_KEY;
            default :
                return EngineKey.UNKNOWN;
        }
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
