package diaEditor.imgui.windows;

import imgui.ImGui;

public class SettingsWindow extends ImguiWindow {

    public SettingsWindow() {
        super("Settings");
    }

    @Override
    public void imgui() {
        ImGui.begin(this.getTitle());

        ImGui.end();
    }
}
