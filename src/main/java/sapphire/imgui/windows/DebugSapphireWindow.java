package sapphire.imgui.windows;

import imgui.ImGui;
import sapphire.imgui.SappImGuiLayer;

public class DebugSapphireWindow extends ImguiWindow {

    public DebugSapphireWindow() {
        super("debug_sapp", "Sapphire Debugger");
    }

    @Override
    public void imgui(SappImGuiLayer layer) {
        ImGui.showMetricsWindow();
    }
}
