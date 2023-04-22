package sapphire.imgui;

import imgui.ImGui;
import sapphire.Sapphire;
import sapphire.SapphireActions;
import sapphire.SapphireEvents;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappEventType;
import sapphire.imgui.windows.ImguiWindow;
import diamondEngine.diaUtils.DiaLogger;

public class SappMenuBar {

    // MENUS
    private static void fileMenu (SappImGUILayer layer) {
        // FILES Menu
        if (ImGui.beginMenu(Sapphire.getLiteral("file"))) {

            // File menu items
            if (ImGui.menuItem(Sapphire.getLiteral("new_file"), "Ctrl+N")) {
                SapphireEvents.notify(new SappEvent(SappEventType.New_file));
                SapphireActions.newFile(layer);
            }
            ImGui.separator();

            if (ImGui.menuItem(Sapphire.getLiteral("open_file"), "Ctrl+O")) {
                SapphireActions.openFile(layer);
            }
            if (ImGui.menuItem(Sapphire.getLiteral("save_file"), "Ctrl+S")) {
                SapphireActions.saveFile(layer);
            }
            if (ImGui.menuItem(Sapphire.getLiteral("save_as"), "Ctrl+Alt+S")) {
                SapphireActions.saveFileAs(layer);
            }
            ImGui.separator();

            // Project menu items
            if (ImGui.menuItem(Sapphire.getLiteral("create_project"))) {
                SapphireActions.createProject();
            }
            if (ImGui.menuItem(Sapphire.getLiteral("open_project"))) {
                SapphireActions.openProject();
            }
            if (ImGui.menuItem(Sapphire.getLiteral("export_project"))) {
                SapphireActions.exportProject(layer);
            }

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

    public static void imgui(SappImGUILayer layer) {
        ImGui.beginMenuBar();

        fileMenu(layer);
        windowsMenu(layer);

        ImGui.endMenuBar();
    }
}
