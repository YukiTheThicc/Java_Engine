package diaEditor.imgui;

import diamondEngine.DiaConsole;
import diamondEngine.DiaUtils;
import imgui.ImGui;

public class MenuBar {

    public void imgui() {

        ImGui.beginMenuBar();
        // FILES Menu
        if (ImGui.beginMenu("File")) {
            if (ImGui.menuItem("Save", "Ctrl+S")) {
                //EventSystem.notify(null, new Event(EventType.SaveLevel));
            }
            if (ImGui.menuItem("Load", "Ctrl+L")) {
                //EventSystem.notify(null, new Event(EventType.LoadLevel));
            }
            ImGui.separator();
            if (ImGui.menuItem("Settings")) {
                //EventSystem.notify(null, new Event(EventType.LoadLevel));
            }
            ImGui.endMenu();
        }
        // WINDOW Menu
        if (ImGui.beginMenu("Window")) {
            if (ImGui.menuItem("Save", "Ctrl+S")) {
                //EventSystem.notify(null, new Event(EventType.SaveLevel));
            }
            if (ImGui.menuItem("Load", "Ctrl+L")) {
                //EventSystem.notify(null, new Event(EventType.LoadLevel));
            }
            ImGui.endMenu();
        }
        ImGui.endMenuBar();
    }
}
