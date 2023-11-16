package sapphire.imgui.windows;

import diamondEngine.Diamond;
import diamondEngine.DiamondObject;
import diamondEngine.Environment;
import diamondEngine.diaUtils.DiaFIFO;
import imgui.ImGui;
import imgui.flag.ImGuiTableFlags;
import imgui.type.ImInt;
import sapphire.imgui.SappImGui;
import sapphire.imgui.SappImGuiLayer;

import java.util.HashMap;

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
