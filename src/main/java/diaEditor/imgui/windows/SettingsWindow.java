package diaEditor.imgui.windows;

import imgui.ImGui;

public class SettingsWindow extends ImguiWindow{

    public SettingsWindow() {

    }

    @Override
    public void imgui() {
        ImGui.showDemoWindow();
    }
}
