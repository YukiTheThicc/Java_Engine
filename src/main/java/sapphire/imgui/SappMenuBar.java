package sapphire.imgui;

import imgui.ImGui;
import sapphire.Sapphire;
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
            if (ImGui.menuItem(Sapphire.getLiteral("new_env"), "Ctrl+N"))
                SapphireEvents.notify(new SappEvent(SappEventType.Add_env));
            ImGui.separator();
            if (ImGui.menuItem(Sapphire.getLiteral("import_env"), "Ctrl+I"))
                SapphireEvents.notify(new SappEvent(SappEventType.Import_env));
            if (ImGui.menuItem(Sapphire.getLiteral("save_env"), "Ctrl+S"))
                SapphireEvents.notify(new SappEvent(SappEventType.Save_env));
            if (ImGui.menuItem(Sapphire.getLiteral("save_env_as"), "Ctrl+Alt+S"))
                SapphireEvents.notify(new SappEvent(SappEventType.Save_env_as));
            ImGui.separator();

            // Project menu items
            if (ImGui.menuItem(Sapphire.getLiteral("create_project")))
                SapphireEvents.notify(new SappEvent(SappEventType.Create_project));
            if (ImGui.menuItem(Sapphire.getLiteral("open_project")))
                SapphireEvents.notify(new SappEvent(SappEventType.Open_project));
            if (ImGui.menuItem(Sapphire.getLiteral("export_project")))
                SapphireEvents.notify(new SappEvent(SappEventType.Export_project));

            ImGui.separator();
            if (ImGui.menuItem(Sapphire.getLiteral("settings")))
                SapphireEvents.notify(new SappEvent(SappEventType.Settings));
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
