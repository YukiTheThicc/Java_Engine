package sapphire;

import diamondEngine.Window;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaUtils;
import sapphire.imgui.SappImGUILayer;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class Sapphire {

    /**
     * Class that manages and contains all FrontEnd/Editor functionalities and resources
     */
    // ATTRIBUTES
    private static Sapphire sapphire = null;
    private final Window window;
    private final SappImGUILayer imGUILayer;
    private boolean running;
    private float dt;
    private SapphireSettings settings;

    // CONSTRUCTORS
    private Sapphire() {
        this.window = Window.get();
        this.window.init();
        this.settings = new SapphireSettings();
        this.imGUILayer = new SappImGUILayer(this.window.getGlfwWindow());
        this.running = false;
        this.dt = 0f;
    }

    // GETTERS & SETTERS
    public SapphireSettings getSettings() {
        return settings;
    }

    public static String getLiteral(String literal) {
        return Sapphire.get().settings.getLiteral(literal);
    }

    public SappImGUILayer getImGUILayer() {
        return imGUILayer;
    }

    public float getDt() {
        return dt;
    }

    // METHODS
    public static Sapphire get() {
        if (Sapphire.sapphire == null) {
            Sapphire.sapphire = new Sapphire();
        }
        return sapphire;
    }

    private boolean shouldClose() {
        return glfwWindowShouldClose(this.window.getGlfwWindow());
    }

    public void start() {
        // Initialize general front end an Engine
        DiaLogger.init();
        DiaUtils.init();
        settings.init();
        imGUILayer.init();
        this.run(imGUILayer);
        DiaLogger.close();
        imGUILayer.destroyImGui();
    }

    public void run(SappImGUILayer layer) {
        this.running = true;
        float bt = (float) glfwGetTime();
        float et = 0;
        while (this.running) {
            this.window.pollEvents();
            this.window.startFrame();
            /*if (dt >= 0) {
            }*/
            this.window.endFrame();
            layer.update();
            et = (float) glfwGetTime();
            dt = et - bt;
            bt = et;
            this.running = !this.shouldClose();
        }
    }
}