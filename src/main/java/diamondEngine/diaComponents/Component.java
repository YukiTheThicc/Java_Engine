package diamondEngine.diaComponents;

import diamondEngine.DiamondObject;
import diamondEngine.Entity;
import diamondEngine.Environment;
import imgui.ImGui;
import imgui.flag.ImGuiStyleVar;
import sapphire.Sapphire;
import sapphire.imgui.SappDrawable;

public abstract class Component extends DiamondObject implements SappDrawable {

    // ATTRIBUTES
    private Entity owner;

    public Component(Environment env) {
       super(env);
    }

    // GETTERS & SETTERS
    public Entity getOwner() {
        return owner;
    }

    public void setOwner(Entity owner) {
        this.owner = owner;
    }

    // METHODS
    public abstract void update(float dt);

    public void imgui() {
        ImGui.text(this.getClass().getSimpleName());
    }

    public boolean selectable() {
        boolean result = false;
        ImGui.pushID(this.getUid());
        ImGui.beginGroup();
        float buttonOriginX = ImGui.getCursorPosX();

        ImGui.pushStyleVar(ImGuiStyleVar.ButtonTextAlign, 0.1f, 0.5f);
        if (ImGui.button(this.getClass().getSimpleName(), ImGui.getContentRegionAvailX(), ImGui.getFontSize() * 1.5f)) result = true;
        ImGui.popStyleVar();

        ImGui.sameLine();
        ImGui.setCursorPosX(buttonOriginX);
        ImGui.image(Sapphire.getIcon("component.png").getId(), ImGui.getFontSize() * 1.5f, ImGui.getFontSize() * 1.5f,
                0, 1, 1, 0);

        ImGui.endGroup();
        ImGui.popID();

        return result;
    }

    public abstract Component copy();
}
