package sapphire.imgui.windows;

import diamondEngine.diaRenderer.Texture;
import imgui.ImGui;
import imgui.ImVec2;
import sapphire.Sapphire;
import sapphire.SapphireDir;
import sapphire.SapphireProject;
import sapphire.imgui.AlignX;
import sapphire.imgui.AlignY;
import sapphire.imgui.SappImGUILayer;
import sapphire.imgui.SappImGui;

import java.io.File;

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
                SappImGui.align(AlignX.CENTER, AlignY.CENTER, SappImGui.textSize(Sapphire.getLiteral("loading")), ImGui.getFontSize());
                ImGui.text(Sapphire.getLiteral("loading"));
            } else {
                drawFile(project.getRoot());
            }
        }
        ImGui.end();
    }

    private void drawFile(SapphireDir dir) {
        if (ImGui.treeNode(dir.getPath().getName())) {
            for (SapphireDir nestedDir : dir.getDirs()) {
                drawFile(nestedDir);
            }
            String iconFile;
            Texture tex;
            for (File file : dir.getFiles()) {
                iconFile = file.getName().substring(file.getName().lastIndexOf('.') + 1) + ".png";
                tex = Sapphire.getIcon(iconFile);
                ImGui.image(tex.getId(), tex.getWidth(), tex.getHeight(),0, 1,1, 0);
                ImGui.sameLine();
                ImGui.text(file.getName());
            }
            ImGui.treePop();
            ImGui.spacing();
        }
    }
}
