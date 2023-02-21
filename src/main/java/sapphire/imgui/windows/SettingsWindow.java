package sapphire.imgui.windows;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import sapphire.imgui.ImGUILayer;

public class SettingsWindow extends ImguiWindow {

    public SettingsWindow() {
        super("settings", "Settings");
    }

    @Override
    public void imgui(ImGUILayer layer) {

        ImGui.setNextWindowSize(400f, 400f, ImGuiCond.FirstUseEver);
        ImGui.begin(this.getTitle());

        ImGui.end();
    }
}
