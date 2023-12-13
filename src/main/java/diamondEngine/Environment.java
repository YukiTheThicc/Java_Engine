package diamondEngine;

import diamondEngine.diaComponents.Transform;
import diamondEngine.diaRenderer.DebugRenderer;
import diamondEngine.diaRenderer.Framebuffer;
import diamondEngine.diaUtils.DiaHierarchyNode;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

public class Environment extends DiaObject {

    // CONSTANTS
    public static final String ENV_EXTENSION = ".denv";
    public static final String DEFAULT_NAME = "UNKNOWN";
    public static final String NEW_ENVIRONMENT_NAME = "New Environment";
    public static final int DEFAULT_FRAME_X = 480;
    public static final int DEFAULT_FRAME_Y = 270;

    // PERSISTENT DATA
    private int frameX;
    private int frameY;
    private List<Entity> entities;
    private List<Environment> nestedEnvironments;
    private HashMap<String, DiaHierarchyNode> hierarchyNodes;
    private DiaHierarchyNode hierarchyTreeRoot;

    // RUNTIME DATA
    private transient String originFile;
    private transient List<Entity> entitiesToAdd;
    private transient List<Entity> entitiesToDelete;
    private transient Framebuffer frame;
    private transient float winSizeAdjustX = 1.0f;
    private transient float winSizeAdjustY = 1.0f;

    // FLAGS
    private transient boolean isDirty = false;
    private transient boolean toRemove = false;
    private transient boolean isModified = true;
    private transient boolean debug = true;
    private transient boolean isProfiling = true;

    // CONSTRUCTORS
    public Environment() {
        super();
        this.name = NEW_ENVIRONMENT_NAME;
        this.frameX = DEFAULT_FRAME_X;
        this.frameY = DEFAULT_FRAME_Y;
        this.entities = new ArrayList<>();
        this.nestedEnvironments = new ArrayList<>();
    }

    public Environment(String name) {
        super();
        this.name = name;
        this.frameX = DEFAULT_FRAME_X;
        this.frameY = DEFAULT_FRAME_Y;
        this.entities = new ArrayList<>();
        this.nestedEnvironments = new ArrayList<>();
    }

    public Environment(String name, String uuid) {
        super(uuid);
        this.name = name;
        this.frameX = DEFAULT_FRAME_X;
        this.frameY = DEFAULT_FRAME_Y;
        this.entities = new ArrayList<>();
        this.nestedEnvironments = new ArrayList<>();
    }

    // GETTERS & SETTERS
    public List<Entity> getEntities() {
        return entities;
    }

    public HashMap<String, DiaHierarchyNode> getHierarchyNodes() {
        return hierarchyNodes;
    }

    public DiaHierarchyNode getHierarchyRoot() {
        return hierarchyTreeRoot;
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

    // METHODS
    // -- START SECTION: LIFECYCLE METHODS -- //
    /**
     * Initializes the transient attributes of the environment,
     */
    public void init() {
        frame = new Framebuffer(frameX, frameY);
        isModified = true;
        hierarchyNodes = new HashMap<>();
        hierarchyTreeRoot = new DiaHierarchyNode(null);
        originFile = null;
        entitiesToAdd = new ArrayList<>();
        entitiesToDelete = new ArrayList<>();
        winSizeAdjustX = (float) Window.getWidth() / frameX;
        winSizeAdjustY = (float) Window.getHeight() / frameY;

        if (isProfiling) {
            Diamond.getProfiler().addRegister("Update Lists");
            Diamond.getProfiler().addRegister("Update Entities");
            Diamond.getProfiler().addRegister("Debug Render");
        }
        changeFrame(frameX, frameY);
    }

    /**
     * Updates the environment
     *
     * @param dt Delta time for the main loop update
     */
    public void update(float dt) {
        Diamond.getProfiler().beginMeasurement("Update Lists");
        updateEntityList();
        Diamond.getProfiler().endMeasurement("Update Lists");

        Diamond.getProfiler().beginMeasurement("Update Entities");
        for (DiaObject e : entities) e.update(dt);
        Diamond.getProfiler().endMeasurement("Update Entities");
    }

    /**
     * Updates the entity list of the environment by looping the buffer lists of entities to add and entities to remove
     * and adding or removing entities form the main list accordingly. Automatically registers and unregisters components
     * from added or removed entities. This method should be called after the environment updates.
     */
    public void updateEntityList() {
        if (isDirty) {
            for (Entity e : entitiesToAdd) {
                e.setEnv(this);
                entities.add(e);
                hierarchyNodes.put(e.getUuid(), new DiaHierarchyNode(e));
            }
            for (Entity e : entitiesToDelete) {
                entities.remove(e);
            }
            // Clear buffer lists
            entitiesToAdd.clear();
            entitiesToDelete.clear();
            isDirty = false;
        }
    }
    // -- END SECTION: UPDATE METHODS -- //

    public void destroy() {

    }
    // -- END SECTION: UPDATE METHODS -- //

    // -- START SECTION: HIERARCHY MANAGEMENT METHODS -- //
    /**
     * Adds an entity to the list of entities to be added to be added to the Environment proper later.
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

    /**
     * Adds an entity to the list of entities to be deleted to be deleted from the Environment proper later.
     * @param entity Entity to be deleted from the environment
     */
    public void deleteEntity(Entity entity) {
        if (entity != null) {
            entitiesToDelete.add(entity);
            isDirty = true;
            isModified = true;
        }
    }

    /**
     * Appends to the node 'child' the node 'parent'. If the parent node is set to null, then the node will be
     * set to root level.
     * @param parent Parent entity
     * @param child Child entity
     */
    public void appendChildToNode(DiaHierarchyNode parent, DiaHierarchyNode child) {
        DiaHierarchyNode newParent = parent != null ? parent : hierarchyTreeRoot;
        if (child.getParent() != null) {
            // If the child has a parent then it has to be removed from the parents children list
            child.getParent().getChildren().remove(child);
        }
        child.setParent(newParent);
        newParent.getChildren().add(child);
    }

    /**
     * Returns the parent Entity for the entity with the given id. Returns null if the entity is at root level in the
     * environment hierarchy.
     * @param a Parent entity
     */
    public Entity getEntityParent(String a) {
        DiaHierarchyNode node = hierarchyNodes.get(a);
        if (node != null) return node.getParent().getEntity();
        return null;
    }

    /**
     * Returns the list of IDs of the given entities children. Returns empty list if the entity has no children
     */
    public String[] getNodeChildren() {
        return new String[0];
    }
    // -- END SECTION: HIERARCHY MANAGEMENT METHODS -- //

    // -- START SECTION: OTHER METHODS -- //
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
    // -- END SECTION: OTHER METHODS -- //

}
