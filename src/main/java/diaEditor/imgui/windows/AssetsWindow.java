package diaEditor.imgui.windows;

import imgui.ImGui;

public class AssetsWindow extends ImguiWindow {

    public AssetsWindow() {
        super("Assets");
    }

    @Override
    public void imgui() {

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
