package sapphire.imgui;

import imgui.ImGui;
import sapphire.Sapphire;
import sapphire.SapphireUtils;
import sapphire.imgui.windows.FileWindow;
import sapphire.imgui.windows.ImguiWindow;
import diamondEngine.diaUtils.DiaConsole;

import java.io.File;

public class MenuBar {

    public void imgui(ImGUILayer layer) {
        ImGui.beginMenuBar();

        // FILES Menu
        if (ImGui.beginMenu("File")) {
            if (ImGui.menuItem("New File", "Ctrl+N")) {
                //EventSystem.notify(null, new Event(EventType.SaveLevel));
            }
            if (ImGui.menuItem("Open File", "Ctrl+O")) {
                String[] paths = SapphireUtils.selectFiles();
                for (int i = 0; i < paths.length; i++) {
                    DiaConsole.log("Trying to open file on path '" + paths[i] + "'...", "debug");
                    Sapphire.get().addOpenedFile(new File(paths[i]));
                }
            }
            if (ImGui.menuItem("Save File", "Ctrl+S")) {
                //EventSystem.notify(null, new Event(EventType.SaveLevel));
            }

            ImGui.separator();

            if (ImGui.menuItem("Open Project")) {
                //EventSystem.notify(null, new Event(EventType.SaveLevel));
            }
            if (ImGui.menuItem("Export Project")) {
                //EventSystem.notify(null, new Event(EventType.SaveLevel));
            }

            ImGui.separator();

            if (ImGui.menuItem("Settings")) {
                //EventSystem.notify(null, new Event(EventType.SaveLevel));
            }
            ImGui.endMenu();
        }

        // WINDOW Menu
        if (ImGui.beginMenu("Window")) {
            if (ImGui.beginMenu("Active windows")) {
                for (String windowId : layer.getWindows().keySet()) {
                    ImguiWindow window = layer.getWindows().get(windowId);
                    if (!(window instanceof FileWindow) && ImGui.checkbox(window.getTitle(), window.isActive())) {
                        DiaConsole.log("Saving active windows", "debug");
                        Sapphire.get().getSettings().getActiveWindows().put(window.getId(), window.isActive().get());
                        Sapphire.get().getSettings().save();
                    }
                }
                ImGui.endMenu();
            }
            ImGui.endMenu();
        }

        ImGui.endMenuBar();
    }
}
