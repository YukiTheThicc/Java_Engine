package diamondEngine;

import diamondEngine.diaComponents.Transform;
import diamondEngine.diaRenderer.DebugRenderer;
import diamondEngine.diaRenderer.Framebuffer;
import imgui.ImGui;
import imgui.flag.ImGuiTableFlags;
import imgui.type.ImInt;
import imgui.type.ImString;
import sapphire.Sapphire;
import sapphire.imgui.SappImGuiUtils;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

public class Environment extends DiaObject {

    // CONSTANTS
    public static final String ENV_EXTENSION = ".denv";
    public static final String DEFAULT_NAME = "UNKNOWN";
    public static final int DEFAULT_FRAME_X = 480;
    public static final int DEFAULT_FRAME_Y = 270;

    // ATTRIBUTES
    // Environment properties
    private String name;
    private int frameX;
    private int frameY;
    private List<Environment> children;
    private List<Entity> entities;

    // Runtime attributes
    private transient String originFile;
    private transient HashMap<String, DiaObject> registeredEntities;
    private transient List<Entity> entitiesToAdd;
    private transient List<Entity> entitiesToRemove;
    private transient Framebuffer frame;
    private transient float winSizeAdjustX = 1.0f;
    private transient float winSizeAdjustY = 1.0f;
    private transient boolean isInitialized = true;
    private transient boolean isDirty = false;
    private transient boolean toRemove = false;
    private transient boolean isModified = true;
    private transient boolean debug = true;
    private transient boolean isProfiling = true;

    // CONSTRUCTORS
    public Environment() {
        super();
        this.name = DEFAULT_NAME;
        this.frameX = DEFAULT_FRAME_X;
        this.frameY = DEFAULT_FRAME_Y;
        this.children = new ArrayList<>();
        this.entities = new ArrayList<>();
    }

    public Environment(String name) {
        super();
        this.name = name;
        this.frameX = DEFAULT_FRAME_X;
        this.frameY = DEFAULT_FRAME_Y;
        this.children = new ArrayList<>();
        this.entities = new ArrayList<>();
    }

    public Environment(String name, String uuid) {
        super(uuid);
        this.name = name;
        this.frameX = DEFAULT_FRAME_X;
        this.frameY = DEFAULT_FRAME_Y;
        this.children = new ArrayList<>();
        this.entities = new ArrayList<>();
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

    public HashMap<String, DiaObject> getRegisteredEntities() {
        return registeredEntities;
    }

    // METHODS
    /**
     * Initializes the transient attributes of the environment,
     */
    public void init() {
        frame = new Framebuffer(frameX, frameY);
        isModified = true;
        registeredEntities = new HashMap<>();
        originFile = null;
        entitiesToAdd = new ArrayList<>();
        entitiesToRemove = new ArrayList<>();
        winSizeAdjustX = (float) Window.getWidth() / frameX;
        winSizeAdjustY = (float) Window.getHeight() / frameY;

        if (isProfiling) {
            Diamond.getProfiler().addRegister("Update Lists");
            Diamond.getProfiler().addRegister("Update Children");
            Diamond.getProfiler().addRegister("Update Components");
            Diamond.getProfiler().addRegister("Update Entities");
            Diamond.getProfiler().addRegister("Debug Render");
        }
        changeFrame(frameX, frameY);

        isInitialized = true;
    }

    public void addChild(Environment environment) {
        if (environment != null) {
            if (!environment.isInitialized) environment.init();
            children.add(environment);
            environment.setEnv(this);
            isModified = true;
        }
    }

    /**
     * An entity is added to a buffer list to then be added at
     * @param entity Entity to be added
     */
    public void addEntity(Entity entity) {
        if (entity != null && !entities.contains(entity)) {
            if (entity.getComponent(Transform.class) == null) {
                entity.addComponent(new Transform());
            }
            entitiesToAdd.add(entity);
            isModified = true;
            isDirty = true;
        }
    }

    public void removeEntity(Entity entity) {
        if (entity != null) {
            entitiesToRemove.add(entity);
            isDirty = true;
            isModified = true;
        }
    }

    public void registerObject(DiaObject object) {
        this.registeredEntities.put(object.getUuid(), object);
        isModified = true;
    }

    public void unRegisterObject(DiaObject object) {
        this.registeredEntities.remove(object.getUuid());
        isModified = true;
    }

    public void changeFrame(int frameX, int frameY) {
        this.frameX = frameX;
        this.frameY = frameY;
        this.winSizeAdjustX = (float) frameX / Window.getWidth();
        this.winSizeAdjustY = (float) frameY / Window.getHeight();
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
        updateEntityList();
        Diamond.getProfiler().endMeasurement("Update Lists");

        Diamond.getProfiler().beginMeasurement("Update Children");
        for (Environment child : children) child.update(dt);
        Diamond.getProfiler().endMeasurement("Update Children");

        Diamond.getProfiler().beginMeasurement("Update Entities");
        for (Entity e : entities) e.update(dt);
        Diamond.getProfiler().endMeasurement("Update Entities");
    }

    /**
     * Updates the entity list of the environment by looping the buffer lists of entities to add and entities to remove
     * and adding or removing entities form the main list accordingly. Automatically registers and unregisters components
     * from added or removed entities. This method should be called after the environment updates.
     */
    public void updateEntityList() {
        if (isDirty) {
            for (Entity e : entitiesToAdd) addEntityToList(e);
            for (Entity e : entitiesToRemove) removeEntityFromList(e);
            // Clear buffer lists
            entitiesToAdd.clear();
            entitiesToRemove.clear();
            isDirty = false;
        }
    }

    private void addEntityToList(Entity e) {
        e.setEnv(this);
        registeredEntities.put(e.getUuid(), e);
        entities.add(e);
        registeredEntities.put(e.getUuid(), e);
    }

    private void removeEntityFromList(Entity e) {
        entities.remove(e);
        registeredEntities.remove(e.getUuid());
    }

    public void destroy() {

    }

    @Override
    public void inspect() {
        SappImGuiUtils.textLabel("UUID", getUuid());
        SappImGuiUtils.textLabel("Framebuffer", "" + frame.getFboId());
        ImString newName = new ImString(name, 256);
        if (SappImGuiUtils.inputText(Sapphire.getLiteral("name"), newName)) {
            if (Sapphire.get().getProject() != null && !newName.isEmpty()) {
                name = newName.get();
                isModified = true;
            }
        }

        ImInt newWidth = new ImInt(frameX);
        if (SappImGuiUtils.inputInt(Sapphire.getLiteral("frame_width"), newWidth)) {
            changeFrame(newWidth.get(), frameY);
            isModified = true;
        }
        ImInt newHeight = new ImInt(frameY);
        if (SappImGuiUtils.inputInt(Sapphire.getLiteral("frame_height"), newHeight)) {
            changeFrame(frameX, newHeight.get());
            isModified = true;
        }
        ImGui.separator();

        // UID System INFO
        if (ImGui.beginTable("Objects", 2 , ImGuiTableFlags.Borders)) {

            ImGui.tableSetupColumn("ID");
            ImGui.tableSetupColumn("Object");
            ImGui.tableHeadersRow();
            for (String key : getRegisteredEntities().keySet()) {
                ImGui.tableNextColumn();
                ImGui.text("" + key);
                ImGui.tableNextColumn();
                ImGui.text(getRegisteredEntities().get(key).getClass().getSimpleName());
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
}
