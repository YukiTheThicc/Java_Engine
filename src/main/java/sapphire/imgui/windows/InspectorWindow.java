package sapphire.imgui.windows;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import sapphire.Sapphire;
import sapphire.imgui.*;

public class InspectorWindow extends ImguiWindow {

    public InspectorWindow() {
        super("inspector", Sapphire.getLiteral("inspector_window"));
    }

    @Override
    public void imgui(SappImGuiLayer layer) {

        ImGui.setNextWindowSize(400f, 400f, ImGuiCond.FirstUseEver);
        if (ImGui.begin(this.getTitle(), this.getFlags())) {
            SappInspectable activeObj = Sapphire.getActiveObject();
            if (activeObj != null) {
                String name = activeObj.getClass().getSimpleName().toUpperCase();
                SappImGuiUtils.align(AlignX.CENTER, AlignY.TOP, SappImGuiUtils.textSize(name), ImGui.getFontSize());
                ImGui.text(name);
                ImGui.separator();
                activeObj.inspect();
            }
        }
        ImGui.end();
    }
}
