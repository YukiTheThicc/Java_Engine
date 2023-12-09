package sapphire.imgui.windows;

import diamondEngine.Camera;
import imgui.ImGui;
import imgui.type.ImFloat;
import sapphire.Sapphire;
import sapphire.imgui.SappImGuiLayer;
import sapphire.imgui.SappImGuiUtils;

public class EditorCameraWindow extends ImguiWindow {

    // ATTRIBUTES
    private Camera editorCamera;

    public EditorCameraWindow() {
        super("editor_camera", Sapphire.getLiteral("editor_camera"));
        this.editorCamera = GameViewWindow.editorCamera;
    }

    @Override
    public void imgui(SappImGuiLayer layer) {

        if (ImGui.begin(this.getTitle(), this.getFlags())) {

            ImFloat zoom = new ImFloat(editorCamera.zoom);
            if (SappImGuiUtils.dragFloat(Sapphire.getLiteral("zoom"), zoom)) editorCamera.zoom = zoom.get();
            SappImGuiUtils.drawVec2Control(Sapphire.getLiteral("position"), editorCamera.pos);
            SappImGuiUtils.drawVec2Control(Sapphire.getLiteral("projection_size"), editorCamera.getPSize());
            SappImGuiUtils.drawVec3Control(Sapphire.getLiteral("camera_front"), editorCamera.getFront());
            SappImGuiUtils.drawVec3Control(Sapphire.getLiteral("camera_up"), editorCamera.getUp());
            SappImGuiUtils.drawMatrix4f(Sapphire.getLiteral("projection_matrix"), editorCamera.getProjMatrix());
        }
        ImGui.end();
    }
}
