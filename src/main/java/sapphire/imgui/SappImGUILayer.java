package sapphire.imgui;

import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaUtils;
import imgui.flag.ImGuiStyleVar;
import sapphire.Sapphire;
import sapphire.SapphireControls;
import sapphire.SapphireEvents;
import sapphire.SapphireSettings;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappEventType;
import sapphire.imgui.windows.*;
import diamondEngine.Window;
import diamondEngine.diaUtils.DiaLogger;
import imgui.*;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImBoolean;
import imgui.internal.ImGui;
import org.joml.Vector2f;
import sapphire.utils.SappStyles;

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

public class SappImGUILayer {

    private final long glfwWindow;
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private HashMap<String, ImguiWindow> windows;
    private ArrayList<ImguiWindow> windowsToAdd;
    private GameViewWindow gameView;
    private FileWindow lastFocusedFile;
    private int dockId;
    private boolean dirty;
    private boolean fontChanged = false;

    // CONSTRUCTORS
    public SappImGUILayer(long windowPtr) {
        this.glfwWindow = windowPtr;
        this.windows = new HashMap<>();
        this.dockId = -1;
        this.dirty = false;
        this.windowsToAdd = new ArrayList<>();
        this.lastFocusedFile = null;
    }

    // GETTERS & SETTERS
    public HashMap<String, ImguiWindow> getWindows() {
        return windows;
    }

    public int getDockId() {
        return dockId;
    }

    public FileWindow getLastFocusedFile() {
        return lastFocusedFile;
    }

    public void setLastFocusedFile(FileWindow lastFocusedFile) {
        this.lastFocusedFile = lastFocusedFile;
    }

    // METHODS
    public void init() {

        DiaLogger.log("Initializing ImGUI...");
        ImGui.createContext();
        final ImGuiIO io = ImGui.getIO();

        try {
            Files.createDirectories(Paths.get("sapphire"));
            io.setIniFilename("sapphire/imgui.ini");
        } catch (IOException e) {
            DiaLogger.log("Failed to set ImGui ini file", DiaLoggerLevel.ERROR);
        }
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        io.setBackendPlatformName("imgui_java_impl_glfw");

        initCallbacks(io);
        addAvailableFonts(io);

        // Set up clipboard functionality
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

        imGuiGlfw.init(glfwWindow, true);
        imGuiGl3.init("#version 330 core");
        SappStyles.setSapphireStyles();
        SappImGui.init();
        initWindows();
        changeFont(Sapphire.get().getSettings().getCurrentFont());
    }

    private void initWindows() {
        /*
         * For now windows are going to be statically added in the init function. A new ImGui frame is created to allow
         * the use of ImGUI functionalities when creating the windows
         */

        startFrame();

        // WINDOWS
        gameView = new GameViewWindow();
        ImguiWindow newWindow = new SettingsWindow();
        windows.put(newWindow.getId(), newWindow);
        newWindow = new InspectorWindow();
        windows.put(newWindow.getId(), newWindow);
        newWindow = new AssetsWindow();
        windows.put(newWindow.getId(), newWindow);
        newWindow = new EnvHierarchyWindow();
        windows.put(newWindow.getId(), newWindow);
        newWindow = new LogViewerWindow();
        windows.put(newWindow.getId(), newWindow);
        newWindow = new ProfilerWindow();
        windows.put(newWindow.getId(), newWindow);
        newWindow = new FileNavigatorWindow();
        windows.put(newWindow.getId(), newWindow);
        newWindow = new DebugSapphireWindow();
        windows.put(newWindow.getId(), newWindow);

        /* Settings for the windows are loaded. At the time of writing this code, only one setting is stored, being if
         * the window is active or not. Because of this, the window settings are stored as a simple map*/
        for (String windowId : Sapphire.get().getSettings().getActiveWindows().keySet()) {
            for (String window : windows.keySet()) {
                if (windows.get(window).getId().equals(windowId)) {
                    windows.get(window).setActive(Sapphire.get().getSettings().getActiveWindows().get(windowId));
                }
            }
        }

        endFrame();
    }

    private void initCallbacks(ImGuiIO io) {
        // GLFW callbacks to handle user input
        glfwSetKeyCallback(glfwWindow, (w, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                io.setKeysDown(key, true);
            } else if (action == GLFW_RELEASE) {
                io.setKeysDown(key, false);
            }

            SapphireControls.processControls(io);

            io.addConfigFlags(ImGuiConfigFlags.NavNoCaptureKeyboard);

            io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
            io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
            io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
            io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));

            if (!io.getWantCaptureKeyboard()) {
                //KeyListener.keyCallback(w, key, scancode, action, mods);
            }
        });

        glfwSetCharCallback(glfwWindow, (w, c) -> {
            //DiaLogger.log("Pressed char: " + w + " - " + c);
        });

        glfwSetCharModsCallback(glfwWindow, (w, c, k) -> {
            //DiaLogger.log("Pressed char: " + w + " - " + c + " - " + k);
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
    }

    private void addAvailableFonts(imgui.ImGuiIO io) {

        ArrayList<File> fontFiles = DiaUtils.getFilesInDir("sapphire/fonts", "ttf");
        fontFiles.addAll(DiaUtils.getFilesInDir("sapphire/fonts", "otf"));
        SapphireSettings settings = Sapphire.get().getSettings();
        if (!fontFiles.isEmpty()) {

            String[] fontsList = new String[fontFiles.size()];
            int i = 0;
            for (File file : fontFiles) {

                final ImFontAtlas fontAtlas = io.getFonts();
                final ImFontConfig fontConfig = new ImFontConfig();
                fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());
                fontConfig.setPixelSnapH(true);

                ImFont font = fontAtlas.addFontFromFileTTF(file.getAbsolutePath(), settings.getFontSize(), fontConfig);
                settings.addFont(font);
                fontsList[i] = file.getName();
                fontConfig.destroy(); // After all fonts were added we don't need this config more
                i++;
            }
            settings.setFontsList(fontsList);
        } else {
            DiaLogger.log("Failed to load any font files from Sapphire fonts dir");
        }
    }

    private void endFrame() {

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glClearColor(0f, 0f, 0f, 1f);
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

        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0, 8);
        ImGui.begin("Dockspace Outer", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar();

        // Dockspace
        dockId = ImGui.dockSpace(ImGui.getID("Dockspace"));
        imgui.ImGui.setNextWindowDockID(dockId);
        SappMenuBar.imgui(this);

        ImGui.end();
    }

    private void startFrame() {
        imGuiGlfw.newFrame();
        ImGui.newFrame();
        if (fontChanged) {
            SapphireEvents.notify(new SappEvent(SappEventType.Font_changed));
            fontChanged = false;
        }
    }

    public void changeFont(String fontName) {
        SapphireSettings settings = Sapphire.get().getSettings();
        settings.setCurrentFont(fontName);
        for (ImFont font : settings.getFonts()) {
            if (font.getDebugName().split(",")[0].equals(fontName)) {
                ImGui.getIO().setFontDefault(font);
                fontChanged = true;
            }
        }
    }

    public void addWindow(ImguiWindow window) {
        if (window != null) {
            if (windows.get(window.getTitle()) == null) {
                this.windowsToAdd.add(window);
                this.dirty = true;
            } else {
                DiaLogger.log("Tried to open an already opened window: '" + window.getId() + "'");
            }
        }
    }

    public void update() {
        startFrame();
        setupDockSpace();

        ArrayList<String> toRemove = new ArrayList<>();
        this.gameView.imgui(this);
        for (String window : windows.keySet()) {

            if (windows.get(window).shouldClose()) {
                toRemove.add(window);
                dirty = true;
            }
            if (windows.get(window).isActive().get()) {
                windows.get(window).imgui(this);
            }
        }

        endFrame();

        if (dirty) {
            for (String window : toRemove) {
                windows.remove(window);
            }
            for (ImguiWindow window : windowsToAdd) {
                windows.put(window.getId(), window);
            }
            windowsToAdd.clear();
            this.dirty = false;
        }
    }

    public void destroyImGui() {
        imGuiGl3.dispose();
        ImGui.destroyContext();
    }
}
