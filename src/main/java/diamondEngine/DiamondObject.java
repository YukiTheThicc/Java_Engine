package diamondEngine;

public abstract class DiamondObject {

    // ATTRIBUTES
    private transient Environment parent;
    private final long uid;

    // CONSTRUCTORS
    public DiamondObject(Environment env, long id) {
        this.parent = env;
        this.uid = env.getID();
    }

    public DiamondObject(Environment env) {
        this.parent = env;
        this.uid = env.getID();
    }

    // GETTERS && SETTERS
    public Environment getParent() {
        return parent;
    }

    public long getUid() {
        return uid;
    }

    public void setParent(Environment parent) {
        this.parent = parent;
    }

    public abstract void update(float dt);
}
