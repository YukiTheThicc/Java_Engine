package sapphire.imgui;

import sapphire.Sapphire;
import sapphire.imgui.windows.*;
import diamondEngine.Window;
import diamondEngine.diaUtils.DiaConsole;
import imgui.*;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImBoolean;
import imgui.internal.ImGui;
import org.joml.Vector2f;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

public class ImGUILayer {

    private long glfwWindow;
    // LWJGL3 renderer (SHOULD be initialized)
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private MenuBar menuBar;
    private HashMap<String, ImguiWindow> windows;
    private GameViewWindow gameView;
    private int dockId;

    // CONSTRUCTORS
    public ImGUILayer(long windowPtr) {
        this.glfwWindow = windowPtr;
        this.menuBar = new MenuBar();
        this.windows = new HashMap<>();
        this.dockId = -1;
    }

    // GETTERS & SETTERS
    public HashMap<String, ImguiWindow> getWindows() {
        return windows;
    }

    public void addWindow(ImguiWindow window) {
        if (window != null) {
            this.windows.put(window.getId(), window);
        }
    }

    public int getDockId() {
        return dockId;
    }

    // METHODS
    public void init() {

        DiaConsole.log("Initializing ImGUI...", "debug");
        ImGui.createContext();
        final ImGuiIO io = ImGui.getIO();

        try {
            Files.createDirectories(Paths.get("sapphire"));
            io.setIniFilename("sapphire/imgui.ini");
        } catch (IOException e) {
            DiaConsole.log("Failed to set ImGui ini file", "error");
        }
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        io.setBackendPlatformName("imgui_java_impl_glfw");

        // GLFW callbacks to handle user input
        glfwSetKeyCallback(glfwWindow, (w, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                io.setKeysDown(key, true);
            } else if (action == GLFW_RELEASE) {
                io.setKeysDown(key, false);
            }

            io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
            io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
            io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
            io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));

            if (!io.getWantCaptureKeyboard()) {
                //KeyListener.keyCallback(w, key, scancode, action, mods);
            }
        });

        glfwSetCharCallback(glfwWindow, (w, c) -> {
            if (c != GLFW_KEY_DELETE) {
                io.addInputCharacter(c);
            }
        });

        glfwSetMouseButtonCallback(glfwWindow, (w, button, action, mods) -> {
            final boolean[] mouseDown = new boolean[5];

            mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
            mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
            mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
            mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
            mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

            io.setMouseDown(mouseDown);

            if (!io.getWantCaptureMouse() && mouseDown[1]) {
                ImGui.setWindowFocus(null);
            }
            /*
            if (!io.getWantCaptureMouse() || viewPortWindow.getWantCaptureMouse()) {
                MouseListener.mouseButtonCallback(w, button, action, mods);
            }*/
        });

        glfwSetScrollCallback(glfwWindow, (w, xOffset, yOffset) -> {
            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);

            /*
            if (!io.getWantCaptureMouse() || viewPortWindow.getWantCaptureMouse()) {
                MouseListener.mouseScrollCallback(w, xOffset, yOffset);
            }*/
        });

        io.setSetClipboardTextFn(new ImStrConsumer() {
            @Override
            public void accept(final String s) {
                glfwSetClipboardString(glfwWindow, s);
            }
        });

        io.setGetClipboardTextFn(new ImStrSupplier() {
            @Override
            public String get() {
                final String clipboardString = glfwGetClipboardString(glfwWindow);
                if (clipboardString != null) {
                    return clipboardString;
                } else {
                    return "";
                }
            }
        });

        String fontDir = Sapphire.get().getSettings().getFont();
        if (new File(fontDir).isFile()) {
            final ImFontAtlas fontAtlas = io.getFonts();
            final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

            // Glyphs could be added per-font as well as per config used globally like here
            fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());

            // Fonts merge example
            fontConfig.setPixelSnapH(true);
            fontAtlas.addFontFromFileTTF(fontDir, 12, fontConfig);
            fontConfig.destroy(); // After all fonts were added we don't need this config more
        } else {
            DiaConsole.log("Specified engine font has not been found: \"" + fontDir + "\"", "error");
        }

        // For now windows are going to be statically added in the init function
        // WINDOWS
        gameView = new GameViewWindow();
        ImguiWindow newWindow = new SettingsWindow();
        addWindow(newWindow);
        newWindow = new EntityPropertiesWindow();
        addWindow(newWindow);
        newWindow = new AssetsWindow();
        addWindow(newWindow);
        newWindow = new EnvHierarchyWindow();
        addWindow(newWindow);

        /* Settings for the windows are loaded. At the time of writing this code, only one setting is stored, being if
         * the window is active or not. Because of this, the window settings are stored as a simple map. When it comes
         * to runtime, the windows are stored in memory as an ArrayList. For the moment then, when initializing the
         * front end the settings map will be iterated through and the windows names will be compared with the key of
         * each entry, then set it to active or not appropriately. Could be necessary in the future to fully serialize
         * the windows. With this solution its also assured that all windows available will be created no matter if it
         * is registered or not on the settings.
         */
        for (String windowId : Sapphire.get().getSettings().getActiveWindows().keySet()) {
            for (String window : windows.keySet()) {
                if (windows.get(window).getId().equals(windowId)) {
                    windows.get(window).setActive(Sapphire.get().getSettings().getActiveWindows().get(windowId));
                }
            }
        }

        /*
         * Method initializes LWJGL3 renderer.
         * This method SHOULD be called after initializing ImGui configuration.
         */
        imGuiGlfw.init(glfwWindow, true);
        imGuiGl3.init("#version 330 core");
    }

    public void removeWindow(String id) {
        windows.remove(id);
    }

    private void startFrame() {
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    public void update() {
        startFrame();
        setupDockSpace();
        this.gameView.imgui(this);
        for (String window : windows.keySet()) {
            if ((windows.get(window) instanceof FileWindow) || windows.get(window).isActive().get()) {
                windows.get(window).imgui(this);
            }
        }
        updateFileWindows();
        endFrame();
    }

    private void updateFileWindows() {
        // Check for new opened files on the front end and update the file windows accordingly
        // TODO: Consider changing ArrayList for HashMap to avoid nested for loops
        if (Sapphire.get().hasUpdatedFiles()) {

            ArrayList<FileWindow> windowsToAdd = new ArrayList<>();
            for (File file : Sapphire.get().getOpenedFiles()) {
                if (windows.get(file.getName()) == null) {
                    windowsToAdd.add(new FileWindow(file.getName(), file));
                }
            }

            for (FileWindow fileWindow : windowsToAdd) {
                windows.put(fileWindow.getId(), fileWindow);
            }

            Sapphire.get().filesUpdated();
        }
    }

    private void endFrame() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glClearColor(1f, 1f, 1f, 1f);
        glClear(GL_COLOR_BUFFER_BIT);
        // After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
        // At that moment ImGui will be rendered to the current OpenGL context.
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        long backupWindowPtr = glfwGetCurrentContext();
        ImGui.updatePlatformWindows();
        ImGui.renderPlatformWindowsDefault();
        glfwMakeContextCurrent(backupWindowPtr);
    }

    private void destroyImGui() {
        imGuiGl3.dispose();
        ImGui.destroyContext();
    }

    private void setupDockSpace() {
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoTitleBar |
                ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGuiViewport mainViewport = ImGui.getMainViewport();
        ImGui.setNextWindowPos(mainViewport.getWorkPosX(), mainViewport.getWorkPosY());
        ImGui.setNextWindowSize(mainViewport.getWorkSizeX() * 2, mainViewport.getWorkSizeY());
        ImGui.setNextWindowViewport(mainViewport.getID());
        Vector2f windowPos = Window.getPosition();
        ImGui.setNextWindowPos(windowPos.x, windowPos.y);
        ImGui.setNextWindowSize(Window.getWidth(), Window.getHeight());

        // ImGui Styles
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.2f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.2f);
        ImGui.begin("Dockspace Outer", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);

        // Dockspace
        dockId = ImGui.dockSpace(ImGui.getID("Dockspace"));
        imgui.ImGui.setNextWindowDockID(dockId);
        menuBar.imgui(this);

        ImGui.end();
    }
}
