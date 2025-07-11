package de.Roboter007.voxiety.core.listeners;

import de.Roboter007.voxiety.core.keys.KeyBind;
import de.Roboter007.voxiety.core.keys.KeyBinds;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {

    private static KeyListener voxKeyListener;
//    private boolean[] keyPressed = new boolean[350];

    private KeyListener() {
    }

    public static KeyListener access() {
        if(voxKeyListener == null) {
            voxKeyListener = new KeyListener();
        }
        return voxKeyListener;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
//        if(key <  access().keyPressed.length) {
//            if (action == GLFW_PRESS) {
//                access().keyPressed[key] = true;
//            } else if (action == GLFW_RELEASE) {
//                access().keyPressed[key] = false;
//            }
//        }
        for(KeyBind keyBind : KeyBinds.KEY_BINDS) {
            if(key == keyBind.getKeyBind()) {
                keyBind.runAction(action);
                 //ToDo: System vervollständigen + den Part davor, wenn möglich entfernen
            }
        }
    }

//    public static boolean keyPressed(int keyCode) {
//        if(keyCode <  access().keyPressed.length) {
//            return access().keyPressed[keyCode];
//        } else {
//            return false;
//        }
//    }
}
