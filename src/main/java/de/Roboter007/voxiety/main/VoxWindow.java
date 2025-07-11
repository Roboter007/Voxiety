package de.Roboter007.voxiety.main;

import de.Roboter007.voxiety.Voxiety;
import de.Roboter007.voxiety.core.cameras.Camera;
import de.Roboter007.voxiety.core.listeners.KeyListener;
import de.Roboter007.voxiety.core.listeners.MouseListener;
import de.Roboter007.voxiety.core.renderer.VoxRenderer;
import de.Roboter007.voxiety.main.frames.Frame;
import de.Roboter007.voxiety.main.frames.GameFrame;
import de.Roboter007.voxiety.main.frames.MainFrame;
import de.Roboter007.voxiety.utils.VoxAssets;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;
import java.util.concurrent.Callable;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class VoxWindow {

    private static boolean loaded = false;
    protected static String WINDOW_TITLE = Voxiety.NAME + " - " + Voxiety.VERSION;
    protected static int WINDOW_HEIGHT = 1080;
    protected static int WINDOW_WIDTH = 1920;

    public float ratio() {
        return (float) WINDOW_HEIGHT / (float) WINDOW_WIDTH;
    }

    public static int tileSize() {
        return 32;
    }


    protected static long windowID;

    private static VoxWindow voxWindow = null;

    private static Frame currentFrame;

    public static VoxWindow access() {
        if(voxWindow == null) {
            voxWindow = new VoxWindow();
        }
        return voxWindow;
    }

    public void debug() {
        System.out.println("default TP: " + VoxAssets.getDefaultTP());
        System.out.println("Nap Spritesheet in default TP: " + VoxAssets.getDefaultTP().getSpritesheet("C:\\Users\\r07sc\\Desktop\\Programme\\Java\\Meine Projekte\\Voxiety\\out\\production\\resources\\voxiety\\player\\napoleon_spritesheet.png"));
    }

    public void switchToFrame(int newFrame) {
        switch (newFrame) {
            case 0:
                if(!(currentFrame instanceof MainFrame)) {
                    currentFrame = new MainFrame();
                    currentFrame.init();
                    currentFrame.start();
                }
                break;
            case 1:
                if(!(currentFrame instanceof GameFrame)) {
                    currentFrame = new GameFrame();
                    currentFrame.init();
                    currentFrame.start();
                }
                break;
            default:
                System.out.println("Unknown Frame: '" + currentFrame + "'");
                break;
        }
    }

    public static void reloadFrame() {
        if(currentFrame != null) {
            currentFrame.renderer.clearBatches();
            currentFrame.init();
            currentFrame.start();
        }
    }

    public static Frame frame() {
        access();
        return currentFrame;
    }

    public void run() {
        System.out.println("LWJGL Version: " + Version.getVersion());

        init();
        loop();
        destroy();
    }

    protected void init() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        //glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        windowID = GLFW.glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, 0, 0);
        if (windowID == 0) {
            throw new RuntimeException("Failed to create the window!");
        }
        //Icon
        //glfwSetWindowIcon(window, );


        /*try (MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(windowID, pWidth, pHeight);

            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            if(vidMode != null) {
                glfwSetWindowPos(
                        windowID,
                        (vidMode.width() - pWidth.get(0)) / 2,
                        (vidMode.height() - pHeight.get(0)) / 2
                );
            }
        } */


        glfwSetCursorPosCallback(windowID, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(windowID, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(windowID, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(windowID, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(windowID, (_, newWidth, newHeight) -> {
            VoxWindow.setWidth(newWidth);
            VoxWindow.setHeight(newHeight);

            Camera.TARGETED_WINDOW_WIDTH = newWidth;
            Camera.TARGETED_WINDOW_HEIGHT = newHeight;

            GameFrame.player.updateSprite();
        });


        glfwMakeContextCurrent(windowID);

        //V-Sync
        glfwSwapInterval(1);

        glfwShowWindow(windowID);

        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        if(!loaded) {
            VoxAssets.reloadTexturePacks();
            loaded = true;
        }
        VoxWindow.access().switchToFrame(1);
    }

    protected void loop() {
        float beginnTime = (float) glfwGetTime();
        float endTime;
        float delta = -1.0f;
        while (!GLFW.glfwWindowShouldClose(windowID)) {
            GLFW.glfwPollEvents();

            GL11.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            if(delta >= 0) {
                currentFrame.update(delta);
            }

//            if(KeyListener.keyPressed(GLFW_KEY_C)) {
//                VoxWindow.access().switchToFrame(1);
//            }
//
//            if(KeyListener.keyPressed(GLFW_KEY_V)) {
//                VoxWindow.access().switchToFrame(0);
//            }
//
//            if(KeyListener.keyPressed(GLFW_KEY_R)) {
//                VoxAssets.reloadTexturePacks();
//                //currentFrame.reloadFrame();
//            }

            GLFW.glfwSwapBuffers(windowID);

            endTime = (float) glfwGetTime();
            delta = endTime - beginnTime;
            beginnTime = endTime;
        }
    }

    protected void destroy() {
        glfwFreeCallbacks(windowID);
        GLFW.glfwDestroyWindow(windowID);

        GLFW.glfwTerminate();
        GLFWErrorCallback glfwErrorCallback = glfwSetErrorCallback(null);
        if(glfwErrorCallback != null) {
            glfwErrorCallback.free();
        }
    }

    public void reload() {
        init();
    }

    public static void setWidth(int width) {
        WINDOW_WIDTH = width;
    }

    public static void setHeight(int height) {
        WINDOW_HEIGHT = height;
    }

    public static int windowWidth() {
        return WINDOW_WIDTH;
    }

    public static int windowHeight() {
        return WINDOW_HEIGHT;
    }

}
