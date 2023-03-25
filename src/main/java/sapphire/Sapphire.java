package sapphire;

import com.google.gson.JsonObject;
import diamondEngine.Window;
import diamondEngine.diaRenderer.Texture;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaUtils;
import sapphire.imgui.SappImGUILayer;

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
    private SappImGUILayer imGUILayer;
    private boolean running;
    private float dt;
    private SapphireSettings settings;
    private SapphireProject project;
    private HashMap<String, String> literals;
    private HashMap<String, Texture> icons;

    // CONSTRUCTORS
    private Sapphire() {
        this.window = Window.get();
        this.settings = new SapphireSettings();
        this.imGUILayer = null;
        this.running = false;
        this.literals = new HashMap<>();
        this.icons = new HashMap<>();
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
        if (Sapphire.get().literals.get(literal) != null) {
            return Sapphire.get().literals.get(literal);
        }
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

    // METHODS
    private boolean shouldClose() {
        return glfwWindowShouldClose(this.window.getGlfwWindow());
    }

    private void loadIcons() {
        ArrayList<File> icons = DiaUtils.getFilesInDir("sapphire/res/icons/16", "png");
        for (File icon : icons) {
            Texture iconTex = new Texture();
            iconTex.load(icon.getAbsolutePath());
            if (iconTex.getId() != 0 && !"".equals(iconTex.getPath())) {
                this.icons.put(icon.getName(), iconTex);
            }
        }
    }

    public void defaultLiterals() {
        literals.put("file", "File");
        literals.put("window", "Window");
        literals.put("new_file", "New file");
        literals.put("open_file", "Open file");
        literals.put("save_file", "Save file");
        literals.put("save_as", "Save as...");
        literals.put("open_project", "Open project");
        literals.put("export_project", "Export project");
        literals.put("settings", "Settings");
        literals.put("active_windows", "Active windows");
        literals.put("assets", "Assets");
        literals.put("sprites", "Sprites");
        literals.put("tiles", "Tiles");
        literals.put("sounds", "Sounds");
        literals.put("entity_properties", "Entity properties");
        literals.put("env_hierarchy", "Environments");
        literals.put("apply", "Apply");
        literals.put("accept", "Accept");
        literals.put("ok", "Ok");
        literals.put("close", "Close");
        literals.put("cancel", "Cancel");
        literals.put("yes", "Yes");
        literals.put("no", "No");
        literals.put("confirm_save", "Save?");
        literals.put("sure_to_save_log", "Save log to new file?");
        literals.put("clear", "Clear");
        literals.put("severity", "Severity");
        literals.put("lang", "Language");
        literals.put("dont_ask_again", "Don't ask this again");
        literals.put("save_log", "Save log");
        literals.put("general_settings", "General");
        literals.put("style_settings", "Styles");
        literals.put("font", "Font");
        literals.put("create_project", "Create project");
        literals.put("no_project_file", "No project configuration has been found on the directory");
        literals.put("dir_not_empty", "Cannot create a project on this directory, choose an empty directoryw");
    }

    public static Sapphire get() {
        if (Sapphire.sapphire == null) {
            Sapphire.sapphire = new Sapphire();
        }
        return sapphire;
    }

    public void start() {
        // Initialize general front end an Engine
        DiaLogger.init();
        DiaUtils.init();
        defaultLiterals();
        window.init("Sapphire", "sapphire/icon.png");
        loadIcons();
        imGUILayer = new SappImGUILayer(this.window.getGlfwWindow());
        settings.init();
        imGUILayer.init();
        run();
        DiaLogger.close();
        imGUILayer.destroyImGui();
    }

    public void run() {
        this.running = true;
        float bt = (float) glfwGetTime();
        float et;
        while (this.running) {
            this.window.pollEvents();
            this.window.startFrame();
            if (glfwGetWindowAttrib(window.getGlfwWindow(), GLFW_FOCUSED) != 0) {
                /*if (dt >= 0) {
                }*/
                this.imGUILayer.update();
            }
            this.window.endFrame();
            et = (float) glfwGetTime();
            dt = et - bt;
            bt = et;
            this.running = !this.shouldClose();
        }
    }
}