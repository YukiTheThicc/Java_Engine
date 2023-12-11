package diamondEngine.diaComponents;

import diamondEngine.DiaObject;

public abstract class Component extends DiaObject {

    // ATTRIBUTES
    private String ownerId;

    public Component() {
       super();
    }

    public Component(String uuid) {
        super( uuid);
    }

    // GETTERS & SETTERS
    public String getOwner() {
        return ownerId;
    }

    public void setOwner(String owner) {
        this.ownerId = owner;
    }

    // METHODS

    /**
     * !!! REVISE !!! It may be better to re-think this approach. To allow components to work properly WITHIN the current
     * serialization system, it is necessary to implement an init function that initializes transient attributes as they
     * are not serialized. Of course this is only necessary when the component contains transient attributes.
     */
    public abstract void init();

    public abstract void update(float dt);

    public abstract Component copy();
}
