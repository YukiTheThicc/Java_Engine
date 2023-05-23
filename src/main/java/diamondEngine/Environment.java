package diamondEngine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diamondEngine.diaComponents.Component;
import diamondEngine.diaRenderer.Framebuffer;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaMath;
import diamondEngine.diaUtils.serializers.ComponentSerializer;
import diamondEngine.diaUtils.serializers.EntitySerializer;
import imgui.ImGui;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.joml.Vector2i;
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
    public static final String ENVS_EXT = ".denv";
    public static final int DEFAULT_FRAME_X = 480;
    public static final int DEFAULT_FRAME_Y = 270;

    // ATTRIBUTES
    private Environment parent;
    private String name;
    private String originFile;
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
    private transient long uid;
    private transient float winSizeAdjustX = 1.0f;
    private transient float winSizeAdjustY = 1.0f;
    private transient boolean isDirty = false;
    private transient boolean toRemove = false;
    private transient boolean isModified = true;

    // CONSTRUCTORS
    public Environment(String name) {
        this.uid = Diamond.genId();
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
        this.uid = Diamond.genId();
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

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getUid() {
        return uid;
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
        this.frame.unBind();
    }


    public void update(float dt) {
        updateLists();
        for (Environment child : children) child.update(dt);
        for (Component component : components) component.update(dt);
        for (Entity e : entities) {
            e.update(dt);
        }
    }

    /**
     * Updates the contents of the environment
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
        if (isModified) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(Component.class, new ComponentSerializer())
                    .registerTypeAdapter(Entity.class, new EntitySerializer())
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
    }

    public void load(String path) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentSerializer())
                .registerTypeAdapter(Entity.class, new EntitySerializer())
                .enableComplexMapKeySerialization()
                .create();
        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get(path)));
            if (!inFile.equals("")) {
                Environment loadedEnv = gson.fromJson(inFile, Environment.class);
                entities = loadedEnv.getEntities();
                components = loadedEnv.getComponents();
                name = loadedEnv.getName();
                originFile = loadedEnv.getOriginFile();
                isModified = false;
            }
        } catch (Exception e) {
            DiaLogger.log(this.getClass(), "Failed to load environment from '" + path + "'\n\t" + e.getMessage(), DiaLoggerLevel.ERROR);
        }
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

        ImGui.text("winSizeAdjustX: " + winSizeAdjustX);
        ImGui.text("winSizeAdjustY: " + winSizeAdjustY);
        ImGui.text(Window.getWidth() + " / " + Window.getHeight());
        ImGui.text((float) Window.getWidth() / frameX + " / " + (float) Window.getHeight() / frameY);
    }

    @Override
    public boolean selectable() {
        return false;
    }
}
