package sapphire;

import static org.lwjgl.glfw.GLFW.*;

import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.Game;
import diamondEngine.Window;

public class ContainerEditor {

    private final Window window;
    private boolean running;
    private boolean editorPlaying = false;
    private float dt;
    private Game game;

    // CONSTRUCTORS
    public ContainerEditor() {
        this.running = false;
        this.window = Window.get();
        this.game = null;
        this.dt = 0f;
    }

    // GETTERS AND SETTERS
    private Window getDisplay() {
        return window;
    }

    public boolean isRunning() {
        return running;
    }

    public float getDt() {
        return dt;
    }

    // METHODS
    public void init() {
        DiaLogger.log("Initializing Editor Container...");
        this.window.init(true);
    }

    public void stopEditor() {
        this.editorPlaying = false;
    }

    private void dispose() {
        this.getDisplay().close();
    }

    private boolean shouldClose() {
        return glfwWindowShouldClose(this.window.getGlfwWindow());
    }

    public void run() {
        this.running = true;
        float bt = (float) glfwGetTime();
        float et = (float) glfwGetTime();
        while (this.running) {
            this.window.pollEvents();
            this.window.startFrame();
            if (dt >= 0) {
                if (this.game != null) {
                }
            }
            this.window.endFrame();
            et = (float) glfwGetTime();
            dt = et - bt;
            bt = et;
            this.running = !this.shouldClose();
        }
        this.dispose();
    }

}
