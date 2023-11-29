package sapphire.imgui;

import diamondEngine.diaControls.MouseControls;
import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaUtils;
import imgui.extension.implot.ImPlot;
import imgui.flag.ImGuiStyleVar;
import imgui.internal.ImGuiDockNode;
import sapphire.Sapphire;
import sapphire.SappKeyControls;
import sapphire.eventsSystem.SappEvents;
import sapphire.SappSettings;
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

public class SappImGuiLayer {

    private final long glfwWindow;
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private HashMap<String, ImguiWindow> windows;
    private ArrayList<ImguiWindow> windowsToAdd;
    private ArrayList<ImguiWindow> windowsToRemove;
    private GameViewWindow gameView;
    private FileWindow lastFocusedFile;
    private ImFont smallFont;
    private int dockId;
    private boolean dirty;
    private boolean fontChanged = false;

    // CONSTRUCTORS
    public SappImGuiLayer(long windowPtr) {
        this.glfwWindow = windowPtr;
        this.windows = new HashMap<>();
        this.dockId = -1;
        this.dirty = false;
        this.windowsToAdd = new ArrayList<>();
        this.windowsToRemove = new ArrayList<>();
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

    public ImFont getSmallFont() {
        return smallFont;
    }

    public GameViewWindow getGameView() {
        return gameView;
    }

    // METHODS
    public void init() {

        ImGui.createContext();
        ImPlot.createContext();
        final ImGuiIO io = ImGui.getIO();

        try {
            Files.createDirectories(Paths.get("sapphire"));
            io.setIniFilename("sapphire/imgui.ini");
        } catch (IOException e) {
            DiaLogger.log(this.getClass(), "Failed to set ImGui ini file", DiaLoggerLevel.ERROR);
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

        // !!! REVISE !!! Should be false once all callbacks are set up
        imGuiGlfw.init(glfwWindow, true);
        imGuiGl3.init("#version 330 core");
        SappStyles.setSapphireStyles();
        SappImGuiUtils.init();
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
        newWindow = new LogWindow();
        windows.put(newWindow.getId(), newWindow);
        newWindow = new ProfilerWindow();
        windows.put(newWindow.getId(), newWindow);
        newWindow = new FileNavigatorWindow();
        windows.put(newWindow.getId(), newWindow);
        newWindow = new DebugSapphireWindow();
        windows.put(newWindow.getId(), newWindow);
        newWindow = new EditorCameraWindow();
        windows.put(newWindow.getId(), newWindow);

        /* Settings for the windows are loaded. At the time of writing this code, only one setting is stored, being if
         * the window is active or not. Because of this, the window settings are stored as a simple map
         */
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

            SappKeyControls.processControls(io);

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

            if (!io.getWantCaptureMouse() || gameView.getWantCaptureMouse()) {
                MouseControls.mouseButtonCallback(w, button, action, mods);
            }
        });

        glfwSetScrollCallback(glfwWindow, (w, xOffset, yOffset) -> {
            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);


            if (!io.getWantCaptureMouse() || gameView.getWantCaptureMouse()) {
                MouseControls.mouseScrollCallback(w, xOffset, yOffset);
            }
        });
    }

    private void addAvailableFonts(imgui.ImGuiIO io) {

        ArrayList<File> fontFiles = DiaUtils.getFilesInDir("sapphire/fonts", "ttf");
        fontFiles.addAll(DiaUtils.getFilesInDir("sapphire/fonts", "otf"));
        SappSettings settings = Sapphire.get().getSettings();
        if (!fontFiles.isEmpty()) {

            String[] fontsList = new String[fontFiles.size()];
            int i = 0;
            for (File file : fontFiles) {

                final ImFontAtlas fontAtlas = io.getFonts();
                final ImFontConfig fontConfig = new ImFontConfig();
                fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());
                fontConfig.setPixelSnapH(true);

                ImFont font = fontAtlas.addFontFromFileTTF(file.getAbsolutePath(), settings.getFontSize(), fontConfig);

                /*
                int[] rect_ids = new int[2];
                rect_ids[0] = io.getFonts().addCustomRectFontGlyph(font, (short) 'a', 13, 13, 13 + 1f);
                rect_ids[1] = io.getFonts().addCustomRectFontGlyph(font, (short) 'b', 13, 13, 13 + 1f);

                io.getFonts().build();

                ImInt width = new ImInt();
                ImInt height = new ImInt();
                ImInt bytesPerPixel = new ImInt();
                ByteBuffer texPixels = io.getFonts().getTexDataAsRGBA32(width, height, bytesPerPixel);
                DiaLogger.log("_____________________");
                DiaLogger.log("width: " + width);
                DiaLogger.log("height: " + height);
                DiaLogger.log("bytesPerPixel: " + bytesPerPixel);
                DiaLogger.log("size: " + texPixels.limit());

                for (int id : rect_ids) {
                    ImFontGlyph cosa = font.findGlyph(id);
                    DiaLogger.log("Glyph: " + id);
                    DiaLogger.log("Glyph width: " + cosa.getX1());
                    DiaLogger.log("Glyph height: " + cosa.getY1());
                    DiaLogger.log("Glyph advance: " + cosa.getAdvanceX());
                    for (int y = 0; y < cosa.getY1(); y++) {
                        int index = (int) (cosa.getAdvanceX() + (cosa.getY1() + y) * width.get() + cosa.getX1());
                        DiaLogger.log("Index: " + index);
                        for (int x = (int) (cosa.getX1() + cosa.getAdvanceX()); x > 0; x--) {
                            texPixels.put(index, new byte[]{(byte) 255, (byte) 0, (byte) 0, (byte) 255});
                            index++;
                        }
                    }
                }

                byte[] arr = new byte[texPixels.limit()];
                texPixels.get(arr);
                ImFont newFont = fontAtlas.addFontFromMemoryTTF(arr, settings.getFontSize());*/

                settings.addFont(font);
                ImFont smallFont = fontAtlas.addFontFromFileTTF(file.getAbsolutePath(), settings.getFontSize() - 4, fontConfig);
                settings.addSmallFont(smallFont);
                fontsList[i] = file.getName();
                fontConfig.destroy(); // After all fonts were added we don't need this config anymore
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
            SappEvents.notify(new SappEvent(SappEventType.Font_changed));
            fontChanged = false;
        }
    }

    public void changeFont(String fontName) {
        SappSettings settings = Sapphire.get().getSettings();
        settings.setCurrentFont(fontName);
        for (ImFont font : settings.getFonts()) {
            if (font.getDebugName().split(",")[0].equals(fontName)) {
                ImGui.getIO().setFontDefault(font);
                smallFont = settings.getSmallFonts().get(settings.getFonts().indexOf(font));
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

    private void afterUpdate() {
        if (dirty) {
            for (ImguiWindow window : windowsToRemove) {
                windows.remove(window.getId());
            }
            for (ImguiWindow window : windowsToAdd) {
                windows.put(window.getId(), window);
            }
            windowsToAdd.clear();
            this.dirty = false;
        }
    }

    public void update() {
        startFrame();
        setupDockSpace();
        ImGuiDockNode node = ImGui.dockBuilderGetCentralNode(dockId);
        ImGui.setNextWindowDockID(node.getID());
        this.gameView.imgui(this);
        for (ImguiWindow window : windows.values()) {
            if (window.shouldClose()) {
                windowsToRemove.add(window);
                dirty = true;
            }
            if (window.isActive().get()) {
                window.imgui(this);
            }
        }
        endFrame();
        afterUpdate();
    }

    public void destroyImGui() {
        imGuiGl3.dispose();
        ImGui.destroyContext();
    }
}
