package diamondEngine.diaComponents;

import diamondEngine.DiaEntity;
import diamondEngine.Diamond;
import imgui.ImGui;
import sapphire.imgui.SappDrawable;

public abstract class Component implements SappDrawable {

    // ATTRIBUTES
    private long uid;
    private DiaEntity parent;

    public Component() {
        this.uid = Diamond.genId();
    }

    // GETTERS & SETTERS
    public DiaEntity getParent() {
        return parent;
    }

    public void setParent(DiaEntity parent) {
        this.parent = parent;
    }

    public long getUid() {
        return uid;
    }

    // METHODS
    public abstract void update(float dt);

    public void imgui() {
        ImGui.text(this.getClass().getSimpleName());
    }

    public boolean selectable() {
        return ImGui.selectable(this.getClass().getSimpleName());
    }
}
