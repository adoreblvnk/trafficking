package com.sit.recyclingpinball.engine.platform.libgdx;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class PlatformInputAdapter extends InputAdapter {

    private final com.sit.recyclingpinball.engine.interfaces.IInputManager inputProcessor;
    private final PlatformDisplay display;

    public PlatformInputAdapter(com.sit.recyclingpinball.engine.interfaces.IInputManager inputProcessor, PlatformDisplay display) {
        this.inputProcessor = inputProcessor;
        this.display = display;
    }

    private PlatformKey mapKey(int keycode) {
        return switch (keycode) {
            case Keys.LEFT -> PlatformKey.LEFT;
            case Keys.RIGHT -> PlatformKey.RIGHT;
            case Keys.UP -> PlatformKey.UP;
            case Keys.DOWN -> PlatformKey.DOWN;
            case Keys.SPACE -> PlatformKey.SPACE;
            case Keys.ENTER -> PlatformKey.ENTER;
            case Keys.ESCAPE -> PlatformKey.ESCAPE;
            case Keys.A -> PlatformKey.A;
            case Keys.B -> PlatformKey.B;
            case Keys.C -> PlatformKey.C;
            case Keys.D -> PlatformKey.D;
            case Keys.E -> PlatformKey.E;
            case Keys.F -> PlatformKey.F;
            case Keys.G -> PlatformKey.G;
            case Keys.H -> PlatformKey.H;
            case Keys.I -> PlatformKey.I;
            case Keys.J -> PlatformKey.J;
            case Keys.K -> PlatformKey.K;
            case Keys.L -> PlatformKey.L;
            case Keys.M -> PlatformKey.M;
            case Keys.N -> PlatformKey.N;
            case Keys.O -> PlatformKey.O;
            case Keys.P -> PlatformKey.P;
            case Keys.Q -> PlatformKey.Q;
            case Keys.R -> PlatformKey.R;
            case Keys.S -> PlatformKey.S;
            case Keys.T -> PlatformKey.T;
            case Keys.U -> PlatformKey.U;
            case Keys.V -> PlatformKey.V;
            case Keys.W -> PlatformKey.W;
            case Keys.X -> PlatformKey.X;
            case Keys.Y -> PlatformKey.Y;
            case Keys.Z -> PlatformKey.Z;
            case Keys.NUM_0 -> PlatformKey.NUM_0;
            case Keys.NUM_1 -> PlatformKey.NUM_1;
            case Keys.NUM_2 -> PlatformKey.NUM_2;
            case Keys.NUM_3 -> PlatformKey.NUM_3;
            case Keys.NUM_4 -> PlatformKey.NUM_4;
            case Keys.NUM_5 -> PlatformKey.NUM_5;
            case Keys.NUM_6 -> PlatformKey.NUM_6;
            case Keys.NUM_7 -> PlatformKey.NUM_7;
            case Keys.NUM_8 -> PlatformKey.NUM_8;
            case Keys.NUM_9 -> PlatformKey.NUM_9;
            case Keys.ANY_KEY -> PlatformKey.ANY_KEY;
            default -> PlatformKey.UNKNOWN;
        };
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int worldY = display.getHeight() - screenY;
        return inputProcessor != null && inputProcessor.touchDown(screenX, worldY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        int worldY = display.getHeight() - screenY;
        return inputProcessor != null && inputProcessor.touchDragged(screenX, worldY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        int worldY = display.getHeight() - screenY;
        return inputProcessor != null && inputProcessor.touchUp(screenX, worldY, pointer, button);
    }

    @Override
    public boolean keyDown(int keycode) {
        return inputProcessor != null && inputProcessor.keyDown(mapKey(keycode));
    }

    @Override
    public boolean keyUp(int keycode) {
        return inputProcessor != null && inputProcessor.keyUp(mapKey(keycode));
    }
}
