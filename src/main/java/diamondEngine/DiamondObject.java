package diamondEngine;

import diamondEngine.diaUtils.DiaUUID;

public abstract class DiamondObject {

    // ATTRIBUTES
    private transient Environment parent;
    private final String uuid;

    // CONSTRUCTORS
    public DiamondObject(Environment env) {
        this.parent = env;
        this.uuid = DiaUUID.generateUUID();
    }

    public DiamondObject(Environment env, String uuid) {
        this.parent = env;
        this.uuid = DiaUUID.checkUUID(uuid);
    }

    // GETTERS && SETTERS
    public Environment getParent() {
        return parent;
    }

    public String getUuid() {
        return uuid;
    }

    public void setParent(Environment parent) {
        this.parent = parent;
    }

    public abstract void update(float dt);
}
