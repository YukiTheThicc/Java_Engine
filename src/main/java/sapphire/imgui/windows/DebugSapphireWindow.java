package sapphire.imgui.windows;

import imgui.ImGui;
import sapphire.imgui.SappImGUILayer;

public class DebugSapphireWindow extends ImguiWindow {

    public DebugSapphireWindow() {
        super("debug_sapp", "Sapphire Debugger");
    }

    @Override
    public void imgui(SappImGUILayer layer) {

        ImGui.begin(this.getTitle());
        ImGui.end();
    }
}
