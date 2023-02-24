package sapphire;

import static org.lwjgl.glfw.GLFW.*;

import diamondEngine.diaUtils.DiaConsole;
import diamondEngine.Game;
import diamondEngine.Window;

public class ContainerEditor {

    private final Window window;
    private boolean running;
    private boolean editorPlaying = false;
    private Game game;

    // CONSTRUCTORS
    public ContainerEditor() {
        this.running = false;
        this.window = Window.get();
        this.game = null;
    }

    // GETTERS AND SETTERS
    private Window getDisplay() {
        return window;
    }

    public boolean isRunning() {
        return running;
    }

    // METHODS
    public void init() {
        DiaConsole.log("Initializing Editor Container...");
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
        float dt = 0f;
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
