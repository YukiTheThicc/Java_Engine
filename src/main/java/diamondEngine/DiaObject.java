package diamondEngine;

import diamondEngine.diaUtils.DiaUUID;
import imgui.ImGui;

public abstract class DiaObject {

    // ATTRIBUTES
    private transient Environment env = null;
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
    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

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
