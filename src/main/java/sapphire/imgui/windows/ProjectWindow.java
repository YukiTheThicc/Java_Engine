package sapphire.imgui.windows;

import imgui.ImGui;
import sapphire.imgui.SappImGUILayer;

public class ProjectWindow extends ImguiWindow {

    // CONSTRUCTORS
    public ProjectWindow() {
        super("project", "Project");
    }

    // METHODS
    @Override
    public void imgui(SappImGUILayer layer) {

        ImGui.begin(this.getTitle());

        ImGui.end();
    }
}
