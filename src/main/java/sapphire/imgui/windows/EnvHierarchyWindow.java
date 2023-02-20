package sapphire.imgui.windows;

import imgui.ImGui;

public class EnvHierarchyWindow extends ImguiWindow {

    public EnvHierarchyWindow() {
        super("en_hierarchy", "Environment Hierarchy");
    }

    @Override
    public void imgui() {
        ImGui.showDemoWindow();
    }
}
