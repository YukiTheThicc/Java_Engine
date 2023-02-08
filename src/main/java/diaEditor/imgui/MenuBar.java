package diaEditor.imgui;

import diamondEngine.DiaConsole;
import diamondEngine.DiaUtils;
import imgui.ImGui;

public class MenuBar {

    public void imgui() {

        ImGui.beginMenuBar();
        // FILES Menu
        if (ImGui.beginMenu("File")) {
            if (ImGui.menuItem("Open Project", "Ctrl+S")) {
                //EventSystem.notify(null, new Event(EventType.SaveLevel));
            }
            if (ImGui.menuItem("Export Project", "Ctrl+S")) {
                //EventSystem.notify(null, new Event(EventType.SaveLevel));
            }
            ImGui.separator();
            ImGui.endMenu();
        }
        // WINDOW Menu
        if (ImGui.beginMenu("Window")) {
            ImGui.endMenu();
        }
        ImGui.endMenuBar();
    }
}
