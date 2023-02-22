package sapphire.imgui.windows;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import sapphire.imgui.ImGUILayer;

public class EntityPropertiesWindow extends ImguiWindow {

    public EntityPropertiesWindow() {
        super("entity_properties", "Entity Properties");
    }

    @Override
    public void imgui(ImGUILayer layer) {

        ImGui.setNextWindowSize(400f, 400f, ImGuiCond.FirstUseEver);
        ImGui.begin(this.getTitle(), this.getFlags());

        ImGui.end();
    }
}
