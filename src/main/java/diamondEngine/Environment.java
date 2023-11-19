package diamondEngine;

import com.google.gson.*;
import diamondEngine.diaComponents.Component;
import diamondEngine.diaRenderer.DebugRenderer;
import diamondEngine.diaRenderer.Framebuffer;
import diamondEngine.diaUtils.DiaFIFO;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaUUID;
import diamondEngine.diaUtils.serializers.ComponentSerializer;
import diamondEngine.diaUtils.serializers.EntitySerializer;
import imgui.ImGui;
import imgui.flag.ImGuiTableFlags;
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
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class Environment implements SappDrawable {

    // CONSTANTS
    public static final String ENV_EXTENSION = ".denv";
    public static final String DEFAULT_NAME = "UNKNOWN";
    public static final int DEFAULT_FRAME_X = 480;
    public static final int DEFAULT_FRAME_Y = 270;

    // ATTRIBUTES
    // Environment properties
    private Environment parent;
    private String name;
    private String uuid;
    private int frameX;
    private int frameY;
    private List<Environment> children;
    private List<Entity> entities;
    private transient String originFile;
    private transient boolean isInitialized;

    // Runtime attributes
    private transient HashMap<String, DiamondObject> registeredObjects;
    private transient List<Entity> entitiesToRemove;
    private transient Framebuffer frame;
    private transient float winSizeAdjustX = 1.0f;
    private transient float winSizeAdjustY = 1.0f;
    private transient boolean isDirty = false;
    private transient boolean toRemove = false;
    private transient boolean isModified = true;
    private transient boolean debug = true;
    private transient boolean isProfiling = true;

    // CONSTRUCTORS
    public Environment() {
        this.frameX = DEFAULT_FRAME_X;
        this.frameY = DEFAULT_FRAME_Y;
        this.parent = null;
        this.name = DEFAULT_NAME;
        this.uuid = DiaUUID.generateUUID();
        this.originFile = null;
        this.isInitialized = false;
        this.children = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.entitiesToRemove = new ArrayList<>();
        this.winSizeAdjustX = (float) Window.getWidth() / frameX;
        this.winSizeAdjustY = (float) Window.getHeight() / frameY;
    }

    public Environment(String name) {
        this.frameX = DEFAULT_FRAME_X;
        this.frameY = DEFAULT_FRAME_Y;
        this.parent = null;
        this.name = name;
        this.uuid = DiaUUID.generateUUID();
        this.originFile = null;
        this.isInitialized = false;
        this.children = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.entitiesToRemove = new ArrayList<>();
        this.winSizeAdjustX = (float) Window.getWidth() / frameX;
        this.winSizeAdjustY = (float) Window.getHeight() / frameY;
    }

    public Environment(String name, String uuid) {
        this.frameX = DEFAULT_FRAME_X;
        this.frameY = DEFAULT_FRAME_Y;
        this.parent = null;
        this.name = name;
        this.uuid = DiaUUID.checkUUID(uuid);
        this.originFile = null;
        this.isInitialized = false;
        this.children = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.entitiesToRemove = new ArrayList<>();
        this.winSizeAdjustX = (float) Window.getWidth() / frameX;
        this.winSizeAdjustY = (float) Window.getHeight() / frameY;
    }

    // GETTERS & SETTERS
    public String getUuid() {
        return uuid;
    }

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

    public Framebuffer getFrame() {
        return frame;
    }

    public int getFrameX() {
        return frameX;
    }

    public int getFrameY() {
        return frameY;
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

    public void setSaved() {
        isModified = false;
    }

    public float getWinSizeAdjustX() {
        return winSizeAdjustX;
    }

    public float getWinSizeAdjustY() {
        return winSizeAdjustY;
    }

    public HashMap<String, DiamondObject> getRegisteredObjects() {
        return registeredObjects;
    }

    // METHODS
    public void init() {
        frame = new Framebuffer(frameX, frameY);
        isInitialized = true;
        isModified = true;
        registeredObjects = new HashMap<>();
        if (isProfiling) {
            Diamond.getProfiler().addRegister("Update Lists");
            Diamond.getProfiler().addRegister("Update Children");
            Diamond.getProfiler().addRegister("Update Components");
            Diamond.getProfiler().addRegister("Update Entities");
            Diamond.getProfiler().addRegister("Debug Render");
        }
    }

    public void addChild(Environment environment) {
        if (environment != null) {
            if (!environment.isInitialized) environment.init();
            children.add(environment);
            environment.setParent(this);
            isModified = true;
        }
    }

    public void addEntity(Entity entity) {
        if (entity != null) {
            entities.add(entity);
            registeredObjects.put(entity.getUuid(), entity);
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

    public void registerObject(DiamondObject object) {
        this.registeredObjects.put(object.getUuid(), object);
        isModified = true;
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
                for (Component c : e.getComponents()) {
                    registeredObjects.remove(c);
                }
                entities.remove(e);
                registeredObjects.remove(e.getUuid());
            }
            entitiesToRemove.clear();
            isDirty = false;
        }
    }

    public void destroy() {

    }

    @Override
    public void imgui() {
        SappImGui.textLabel("UUID", uuid);
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
        ImGui.separator();

        // UID System INFO
        if (ImGui.beginTable("Objects", 2 , ImGuiTableFlags.Borders)) {

            ImGui.tableSetupColumn("ID");
            ImGui.tableSetupColumn("Object");
            ImGui.tableHeadersRow();
            for (String key : getRegisteredObjects().keySet()) {
                ImGui.tableNextColumn();
                ImGui.text("" + key);
                ImGui.tableNextColumn();
                ImGui.text(getRegisteredObjects().get(key).getClass().getSimpleName());
            }
        }
        ImGui.endTable();

        /*
        ImGui.text("winSizeAdjustX: " + winSizeAdjustX);
        ImGui.text("winSizeAdjustY: " + winSizeAdjustY);
        ImGui.text(Window.getWidth() + " / " + Window.getHeight());
        ImGui.text((float) Window.getWidth() / frameX + " / " + (float) Window.getHeight() / frameY);
        ImGui.text("Ratio: " + getRatio());
        */
    }

    @Override
    public boolean selectable() {
        return false;
    }
}
