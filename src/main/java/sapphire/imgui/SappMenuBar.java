package sapphire.imgui;

import imgui.ImGui;
import sapphire.Sapphire;
import sapphire.SapphireUtils;
import sapphire.imgui.windows.FileWindow;
import sapphire.imgui.windows.ImguiWindow;
import diamondEngine.diaUtils.DiaLogger;

import java.io.File;

public class SappMenuBar {

    // MENUS
    private static void fileMenu (SappImGUILayer layer) {
        // FILES Menu
        if (ImGui.beginMenu(Sapphire.getLiteral("file"))) {
            if (ImGui.menuItem(Sapphire.getLiteral("new_file"), "Ctrl+N")) {
                newFile(layer);
            }
            ImGui.separator();
            if (ImGui.menuItem(Sapphire.getLiteral("open_file"), "Ctrl+O")) {
                openFile(layer);
            }
            if (ImGui.menuItem(Sapphire.getLiteral("save_file"), "Ctrl+S")) {
                saveFile(layer);
            }
            if (ImGui.menuItem(Sapphire.getLiteral("save_as"), "Ctrl+Alt+S")) {
                saveFileAs(layer);
            }

            ImGui.separator();

            /*
            if (ImGui.menuItem(Sapphire.getLiteral("open_project"))) {
                //EventSystem.notify(null, new Event(EventType.SaveLevel));
            }
            if (ImGui.menuItem(Sapphire.getLiteral("export_project"))) {
                //EventSystem.notify(null, new Event(EventType.SaveLevel));
            }*/

            ImGui.separator();

            if (ImGui.menuItem(Sapphire.getLiteral("settings"))) {
                DiaLogger.log("Trying to open settings modal window");
                layer.getWindows().get("settings").setActive(true);
            }
            ImGui.endMenu();
        }
    }

    private static void windowsMenu(SappImGUILayer layer) {
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

    // ACTIONS
    private static void newFile(SappImGUILayer layer) {
        String fileName = Sapphire.getLiteral("new_file");
        if (layer.getWindows().get(fileName) != null) {
            // If the new file name has a number, retrieve that number and add 1 to it. If not, add ' 1' to the
            // new files name
            String[] splitName = fileName.split("(\\d+)(?!.*\\d)");
            if (splitName.length > 1) {
                int fileNumber = Integer.parseInt(splitName[splitName.length - 1]) + 1;
                fileName += " " + fileNumber;
            } else {
                fileName += "New File";
            }
        }
        layer.addWindow(new FileWindow(fileName, new File("")));
    }

    private static void openFile(SappImGUILayer layer) {
        String[] paths = SapphireUtils.selectFiles();
        if (paths != null) {
            for (String path : paths) {
                DiaLogger.log("Trying to open file on path '" + path + "'...");
                File newFile = new File(path);
                layer.addWindow(new FileWindow(newFile.getName(), newFile));
            }
        }
    }

    private static void saveFile(SappImGUILayer layer) {
        DiaLogger.log("Trying to save file");
    }

    private static void saveFileAs(SappImGUILayer layer) {

    }

    public static void imgui(SappImGUILayer layer) {
        ImGui.beginMenuBar();

        fileMenu(layer);
        windowsMenu(layer);

        ImGui.endMenuBar();
    }
}
