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
            SappDrawable activeObj = Sapphire.getActiveObject();
            if (activeObj != null) {
                String name = activeObj.getClass().getSimpleName().toUpperCase();
                SappImGui.align(AlignX.CENTER, AlignY.TOP, SappImGui.textSize(name), ImGui.getFontSize());
                ImGui.text(name);
                ImGui.separator();
                activeObj.imgui();
            }
        }
        ImGui.end();
    }
}
