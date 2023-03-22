package sapphire.imgui.windows;

import imgui.ImGui;
import sapphire.Sapphire;
import sapphire.SapphireProject;
import sapphire.imgui.SappImGUILayer;

public class ProjectWindow extends ImguiWindow {

    // ATTRIBUTES
    private float dt;

    // CONSTRUCTORS
    public ProjectWindow() {
        super("project", "Project");
        this.dt = 0.0f;
    }

    // METHODS
    @Override
    public void imgui(SappImGUILayer layer) {

        ImGui.begin(this.getTitle());
        SapphireProject project = Sapphire.get().getProject();
        if (project != null) {
            if (project.getRoot().isAlive()) {
                ImGui.text("Loading...");
            }
            //ImGui.treeNode("", "");
        }
        ImGui.end();
    }
}
