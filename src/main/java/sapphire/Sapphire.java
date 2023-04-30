package sapphire;

import com.google.gson.JsonObject;
import diamondEngine.Diamond;
import diamondEngine.Window;
import diamondEngine.diaRenderer.DebugRenderer;
import diamondEngine.diaRenderer.Texture;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaUtils;
import imgui.flag.ImGuiCol;
import imgui.internal.ImGui;
import sapphire.imgui.SappDrawable;
import sapphire.imgui.SappImGUILayer;
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
    private final SapphireSettings settings;
    private final HashMap<String, int[]> colors;
    private final HashMap<String, Texture> icons;
    private final Diamond diaInstance;
    private SapphireProject project;
    private HashMap<String, String> literals;
    private SappImGUILayer imGUILayer;
    private SappDrawable activeObject;
    private boolean diaRunning;
    private boolean running;
    private float dt;

    // CONSTRUCTORS
    private Sapphire() {
        this.window = Window.get();
        this.settings = new SapphireSettings();
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
    public SapphireSettings getSettings() {
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

    public SapphireProject getProject() {
        return project;
    }

    public void setProject(SapphireProject project) {
        this.project = project;
        if (project.getRoot() != null) {
            settings.setLastProject(project.getRoot().getPath().getAbsolutePath());
            settings.save();
        } else {
            settings.setLastProject("");
            settings.save();
        }
    }

    public SappImGUILayer getImGUILayer() {
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

    public static SappDrawable getActiveObject() {
        return Sapphire.get().activeObject;
    }

    public static void setActiveObject(SappDrawable activeObject) {
        Sapphire.get().activeObject = activeObject;
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

    public void defaultLiterals() {
        this.literals = SappDefaultLiterals.generateDefaults();
    }

    private void defaultColors() {
        colors.put("DiaLogger.CRITICAL", new int[]{175,50,233,255});
        colors.put("DiaLogger.ERROR", new int[]{200,50,50,255});
        colors.put("DiaLogger.WARN", new int[]{200,175,50,255});
        colors.put("DiaLogger.DEBUG", new int[]{50,200,50,255});
        colors.put("DiaLogger.SAPP_DEBUG", new int[]{54,75,108,255});

        // main theme
        colors.put("default", new int[]{125,0,125,255});
        colors.put("light_grey", new int[]{111,120,133,255});
        colors.put("grey", new int[]{62,71,86,255});
        colors.put("main_bg", new int[]{30,35,43,255});
        colors.put("dark_bg", new int[]{16,20,26,255});
        colors.put("highlight", new int[]{86,128,193,255});
        colors.put("accent", new int[]{54,75,108,255});
        colors.put("inactive", new int[]{34,48,76,255});

        // Detail/Icon colors
        colors.put("detail_highLight", new int[]{29,190,224,255});
        colors.put("detail_accent", new int[]{25,119,139,255});
        colors.put("detail_dark", new int[]{33,66,84,255});
        colors.put("detail_bg", new int[]{31,42,55,255});

        // Others
        colors.put("font", new int[]{218,224,232,255});
    }

    public static Sapphire get() {
        if (Sapphire.sapphire == null) {
            Sapphire.sapphire = new Sapphire();
        }
        return sapphire;
    }

    public void launch() {
        // Initialize general front end an Engine
        DiaLogger.init();
        DiaUtils.init();
        SapphireEvents.init(imGUILayer);
        defaultLiterals();
        defaultColors();
        window.init("Sapphire", "sapphire/icon.png");
        diaInstance.init();
        loadIcons();
        imGUILayer = new SappImGUILayer(this.window.getGlfwWindow());
        settings.init();
        imGUILayer.init();
        run();
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
            diaInstance.getCurrentEnv().startFrame();
            if (dt >= 0) {
                diaInstance.update(dt);
            }
            DebugRenderer.beginFrame();
            DebugRenderer.draw();
            diaInstance.getCurrentEnv().endFrame();

            window.endFrame();
            imGUILayer.update();
            et = (float) glfwGetTime();
            dt = et - bt;
            bt = et;
            running = !shouldClose();
        }
    }
}