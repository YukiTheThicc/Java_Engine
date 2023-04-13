package sapphire.imgui.windows;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import sapphire.imgui.SappImGUILayer;

public class EntityPropertiesWindow extends ImguiWindow {

    public EntityPropertiesWindow() {
        super("entity_properties", "Entity Properties");
    }

    @Override
    public void imgui(SappImGUILayer layer) {

        ImGui.setNextWindowSize(400f, 400f, ImGuiCond.FirstUseEver);
        if (ImGui.begin(this.getTitle(), this.getFlags())) {

        }

        ImGui.end();
    }
}
