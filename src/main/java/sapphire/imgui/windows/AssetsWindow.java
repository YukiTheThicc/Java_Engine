package sapphire.imgui.windows;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import sapphire.imgui.SappImGUILayer;

public class AssetsWindow extends ImguiWindow {

    public AssetsWindow() {
        super("assets","Assets");
    }

    @Override
    public void imgui(SappImGUILayer layer) {

        ImGui.setNextWindowSize(400f, 400f, ImGuiCond.FirstUseEver);
        if (ImGui.begin(this.getTitle(), this.getFlags())) {

            if (ImGui.beginTabBar(this.getTitle(), this.getFlags())) {
                if (ImGui.beginTabItem("Sprites")) {
                    ImGui.endTabItem();
                }
                if (ImGui.beginTabItem("Tiles")) {
                    ImGui.endTabItem();
                }
                if (ImGui.beginTabItem("Sounds")) {
                    ImGui.endTabItem();
                }

                ImGui.endTabBar();
            }
        }

        ImGui.end();
    }
}
