package diamondEngine;

import diamondEngine.diaUtils.DiaUtils;
import org.lwjgl.glfw.GLFWImage;
import diamondEngine.diaRenderer.Framebuffer;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.GL;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    // ATTRIBUTES
    private int width, height;
    private IntBuffer posX, posY;
    private String title;
    private long glfwWindow;
    private static Window window = null;
    private long audioContext;
    private long audioDevice;
    private Framebuffer framebuffer;

    // CONSTRUCTORS
    private Window() {
        this.width = 1600;
        this.height = 900;
        stackPush();
        this.posX = stackCallocInt(1);
        stackPush();
        this.posY = stackCallocInt(1);
        this.title = "DiamondEngine v0.0.0.1";
        stackPop();
        stackPop();
    }

    // GETTERS & SETTERS
    public static int getWidth() {
        return get().width;
    }

    public static int getHeight() {
        return get().height;
    }

    public String getTitle() {
        return title;
    }

    public long getGlfwWindow() {
        return glfwWindow;
    }

    public static float getTargetAspectRatio() {
        return 16.0f / 9.0f;
    }

    public static void setWidth(int width) {
        get().width = width;
    }

    public static void setHeight(int height) {
        get().height = height;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static Vector2f getPosition() {
        Window window = get();
        Vector2f pos = new Vector2f();
        org.lwjgl.glfw.GLFW.glfwGetWindowPos(window.glfwWindow, window.posX, window.posY);
        pos.x = window.posX.get(0);
        pos.y = window.posY.get(0);
        return pos;
    }

    public static Framebuffer getFramebuffer() {
        return get().framebuffer;
    }

    // METHODS
    public void resizeCallback() {
        System.out.println("Resizing");
    }

    public void init(String title, String iconPath) {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }

        // Set default window state
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        // Create the window itself
        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create GLFW window");
        }

        // TODO: Setup window control callbacksglfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        //        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        //        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        //        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        glfwSetWindowSizeCallback(glfwWindow, (w, newX, newY) -> {
            Window.setWidth(newX);
            Window.setHeight(newY);
        });

        // Setup audio context
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = alcOpenDevice(defaultDeviceName);
        int[] attributes = {0};
        audioContext = alcCreateContext(audioDevice, attributes);
        alcMakeContextCurrent(audioContext);
        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        assert alCapabilities.OpenAL10 : "Audio library not supported";

        // Setup context and show window
        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);
        GL.createCapabilities();
        glDisable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        glfwShowWindow(glfwWindow);

        /*
         * For now there is only one framebuffer and it is attached to the window itself.
         * TODO: find a better solution so the framebuffer/s are detached from the window and multiple can exist at any
         *  given time.
         */
        this.framebuffer = new Framebuffer(this.width, this.height);
        glViewport((int)getPosition().x, (int)getPosition().y, width, height);

        GLFWImage.Buffer image = DiaUtils.loadGLFWImage(iconPath);
        if (image != null) {
            glfwSetWindowIcon(glfwWindow, image);
            image.close();
        }
    }

    public static Window get() {
        if (window == null) {
            window = new Window();
        }
        return Window.window;
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    public void startFrame() {
        this.framebuffer.bind();
        glClearColor(0.1f, 0.1f, 0.1f, 1f);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void endFrame() {
        this.framebuffer.unBind();
        glfwSwapBuffers(glfwWindow);
    }

    public void close() {
        // Free allocated memory
        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevice);
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        posX = null;
        posY = null;

        // Termination of GLFW
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
