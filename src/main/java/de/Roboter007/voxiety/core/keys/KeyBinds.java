package de.Roboter007.voxiety.core.keys;

import de.Roboter007.voxiety.main.VoxWindow;
import de.Roboter007.voxiety.utils.VoxAssets;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class KeyBinds {

    public static final List<KeyBind> KEY_BINDS = new ArrayList<>(350);

    public static void registerKeyBind(KeyBind keyBind) {
        KEY_BINDS.add(keyBind);
    }

//    public static KeyBind getKeyBind(int defaultKey) {
//        return KEY_BINDS.get(defaultKey);
//    }

//    public static void unregisterKeyBind(KeyBind keyBind) {
//        KEY_BINDS.remove(keyBind.getDefault());
//    }

    public static void load() {}


    // Walk
    public static KeyBind WALK_FORWARDS = new KeyBind(KeyBind.Type.TOGGLE, GLFW_KEY_W);
    public static KeyBind WALK_LEFT = new KeyBind(KeyBind.Type.TOGGLE, GLFW_KEY_A);
    public static KeyBind WALK_BACKWARDS = new KeyBind(KeyBind.Type.TOGGLE, GLFW_KEY_S);
    public static KeyBind WALK_RIGHT = new KeyBind(KeyBind.Type.TOGGLE, GLFW_KEY_D);

    public static KeyBind SWITCH_TO_MAIN_MENU = new KeyBind(KeyBind.Type.ONE_USE, GLFW_KEY_C, () -> VoxWindow.access().switchToFrame(0));
    public static KeyBind SWITCH_TO_GAME_MENU = new KeyBind(KeyBind.Type.ONE_USE, GLFW_KEY_V, () -> VoxWindow.access().switchToFrame(1));

    public static KeyBind RELOAD_TEXTURES = new KeyBind(KeyBind.Type.ONE_USE, GLFW_KEY_R, VoxAssets::reloadTexturePacks);
    public static KeyBind DEBUG = new KeyBind(KeyBind.Type.ONE_USE, GLFW_KEY_E, () -> VoxWindow.access().debug());
}
