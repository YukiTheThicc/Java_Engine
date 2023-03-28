package diamondEngine;

import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaUtils;
import sapphire.Sapphire;
import sapphire.imgui.SappImGUILayer;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Diamond {

    // ATTRIBUTES
    private static Diamond diamond = null;
    private ArrayList<DiaEnvironment> environments;
    private final Window window;
    private boolean running;
    private float dt;

    // CONSTRUCTORS
    private Diamond() {
        this.window = Window.get();
        this.environments = new ArrayList<>();
        this.running = false;
        this.dt = 0.0f;
    }

    // GETTERS & SETTERS
    public ArrayList<DiaEnvironment> getEnvironments() {
        return environments;
    }

    public void setEnvironments(ArrayList<DiaEnvironment> environments) {
        this.environments = environments;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public float getDt() {
        return dt;
    }

    public void setDt(float dt) {
        this.dt = dt;
    }

    // METHODS
    public static Diamond get() {
        if (Diamond.diamond == null) {
            Diamond.diamond = new Diamond();
        }
        return diamond;
    }

    private boolean shouldClose() {
        return glfwWindowShouldClose(this.window.getGlfwWindow());
    }

    public void launch() {
        // Initialize general front end an Engine
        DiaLogger.init();
        DiaUtils.init();
        window.init("Diamond", "res/icon.png");
        run();
        DiaLogger.close();
    }

    public void run() {
        this.running = true;
        float bt = (float) glfwGetTime();
        float et;
        while (this.running) {
            this.window.pollEvents();
            this.window.startFrame();
            /*if (dt >= 0) {

            }*/
            this.window.endFrame();
            et = (float) glfwGetTime();
            dt = et - bt;
            bt = et;
            this.running = !this.shouldClose();
        }
    }
}
