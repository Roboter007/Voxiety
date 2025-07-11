package de.Roboter007.voxiety.core.listeners;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {

    private static MouseListener voxMouseListener = null;
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    private MouseListener() {
        this.scrollX = 0;
        this.scrollY = 0;
        this.xPos = 0;
        this.yPos = 0;
        this.lastX = 0;
        this.lastY = 0;
    }

    public static MouseListener access() {
        if(voxMouseListener == null) {
            voxMouseListener = new MouseListener();
        }
        return voxMouseListener;
    }

    public static void mousePosCallback(long window, double xPos, double yPos) {
        access().lastX = access().xPos;
        access().lastY = access().yPos;
        access().xPos = xPos;
        access().yPos = yPos;
        access().isDragging = access().mouseButtonPressed[0] || access().mouseButtonPressed[1] || access().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if(action  == GLFW_PRESS) {
            if(button < access().mouseButtonPressed.length) {
                access().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if(button < access().mouseButtonPressed.length) {
                access().mouseButtonPressed[button] = false;
                access().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        access().scrollX = xOffset;
        access().scrollY = yOffset;
    }

    public void endFrame() {
        access().scrollX = 0;
        access().scrollY = 0;
        access().lastX = access().xPos;
        access().lastY = access().yPos;
    }

    public static float x() {
        return (float) access().xPos;
    }

    public static float y() {
        return (float) access().yPos;
    }

    public static float dx() {
        return (float) (access().lastX - access().xPos);
    }

    public static float dy() {
        return (float) (access().lastY - access().yPos);
    }

    public static float scrollX() {
        return (float) access().scrollX;
    }

    public static float scrollY() {
        return (float) access().scrollY;
    }

    public static boolean isDragging() {
        return access().isDragging;
    }

    public static boolean mouseButtonDown(int button) {
        if(button < access().mouseButtonPressed.length) {
            return access().mouseButtonPressed[button];
        } else {
            return false;
        }
    }
}
