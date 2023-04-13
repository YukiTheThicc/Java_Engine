package sapphire.imgui.windows;

import imgui.ImGui;
import sapphire.imgui.SappImGUILayer;

public class EnvPreviewWindow extends ImguiWindow {

    public EnvPreviewWindow() {
        super("env_preview", "Environment Preview");
    }

    @Override
    public void imgui(SappImGUILayer layer) {

        if (ImGui.begin(this.getTitle(), this.getFlags())) {

        }

        ImGui.end();
    }
}
