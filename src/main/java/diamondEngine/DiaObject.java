package diamondEngine;

import diamondEngine.diaUtils.DiaUUID;

public abstract class DiaObject {

    // ATTRIBUTES
    private transient Environment parent = null;
    private final String uuid;

    // CONSTRUCTORS
    public DiaObject() {
        this.uuid = DiaUUID.generateUUID();
    }

    public DiaObject(String uuid) {
        this.uuid = DiaUUID.checkUUID(uuid);
    }

    // GETTERS && SETTERS
    public Environment getParent() {
        return parent;
    }

    public void setParent(Environment parent) {
        this.parent = parent;
    }

    public String getUuid() {
        return uuid;
    }

    public abstract void update(float dt);
}
