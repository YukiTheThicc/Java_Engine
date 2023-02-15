package diaEditor.imgui;

import diaEditor.imgui.windows.ImguiWindow;
import diamondEngine.DiaConsole;
import diamondEngine.DiaUtils;
import imgui.ImGui;

public class MenuBar {

    public void imgui(ImGUILayer layer) {
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
            if (ImGui.beginMenu("Active windows")) {
                for (ImguiWindow window : layer.getWindows()) {
                    if (ImGui.checkbox(window.getTitle(), window.isActive())) {
                        DiaConsole.log("Saving active windows", "debug");
                    }
                }
                ImGui.endMenu();
            }
            ImGui.endMenu();
        }

        ImGui.endMenuBar();
    }
}
