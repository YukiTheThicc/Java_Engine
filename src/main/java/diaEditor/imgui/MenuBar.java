package diaEditor.imgui;

import diaEditor.DiaFrontSettings;
import diaEditor.FrontEnd;
import diaEditor.imgui.windows.ImguiWindow;
import diamondEngine.diaUtils.DiaConsole;
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
            if (ImGui.menuItem("Settings", "Ctrl+Shift+S")) {
                //EventSystem.notify(null, new Event(EventType.SaveLevel));
            }
            ImGui.endMenu();
        }

        // WINDOW Menu
        if (ImGui.beginMenu("Window")) {
            if (ImGui.beginMenu("Active windows")) {
                for (ImguiWindow window : layer.getWindows()) {
                    if (ImGui.checkbox(window.getTitle(), window.isActive())) {
                        DiaConsole.log("Saving active windows", "debug");
                        FrontEnd.get().getSettings().getActiveWindows().put(window.getId(), window.isActive().get());
                        FrontEnd.get().getSettings().save();
                    }
                }
                ImGui.endMenu();
            }
            ImGui.endMenu();
        }

        ImGui.endMenuBar();
    }
}
