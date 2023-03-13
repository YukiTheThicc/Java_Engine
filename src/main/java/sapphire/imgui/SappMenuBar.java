package sapphire.imgui;

import imgui.ImGui;
import sapphire.Sapphire;
import sapphire.SapphireUtils;
import sapphire.imgui.windows.FileWindow;
import sapphire.imgui.windows.ImguiWindow;
import diamondEngine.diaUtils.DiaLogger;

import java.io.File;

public class SappMenuBar {

    private void fileMenu (SappImGUILayer layer) {
        // FILES Menu
        if (ImGui.beginMenu(Sapphire.getLiteral("file"))) {
            if (ImGui.menuItem(Sapphire.getLiteral("new_file"), "Ctrl+N")) {
                //EventSystem.notify(null, new Event(EventType.SaveLevel));
            }
            if (ImGui.menuItem(Sapphire.getLiteral("open_file"), "Ctrl+O")) {
                String[] paths = SapphireUtils.selectFiles();
                if (paths != null) {
                    for (String path : paths) {
                        DiaLogger.log("Trying to open file on path '" + path + "'...");
                        File newFile = new File(path);
                        layer.addWindow(new FileWindow(newFile.getName(), newFile));
                    }
                }
            }
            if (ImGui.menuItem(Sapphire.getLiteral("save_file"), "Ctrl+S")) {
                //EventSystem.notify(null, new Event(EventType.SaveLevel));
            }
            if (ImGui.menuItem(Sapphire.getLiteral("save_as"), "Ctrl+Alt+S")) {
                //EventSystem.notify(null, new Event(EventType.SaveLevel));
            }

            ImGui.separator();

            if (ImGui.menuItem(Sapphire.getLiteral("open_project"))) {
                //EventSystem.notify(null, new Event(EventType.SaveLevel));
            }
            if (ImGui.menuItem(Sapphire.getLiteral("export_project"))) {
                //EventSystem.notify(null, new Event(EventType.SaveLevel));
            }

            ImGui.separator();

            if (ImGui.menuItem(Sapphire.getLiteral("settings"))) {
                DiaLogger.log("Trying to open settings modal window");
                layer.getWindows().get("settings").setActive(true);
            }
            ImGui.endMenu();
        }
    }

    private void windowsMenu(SappImGUILayer layer) {
        // WINDOW Menu
        if (ImGui.beginMenu(Sapphire.getLiteral("window"))) {
            if (ImGui.beginMenu(Sapphire.getLiteral("active_windows"))) {
                for (String windowId : layer.getWindows().keySet()) {
                    ImguiWindow window = layer.getWindows().get(windowId);
                    if (window.isConfigurable() && ImGui.checkbox(window.getTitle(), window.isActive())) {
                        DiaLogger.log("Saving active windows");
                        Sapphire.get().getSettings().getActiveWindows().put(window.getId(), window.isActive().get());
                        Sapphire.get().getSettings().save();
                    }
                }
                ImGui.endMenu();
            }
            ImGui.endMenu();
        }
    }

    public void imgui(SappImGUILayer layer) {
        ImGui.beginMenuBar();

        fileMenu(layer);
        windowsMenu(layer);

        ImGui.endMenuBar();
    }
}
