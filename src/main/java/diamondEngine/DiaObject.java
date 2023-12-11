package diamondEngine;

import diamondEngine.diaUtils.DiaUUID;
import imgui.ImGui;

public abstract class DiaObject {

    // ATTRIBUTES
    private transient Environment env = null;
    private transient DiaObject parent;
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
        this.parent = parent;
    }

    // METHODS
    public abstract void update(float dt);

    public void inspect() {
        ImGui.text(this.getClass().getSimpleName());
    };
}
