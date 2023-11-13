package diamondEngine;

import com.google.gson.*;
import diamondEngine.diaComponents.Component;
import diamondEngine.diaRenderer.DebugRenderer;
import diamondEngine.diaRenderer.Framebuffer;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaProfiler;
import diamondEngine.diaUtils.serializers.ComponentSerializer;
import diamondEngine.diaUtils.serializers.EntitySerializer;
import imgui.ImGui;
import imgui.type.ImInt;
import imgui.type.ImString;
import sapphire.Sapphire;
import sapphire.imgui.SappDrawable;
import sapphire.imgui.SappImGui;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Environment implements SappDrawable {

    // CONSTANTS
    public static final String ENV_EXTENSION = ".denv";
    public static final int DEFAULT_FRAME_X = 480;
    public static final int DEFAULT_FRAME_Y = 270;

    // ATTRIBUTES
    private final long UID_SEED = 1000000000;
    private long CURRENT = UID_SEED + 1;
    private Environment parent;
    private String name;
    private transient String originFile;
    private List<Environment> children;
    private List<Entity> entities;
    private List<Component> components;
    private int frameX;
    private int frameY;
    private boolean isInitialized;

    // Runtime attributes
    private transient List<Entity> entitiesToRemove;
    private transient List<Component> componentsToRemove;
    private transient Framebuffer frame;
    private transient float winSizeAdjustX = 1.0f;
    private transient float winSizeAdjustY = 1.0f;
    private transient boolean isDirty = false;
    private transient boolean toRemove = false;
    private transient boolean isModified = true;
    private transient boolean debug = true;
    private transient boolean isProfiling = true;

    // CONSTRUCTORS
    public Environment(String name) {
        this.frameX = DEFAULT_FRAME_X;
        this.frameY = DEFAULT_FRAME_Y;
        this.parent = null;
        this.name = name;
        this.originFile = null;
        this.isInitialized = false;
        this.children = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.components = new ArrayList<>();
        this.entitiesToRemove = new ArrayList<>();
        this.componentsToRemove = new ArrayList<>();
        this.winSizeAdjustX = (float) Window.getWidth() / frameX;
        this.winSizeAdjustY = (float) Window.getHeight() / frameY;
    }

    public Environment(String name, Environment parent) {
        this.frameX = DEFAULT_FRAME_X;
        this.frameY = DEFAULT_FRAME_Y;
        this.parent = parent;
        this.name = name;
        this.originFile = null;
        this.isInitialized = false;
        this.children = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.components = new ArrayList<>();
        this.entitiesToRemove = new ArrayList<>();
        this.componentsToRemove = new ArrayList<>();
        this.winSizeAdjustX = (float) Window.getWidth() / frameX;
        this.winSizeAdjustY = (float) Window.getHeight() / frameY;
    }

    // GETTERS & SETTERS
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Environment> getChildren() {
        return children;
    }

    public Environment getParent() {
        return parent;
    }

    public void setParent(Environment parent) {
        this.parent = parent;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Component> getComponents() {
        return components;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public Framebuffer getFrame() {
        return frame;
    }

    public int getFrameX() {
        return frameX;
    }

    public int getFrameY() {
        return frameY;
    }

    public void setDirty() {
        isDirty = true;
        isModified = true;
    }

    public void setToRemove() {
        this.toRemove = true;
    }

    public boolean isToRemove() {
        return toRemove;
    }

    public String getOriginFile() {
        return originFile;
    }

    public void setOriginFile(String originFile) {
        this.originFile = originFile;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified() {
        isModified = true;
    }

    public float getWinSizeAdjustX() {
        return winSizeAdjustX;
    }

    public float getWinSizeAdjustY() {
        return winSizeAdjustY;
    }

    // METHODS
    public void init() {
        frame = new Framebuffer(frameX, frameY);
        isInitialized = true;
        isModified = true;
        if (isProfiling) {
            Diamond.getProfiler().addRegister("Update Lists");
            Diamond.getProfiler().addRegister("Update Children");
            Diamond.getProfiler().addRegister("Update Components");
            Diamond.getProfiler().addRegister("Update Entities");
            Diamond.getProfiler().addRegister("Debug Render");
        }
    }

    public long genId() {
        CURRENT++;
        return CURRENT;
    }

    public void addChild(Environment environment) {
        if (environment != null) {
            if (!environment.isInitialized) environment.init();
            children.add(environment);
            environment.setParent(this);
            isModified = true;
        }
    }

    public void addComponent(Component component) {
        if (component != null) {
            components.add(component);
            isModified = true;
        }
    }

    public void removeComponent(Component component) {
        if (component != null) {
            componentsToRemove.add(component);
            isDirty = true;
            isModified = true;
        }
    }

    public void addEntity(Entity entity) {
        if (entity != null) {
            entities.add(entity);
            isModified = true;
        }
    }

    public void removeEntity(Entity entity) {
        if (entity != null) {
            entitiesToRemove.add(entity);
            isDirty = true;
            isModified = true;
        }
    }

    public void changeFrame(int frameX, int frameY) {
        this.frameX = frameX;
        this.frameY = frameY;
        this.winSizeAdjustX = (float) Window.getWidth() / frameX;
        this.winSizeAdjustY = (float) Window.getHeight() / frameY;
        frame.destroy();
        frame = new Framebuffer(frameX, frameY);
    }

    public float getAspectRatio() {
        return (float) frameX / frameY;
    }

    public void startFrame() {

        this.frame.bind();
        glClearColor(0.1f, 0.1f, 0.1f, 1f);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void endFrame() {
        if (debug) {
            Diamond.getProfiler().beginMeasurement("Debug Render");
            DebugRenderer.beginFrame();
            DebugRenderer.draw();
            Diamond.getProfiler().endMeasurement("Debug Render");
        }
        this.frame.unBind();
    }

    /**
     * Updates the environment
     * @param dt Delta time for the main loop update
     */
    public void update(float dt) {
        Diamond.getProfiler().beginMeasurement("Update Lists");
        updateLists();
        Diamond.getProfiler().endMeasurement("Update Lists");

        Diamond.getProfiler().beginMeasurement("Update Children");
        for (Environment child : children) child.update(dt);
        Diamond.getProfiler().endMeasurement("Update Children");

        Diamond.getProfiler().beginMeasurement("Update Components");
        for (Component component : components) component.update(dt);
        Diamond.getProfiler().endMeasurement("Update Components");

        Diamond.getProfiler().beginMeasurement("Update Entities");
        for (Entity e : entities) e.update(dt);
        Diamond.getProfiler().endMeasurement("Update Entities");
    }

    /**
     * Updates the entities and components lists of the environment
     */
    public void updateLists() {
        if (isDirty) {
            for (Entity e : entitiesToRemove) {
                entities.remove(e);
            }
            for (Component c : componentsToRemove) {
                components.remove(c);
            }
            entitiesToRemove.clear();
            components.clear();
            isDirty = false;
        }
    }

    public void save(String path) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentSerializer(this))
                .registerTypeAdapter(Entity.class, new EntitySerializer(this))
                .enableComplexMapKeySerialization()
                .create();

        try {
            originFile = path;
            FileWriter writer = new FileWriter(path);
            writer.write(gson.toJson(this));
            writer.close();
            isModified = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCustom(String path) {

    }

    /**
     * Loads an environment from a file passed as a parameter
     * @param path Path of the environment file to load the new environment from
     * @return The newly loaded environment
     */
    public static Environment load(String path) {
        Environment env = new Environment("LOADED ENV");
        env.init();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Environment.class, (InstanceCreator<?>) type -> env)
                .registerTypeAdapter(Component.class, new ComponentSerializer(env))
                .registerTypeAdapter(Entity.class, new EntitySerializer(env))
                .enableComplexMapKeySerialization()
                .create();
        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get(path)));
            if (!inFile.equals("")) {
                Environment loaded = gson.fromJson(inFile, Environment.class);
                long maxId = loaded.CURRENT;
                for (Entity e : loaded.entities) {
                    maxId = e.getUid() > maxId ? e.getUid() + 1 : maxId;
                    for (Component c : e.getComponents()) {
                        maxId = c.getUid() > maxId ? c.getUid() + 1 : maxId;
                    }
                }
                for (Component c : loaded.components) {
                    maxId = c.getUid() > maxId ? c.getUid() + 1 : maxId;
                }
                loaded.CURRENT = maxId;
                loaded.originFile = path;
                loaded.isModified = false;
                return loaded;
            }
        } catch (Exception e) {
            DiaLogger.log(Environment.class, "Failed to load environment from '" + path + "'\n\t" + e.getMessage(), DiaLoggerLevel.ERROR);
        }
        return env;
    }

    public void destroy() {

    }

    @Override
    public void imgui() {
        SappImGui.textLabel("Framebuffer", "" + frame.getFboId());
        ImString newName = new ImString(name, 256);
        if (SappImGui.inputText(Sapphire.getLiteral("name"), newName)) {
            if (Sapphire.get().getProject() != null && !newName.isEmpty()) {
                name = newName.get();
                isModified = true;
            }
        }

        ImInt newWidth = new ImInt(frameX);
        if (SappImGui.inputInt(Sapphire.getLiteral("frame_width"), newWidth)) {
            changeFrame(newWidth.get(), frameY);
            isModified = true;
        }
        ImInt newHeight = new ImInt(frameY);
        if (SappImGui.inputInt(Sapphire.getLiteral("frame_height"), newHeight)) {
            changeFrame(frameX, newHeight.get());
            isModified = true;
        }

        ImGui.text("Current id: " + CURRENT);
        ImGui.text("Seed: " + UID_SEED);


        /*
        ImGui.text("winSizeAdjustX: " + winSizeAdjustX);
        ImGui.text("winSizeAdjustY: " + winSizeAdjustY);
        ImGui.text(Window.getWidth() + " / " + Window.getHeight());
        ImGui.text((float) Window.getWidth() / frameX + " / " + (float) Window.getHeight() / frameY);
        ImGui.text("Ratio: " + getRatio());
        */
    }

    public float getRatio() {
        return ((float) frameX / frameY) * ((float) Window.getHeight() / Window.getWidth());
    }

    @Override
    public boolean selectable() {
        return false;
    }
}
