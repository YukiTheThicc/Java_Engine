package sapphire.imgui.windows;

import imgui.ImGui;
import sapphire.Sapphire;
import sapphire.imgui.SappImGUILayer;

public class ProfilerWindow extends ImguiWindow {

    public ProfilerWindow() {
        super("profiler", "Profiler");
    }

    @Override
    public void imgui(SappImGUILayer layer) {

        ImGui.begin(this.getTitle(), this.getFlags());

        int fps = (int)(1 / Sapphire.get().getContainer().getDt());
        ImGui.labelText("FPS", String.valueOf(fps));

        ImGui.end();
    }
}
