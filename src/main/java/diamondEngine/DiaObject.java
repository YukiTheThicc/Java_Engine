package diamondEngine;

import diamondEngine.diaUtils.DiaUUID;
import imgui.ImGui;

public abstract class DiaObject {

    // ATTRIBUTES
    private transient Environment env = null;
    private transient DiaObject parent = null;
    private final String uuid;

    // CONSTRUCTORS
    public DiaObject() {
        this.uuid = DiaUUID.generateUUID();
    }

    public DiaObject(String uuid) {
        this.uuid = DiaUUID.checkUUID(uuid);
    }

    // GETTERS && SETTERS
    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
        if (!env.getRegisteredEntities().containsKey(this.getUuid())) env.registerObject(this);
    }

    public String getUuid() {
        return uuid;
    }

    public DiaObject getParent() {
        return parent;
    }

    /**
     * Sets the parent to a new object. Handles removal from the parents entity lists
     * @param parent
     */
    public void setParent(DiaObject parent) {
        // Handle removing of nested entity from its parent list
        if (parent != null) {
            if (parent instanceof Environment) {
                ((Environment) parent).removeEntity((Entity) this);
            } else if (parent instanceof Entity) {
                ((Entity) parent).removeNestedEntity((Entity) this);
            }
        }
        this.parent = parent;
    }

    // METHODS
    public abstract void update(float dt);

    public abstract void destroy();

    public void inspect() {
        ImGui.text(this.getClass().getSimpleName());
    };
}
