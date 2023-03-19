package diamondEngine.diaComponents;

import diamondEngine.DiaEntity;

public abstract class Component {

    // ATTRIBUTES
    private DiaEntity parent;

    // GETTERS & SETTERS
    public DiaEntity getParent() {
        return parent;
    }

    public void setParent(DiaEntity parent) {
        this.parent = parent;
    }

    // METHODS

}
