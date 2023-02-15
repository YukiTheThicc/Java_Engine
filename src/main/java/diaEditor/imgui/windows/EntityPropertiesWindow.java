package diaEditor.imgui.windows;

import imgui.ImGui;

public class EntityPropertiesWindow extends ImguiWindow {

    public EntityPropertiesWindow() {
        super("Entity Properties");
    }

    @Override
    public void imgui() {
        ImGui.begin(this.getTitle());

        ImGui.end();
    }
}
