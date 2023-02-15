package sapphire.imgui.windows;

import imgui.ImGui;

public class SettingsWindow extends ImguiWindow {

    public SettingsWindow() {
        super("settings", "Settings");
    }

    @Override
    public void imgui() {
        ImGui.begin(this.getTitle());

        ImGui.end();
    }
}
