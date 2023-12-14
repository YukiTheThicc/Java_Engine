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
    private HashMap<String, DiaHierarchyNode> nodes;

    // RUNTIME DATA
    private transient String originFile;
    private transient List<Entity> entities;
    private transient DiaHierarchyNode hierarchyTree;
    private transient List<DiaHierarchyNode> nodesToAdd;
    private transient List<DiaHierarchyNode> nodesToDelete;
    private transient Framebuffer frame;
    private transient float winSizeAdjustX = 1.0f;
    private transient float winSizeAdjustY = 1.0f;

    // RUNTIME FLAGS
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
    }

    public Environment(String name) {
        super();
        this.name = name;
        this.frameX = DEFAULT_FRAME_X;
        this.frameY = DEFAULT_FRAME_Y;
        this.entities = new ArrayList<>();
    }

    public Environment(String name, String uuid) {
        super(uuid);
        this.name = name;
        this.frameX = DEFAULT_FRAME_X;
        this.frameY = DEFAULT_FRAME_Y;
        this.entities = new ArrayList<>();
    }

    // GETTERS & SETTERS
    public List<Entity> getEntities() {
        return entities;
    }

    public HashMap<String, DiaHierarchyNode> getNodes() {
        return nodes;
    }

    public DiaHierarchyNode getHierarchyRoot() {
        return hierarchyTree;
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
        nodes = new HashMap<>();
        hierarchyTree = new DiaHierarchyNode(null);
        originFile = null;
        nodesToAdd = new ArrayList<>();
        nodesToDelete = new ArrayList<>();
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
            for (DiaHierarchyNode node : nodesToAdd) {
                Entity e = node.getEntity();
                e.setEnv(this);
                entities.add(e);
                nodes.put(e.getUuid(), node);
                modifyHierarchy(null, e);
            }
            for (DiaHierarchyNode e : nodesToDelete) {
                // TODO Implement entity and its node deletion
            }
            // Clear buffer lists
            nodesToAdd.clear();
            nodesToDelete.clear();
            isDirty = false;
        }
    }

    public void destroy() {

    }
    // -- END SECTION: LIFECYCLE METHODS -- //

    // -- START SECTION: HIERARCHY MANAGEMENT METHODS -- //
    /**
     * Adds an entity to the list of entities to be added to be added to the Environment proper later.
     *
     * @param entity Entity to be added
     */
    public void addEntity(Entity entity) {
        if (entity != null && !entities.contains(entity)) {
            if (entity.getComponent(Transform.class) == null) {
                entity.addComponent(new Transform());
            }
            nodesToAdd.add(new DiaHierarchyNode(entity));
            isModified = true;
            isDirty = true;
        }
    }

    /**
     * Adds an entity to the list of entities to be deleted to be deleted from the Environment proper later.
     *
     * @param entity Entity to be deleted from the environment
     */
    public void deleteEntity(Entity entity) {
        if (entity != null) {
            nodesToDelete.add(nodes.get(entity.getUuid()));
            isDirty = true;
            isModified = true;
        }
    }

    /**
     * Appends to the node 'child' the node 'parent'. If the parent node is set to null, then the node will be
     * set to root level.
     *
     * @param parent Parent entity
     * @param child  Child entity
     */
    public void modifyHierarchy(Entity parent, Entity child) {
        DiaHierarchyNode newParent = parent != null ? nodes.get(parent.getUuid()) : hierarchyTree;
        DiaHierarchyNode childNode = nodes.get(child.getUuid());
        DiaHierarchyNode oldParent = nodes.get(childNode.getParent());
        if (newParent.getEntity() == null || !newParent.getEntity().getUuid().equals(childNode.getParent())) {
            if (oldParent != null) {
                // If the child has a parent then it has to be removed from the parents children list
                oldParent.getChildren().remove(child.getUuid());
            } else {
                hierarchyTree.removeChild(child.getUuid());
            }
            childNode.setParent(parent != null ? parent.getUuid() : null);
            newParent.getChildren().add(child.getUuid());
            isModified = true;
        }
    }

    /**
     * Returns the parent Entity for the entity with the given id. Returns null if the entity is at root level in the
     * environment hierarchy.
     *
     * @param a Parent entity
     */
    public Entity getEntityParent(String a) {
        DiaHierarchyNode node = nodes.get(a);
        if (node != null) {
            return nodes.get(node.getParent()).getEntity();
        }
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

    /**
     * Build the hierarchy tree from the nodes list. Meant to be used when the environment is loaded as the nodes are all
     * set to root nodes.
     */
    public void loadEntityHierarchy(HashMap<String, DiaHierarchyNode> nodes) {
        this.entities.clear();
        this.nodes.clear();

        // Load all nodes and corresponding entities into this environment before establishing the tree
        for (DiaHierarchyNode node : nodes.values()) {
            this.nodes.put(node.getEntity().getUuid(), node);
            this.entities.add(node.getEntity());
        }

        // Set the appropriate tree hierarchy from the loaded nodes
        for (DiaHierarchyNode node : this.nodes.values()) {
            if (node.getParent() != null) {
                DiaHierarchyNode parent = nodes.get(node.getParent());
                if (parent.getEntity() != null) {
                    modifyHierarchy(parent.getEntity(), node.getEntity());
                }
            } else {
                modifyHierarchy(null, node.getEntity());
            }
        }
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
