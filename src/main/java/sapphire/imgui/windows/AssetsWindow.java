package sapphire.imgui.windows;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.type.ImBoolean;
import sapphire.imgui.ImGUILayer;

public class AssetsWindow extends ImguiWindow {

    public AssetsWindow() {
        super("assets","Assets");
    }

    @Override
    public void imgui(ImGUILayer layer) {

        ImGui.setNextWindowSize(400f, 400f, ImGuiCond.FirstUseEver);
        ImGui.begin(this.getTitle());

        if (ImGui.beginTabBar(this.getTitle())) {
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

        ImGui.end();
    }
}
