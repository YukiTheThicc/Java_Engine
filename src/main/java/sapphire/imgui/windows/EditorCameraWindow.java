package sapphire.imgui.windows;

import diamondEngine.diaComponents.Camera;
import imgui.ImGui;
import sapphire.Sapphire;
import sapphire.imgui.SappImGUILayer;
import sapphire.imgui.SappImGui;

public class EditorCameraWindow extends ImguiWindow {

    // ATTRIBUTES
    private Camera editorCamera;

    public EditorCameraWindow() {
        super("editor_camera", Sapphire.getLiteral("editor_camera"));
        this.editorCamera = GameViewWindow.editorCamera;
    }

    @Override
    public void imgui(SappImGUILayer layer) {

        if (ImGui.begin(this.getTitle(), this.getFlags())) {

            editorCamera.setZoom(SappImGui.dragFloat(Sapphire.getLiteral("zoom"), editorCamera.getZoom()));
            SappImGui.drawVec2Control(Sapphire.getLiteral("position"), editorCamera.pos);
            SappImGui.drawVec2Control(Sapphire.getLiteral("projection_size"), editorCamera.getPSize());
            SappImGui.drawMatrix4f(Sapphire.getLiteral("projection_matrix"), editorCamera.getProjMatrix());
        }
        ImGui.end();
    }
}
