package sapphire.imgui.windows;

import imgui.ImGui;
import imgui.type.ImInt;
import sapphire.imgui.SappImGuiLayer;

public class DebugSapphireWindow extends ImguiWindow {

    private ImInt num;
    private Object lastPopped;

    public DebugSapphireWindow() {
        super("debug_sapp", "Sapphire Debugger");
        num = new ImInt();
    }

    @Override
    public void imgui(SappImGuiLayer layer) {
        ImGui.showDemoWindow();
    }
}
