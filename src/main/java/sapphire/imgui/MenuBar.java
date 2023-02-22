package sapphire.imgui;

import imgui.ImGui;
import sapphire.Sapphire;
import sapphire.SapphireUtils;
import sapphire.imgui.windows.FileWindow;
import sapphire.imgui.windows.ImguiWindow;
import diamondEngine.diaUtils.DiaConsole;

import java.io.File;

public class MenuBar {

    private void fileMenu (ImGUILayer layer) {
        // FILES Menu
        if (ImGui.beginMenu("File")) {
            if (ImGui.menuItem("New File", "Ctrl+N")) {
                //EventSystem.notify(null, new Event(EventType.SaveLevel));
            }
            if (ImGui.menuItem("Open File", "Ctrl+O")) {
                String[] paths = SapphireUtils.selectFiles();
                if (paths != null) {
                    for (String path : paths) {
                        DiaConsole.log("Trying to open file on path '" + path + "'...", "debug");
                        File newFile = new File(path);
                        layer.addWindow(new FileWindow(newFile.getName(), newFile));
                    }
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
                DiaConsole.log("Trying to open settings modal window");
                layer.getWindows().get("settings").setActive(true);
            }
            ImGui.endMenu();
        }
    }

    private void windowsMenu(ImGUILayer layer) {
        // WINDOW Menu
        if (ImGui.beginMenu("Window")) {
            if (ImGui.beginMenu("Active windows")) {
                for (String windowId : layer.getWindows().keySet()) {
                    ImguiWindow window = layer.getWindows().get(windowId);
                    if (window.isConfigurable() && ImGui.checkbox(window.getTitle(), window.isActive())) {
                        DiaConsole.log("Saving active windows", "debug");
                        Sapphire.get().getSettings().getActiveWindows().put(window.getId(), window.isActive().get());
                        Sapphire.get().getSettings().save();
                    }
                }
                ImGui.endMenu();
            }
            ImGui.endMenu();
        }
    }

    public void imgui(ImGUILayer layer) {
        ImGui.beginMenuBar();

        fileMenu(layer);
        windowsMenu(layer);

        ImGui.endMenuBar();
    }
}
