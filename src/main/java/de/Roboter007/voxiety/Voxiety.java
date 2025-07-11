package de.Roboter007.voxiety;

import de.Roboter007.voxiety.api.test.PerformanceTester;
import de.Roboter007.voxiety.core.keys.KeyBinds;
import de.Roboter007.voxiety.main.VoxWindow;
import de.Roboter007.voxiety.main.world.blocks.Blocks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.ForkJoinPool.defaultForkJoinWorkerThreadFactory;

public class Voxiety {

    public static String NAME = "Voxiety";
    public static String ID = NAME.toLowerCase();
    public static String VERSION = "0.0.1";
    public static ExecutorService SERVICE = new ForkJoinPool(Math.min(0x7fff, Runtime.getRuntime().availableProcessors()),
            defaultForkJoinWorkerThreadFactory, null, true,
            2, 0x7fff, 1, null, 60_000L, TimeUnit.MILLISECONDS);
    public static PerformanceTester VOX_PERF_TESTER = new PerformanceTester();



    public static void main(String[] args) {
        Blocks.load();
        System.out.println("Loaded Blocks");
        initOpenGL();
        KeyBinds.load();
        System.out.println("Loaded KeyBinds");
        VoxWindow.access().run();
    }

    public static void initOpenGL() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Failed to initialize OpenGL!");
        }
    }

}