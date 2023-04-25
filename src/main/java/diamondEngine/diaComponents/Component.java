package diamondEngine.diaComponents;

import diamondEngine.DiaEntity;
import imgui.ImGui;

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
    public abstract void update(float dt);

    public void imgui() {
        ImGui.text(this.getClass().getSimpleName());
    }
}
