package sapphire.imgui.windows;

import imgui.ImGui;

public class EntityPropertiesWindow extends ImguiWindow {

    public EntityPropertiesWindow() {
        super("entity_properties", "Entity Properties");
    }

    @Override
    public void imgui() {
        ImGui.begin(this.getTitle());

        ImGui.end();
    }
}
