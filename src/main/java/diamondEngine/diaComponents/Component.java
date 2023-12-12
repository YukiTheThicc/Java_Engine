package diamondEngine.diaComponents;

import diamondEngine.Entity;
import imgui.ImGui;

public abstract class Component {

    // ATTRIBUTES
    private transient Entity owner;

    public Component() {}

    public Component(Entity owner) {
        this.owner = owner;
    }

    // GETTERS & SETTERS
    public Entity getOwner() {
        return owner;
    }

    public void setOwner(Entity owner) {
        this.owner = owner;
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

    public abstract void destroy();

    public void inspect() {
        ImGui.text(this.getClass().getSimpleName());
    }
}
