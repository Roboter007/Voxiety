package de.Roboter007.voxiety.core.keys;

import org.jetbrains.annotations.Nullable;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyBind {

    private final Type type;
    private final int defaultKey;
    private int keyBind;
    @Nullable
    private final Runnable keyAction;
    private boolean pressed;


    public KeyBind(Type type, int defaultKey) {
        this(type, defaultKey, null);
    }

    public KeyBind(Type type, int defaultKey, @Nullable Runnable keyAction) {
        this.type = type;
        this.defaultKey = defaultKey;
        this.keyBind = defaultKey;
        this.keyAction = keyAction;
        KeyBinds.registerKeyBind(this);
    }

    @Nullable
    public Runnable getKeyAction() {
        return keyAction;
    }

    public void runAction(int action) {
        if(this.type == Type.ONE_USE) {
            if (action == GLFW_PRESS) {
                if(this.keyAction != null) {
                    this.keyAction.run();
                }
            }
        } else if (type == Type.TOGGLE) {
            if (action == GLFW_PRESS) {
                pressed = true;
            } else if (action == GLFW_RELEASE) {
                pressed = false;
            }
        }
    }

    public boolean isPressed() {
        return pressed;
    }

    public Type getType() {
        return type;
    }

    public int getDefault() {
        return defaultKey;
    }

    public int getKeyBind() {
        return keyBind;
    }

    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }

    public enum Type {
        TOGGLE(),
        ONE_USE();
    }
}
