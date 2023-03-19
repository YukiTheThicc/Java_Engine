package sapphire.imgui.windows;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import sapphire.imgui.SappImGUILayer;

public class EnvHierarchyWindow extends ImguiWindow {

    public EnvHierarchyWindow() {
        super("env_hierarchy", "Environment Hierarchy");
    }

    @Override
    public void imgui(SappImGUILayer layer) {

        ImGui.setNextWindowSize(400f, 400f, ImGuiCond.FirstUseEver);
        ImGui.begin(this.getTitle(), this.getFlags());

        ImGui.end();
    }
}
