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

    public void imgui() {
        ImGui.text(this.getClass().getSimpleName());
    }

    public boolean selectable() {
        boolean result = false;
        ImGui.pushID(this.getUuid());
        ImGui.beginGroup();
        float buttonOriginX = ImGui.getCursorPosX();
        // Calculate alignment position relative to the available space so the text always starts after the icon
        float textPositionX = (ImGui.getFontSize() * 1.5f + ImGui.getTreeNodeToLabelSpacing()) /
                (ImGui.getContentRegionAvailX() - ImGui.getTreeNodeToLabelSpacing());

        ImGui.pushStyleVar(ImGuiStyleVar.ButtonTextAlign, textPositionX, 0.5f);
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
