package sapphire.imgui.windows;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.type.ImBoolean;
import sapphire.imgui.ImGUILayer;

public class EnvHierarchyWindow extends ImguiWindow {

    public EnvHierarchyWindow() {
        super("env_hierarchy", "Environment Hierarchy");
    }

    @Override
    public void imgui(ImGUILayer layer) {

        ImGui.setNextWindowSize(400f, 400f, ImGuiCond.FirstUseEver);
        ImGui.showDemoWindow(new ImBoolean(true));
    }
}
