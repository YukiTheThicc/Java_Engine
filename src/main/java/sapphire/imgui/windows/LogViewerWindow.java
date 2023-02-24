package sapphire.imgui.windows;

import imgui.ImGui;
import sapphire.imgui.ImGUILayer;

import java.io.File;

public class LogViewerWindow extends ImguiWindow {

    private File log;

    public LogViewerWindow() {
        super("log_viewer", "Log Viewer");
    }

    @Override
    public void imgui(ImGUILayer layer) {

        ImGui.begin(this.getTitle());

        ImGui.end();
    }
}
