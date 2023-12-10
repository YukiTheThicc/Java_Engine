package sapphire;

import com.google.gson.JsonObject;
import diamondEngine.Diamond;
import diamondEngine.Window;
import diamondEngine.diaControls.MouseControls;
import diamondEngine.diaAssets.Texture;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaUtils;
import sapphire.eventsSystem.SappEvents;
import sapphire.imgui.SappInspectable;
import sapphire.imgui.SappImGuiLayer;
import sapphire.utils.SappDefaultLiterals;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class Sapphire {

    /**
     * Class that manages and contains all FrontEnd/Editor functionalities and resources
     */
    // CONSTANTS
    private static final String UNKNOWN_LITERAL = "UNKNOWN";

    // ATTRIBUTES
    private static Sapphire sapphire = null;
    private final Window window;
    private final SappSettings settings;
    private final HashMap<String, int[]> colors;
    private final HashMap<String, Texture> icons;
    private final Diamond diaInstance;
    private SappProject project;
    private HashMap<String, String> literals;
    private SappImGuiLayer imGUILayer;
    private SappInspectable activeObject;
    private boolean diaRunning;
    private boolean running;
    private float dt;

    // CONSTRUCTORS
    private Sapphire() {
        this.window = Window.get();
        this.settings = new SappSettings();
        this.imGUILayer = null;
        this.running = false;
        this.literals = new HashMap<>();
        this.colors = new HashMap<>();
        this.icons = new HashMap<>();
        this.diaInstance = Diamond.get();
        this.diaRunning = false;
        this.project = null;
        this.dt = 0f;
    }

    // GETTERS & SETTERS
    public SappSettings getSettings() {
        return settings;
    }

    public void setLiterals(JsonObject literals) {
        for (String key : literals.keySet()) {
            this.literals.put(key, literals.get(key).getAsString());
        }
    }

    public static String getLiteral(String literal) {
        if (Sapphire.get().literals.get(literal) != null) return Sapphire.get().literals.get(literal);
        return literal + ": " + UNKNOWN_LITERAL;
    }

    public static Texture getIcon(String name) {
        if (Sapphire.get().icons.get(name) != null) {
            return Sapphire.get().icons.get(name);
        }
        return Sapphire.get().icons.get("_blank.png");
    }

    public SappProject getProject() {
        return project;
    }

    public void setProject(SappProject project) {
        this.project = project;
        if (project.getRoot() != null) {
            settings.setLastProject(project.getRoot().getPath().getAbsolutePath());
            settings.save();
        } else {
            settings.setLastProject("");
            settings.save();
        }
    }

    public SappImGuiLayer getImGUILayer() {
        return imGUILayer;
    }

    public float getDt() {
        return dt;
    }

    public static int[] getColor(String color) {
        if (Sapphire.get().colors.get(color) != null) return Sapphire.get().colors.get(color);
        return Sapphire.get().colors.get("default");
    }

    public boolean isDiaRunning() {
        return diaRunning;
    }

    public void setDiaRunning(boolean diaRunning) {
        this.diaRunning = diaRunning;
    }

    public static SappInspectable getActiveObject() {
        return Sapphire.get().activeObject;
    }

    public static void setActiveObject(SappInspectable activeObject) {
        Sapphire.get().activeObject = activeObject;
    }

    public static String getProjectDir() {
        if (Sapphire.get().project != null) return Sapphire.get().project.getRoot().getPath().getAbsolutePath();
        return "";
    }

    // METHODS
    private boolean shouldClose() {
        return glfwWindowShouldClose(this.window.getGlfwWindow());
    }

    private void loadIcons() {
        ArrayList<File> icons = DiaUtils.getFilesInDir("sapphire/res/icons", "png");
        for (File icon : icons) {
            Texture iconTex = new Texture();
            iconTex.load(icon.getAbsolutePath());
            if (iconTex.getId() != 0 && !"".equals(iconTex.getPath())) {
                this.icons.put(icon.getName(), iconTex);
            }
        }
    }

    private void defaultColors() {

        // Dialogger colors
        colors.put("DiaLogger.CRITICAL", new int[]{175, 50, 233, 255});
        colors.put("DiaLogger.ERROR", new int[]{200, 50, 50, 255});
        colors.put("DiaLogger.WARN", new int[]{200, 175, 50, 255});
        colors.put("DiaLogger.DEBUG", new int[]{50, 200, 50, 255});
        colors.put("DiaLogger.SAPP_DEBUG", new int[]{54, 75, 108, 255});

        // main theme
        colors.put("default", new int[]{125, 0, 125, 255});
        colors.put("light_grey", new int[]{101, 110, 123, 255});
        colors.put("grey", new int[]{61, 66, 72, 255});
        colors.put("main_bg", new int[]{41, 46, 52, 255});
        colors.put("dark_bg", new int[]{25, 31, 37, 255});
        colors.put("highlight", new int[]{93, 139, 191, 255});
        colors.put("accent", new int[]{64, 79, 100, 255});
        colors.put("inactive", new int[]{30, 38, 57, 255});

        // Others
        colors.put("font", new int[]{218, 224, 232, 255});
    }

    public void defaultLiterals() {
        this.literals = SappDefaultLiterals.generateDefaults();
    }

    public static Sapphire get() {
        if (Sapphire.sapphire == null) {
            Sapphire.sapphire = new Sapphire();
        }
        return sapphire;
    }

    public void launch() {
        // Initialize general front end and Engine
        DiaLogger.init();                       // First thing so other systems may log issues when initializing
        DiaUtils.init();                        // Utils ready for other systems to be able to use them
        defaultLiterals();                      // Setting default literals
        defaultColors();                        // Setting default colors

        // Init the window
        window.init("Sapphire", "sapphire/icon.png");

        // Init Sapphires Diamond instance
        diaInstance.init();

        // Register the editor controller for profiling
        Diamond.getProfiler().addRegister("Editor Controller");
        loadIcons();                            // Load Sapphire icons

        // Create ImGUI layer for Sapphire
        imGUILayer = new SappImGuiLayer(this.window.getGlfwWindow());
        settings.init();                        // Init settings before layer to have the settings available for ImGui
        imGUILayer.init();                      // Init ImGui layer
        SappEvents.init(imGUILayer);            // Init sapp events, needs layer for certain handlers
        run();                                  // Run Sapphire main loop

        // Finalize execution once Sapphire is closed
        if (project != null) project.closeProject();
        imGUILayer.destroyImGui();
        DiaLogger.close();
    }

    public void run() {
        running = true;
        float bt = (float) glfwGetTime();
        float et;
        while (running) {

            window.pollEvents();
            SappMouseController.update(dt);
            Diamond.getProfiler().beginMeasurement("Total");
            Diamond.getCurrentEnv().startFrame();
            if (dt >= 0) {
                diaInstance.updateAllEnvLists();
                diaInstance.update(dt);
            }
            MouseControls.endFrame();
            Diamond.getCurrentEnv().endFrame();
            Diamond.getProfiler().endMeasurement("Total");
            Diamond.getProfiler().update(dt);

            window.endFrame();
            imGUILayer.update();
            et = (float) glfwGetTime();
            dt = et - bt;
            bt = et;
            running = !shouldClose();
        }
    }
}