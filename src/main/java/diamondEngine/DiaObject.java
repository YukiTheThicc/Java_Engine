package diamondEngine;

import diamondEngine.diaUtils.DiaUUID;

public abstract class DiaObject {

    // ATTRIBUTES
    private final String uuid;
    protected String name = "";

    // CONSTRUCTORS
    public DiaObject() {
        this.uuid = DiaUUID.generateUUID();
    }

    public DiaObject(String uuid) {
        this.uuid = DiaUUID.checkUUID(uuid);
    }

    // GETTERS && SETTERS
    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // METHODS
    public abstract void update(float dt);

    public abstract void destroy();
}
