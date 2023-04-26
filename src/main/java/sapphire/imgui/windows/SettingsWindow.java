package sapphire.imgui.windows;

import diamondEngine.Window;
import diamondEngine.diaUtils.DiaUtils;
import imgui.*;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImInt;
import sapphire.Sapphire;
import sapphire.SapphireEvents;
import sapphire.SapphireSettings;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappEventType;
import sapphire.imgui.SappImGUILayer;
import sapphire.imgui.SappImGui;

public class SettingsWindow extends ImguiWindow {

    // ATTRIBUTES
    private boolean settingsChanged = false;

    // CONSTRUCTORS
    public SettingsWindow() {
        super("settings", "Settings", false);
        this.setFlags(ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);
        this.setActive(false);
        this.setSizeX(600f);
        this.setSizeY(600f);
    }

    // METHODS
    @Override
    public void imgui(SappImGUILayer layer) {

        ImGui.setNextWindowSize(this.getSizeX(), this.getSizeY(), ImGuiCond.Always);
        if (this.isActive().get()) {

            ImGui.setNextWindowPos(Window.getPosition().x + (Window.getWidth() - this.getSizeX()) / 2,
                    Window.getPosition().y + (Window.getHeight() - this.getSizeY()) / 2);
            ImGui.openPopup(this.getTitle());
            SapphireSettings settings = Sapphire.get().getSettings();
            if (ImGui.beginPopupModal(this.getTitle(), this.isActive(), this.getFlags())) {
                if (ImGui.beginTabBar(this.getTitle(), this.getFlags())) {
                    generalSettingsTab(settings, layer);
                    ImGui.endTabBar();
                }
                checkChanges(settings);
                ImGui.endPopup();
            }
        }
    }

    private void checkChanges(SapphireSettings settings) {
        if (settingsChanged) {
            SapphireEvents.notify(new SappEvent(SappEventType.Settings_changed));
            settings.save();
            settingsChanged = false;
        }
    }

    private void generalSettingsTab(SapphireSettings settings, SappImGUILayer layer) {
        if (ImGui.beginTabItem(Sapphire.getLiteral("general_settings"))) {

            // Workspace
            settings.setWorkspace(SappImGui.inputText(Sapphire.getLiteral("workspace"), settings.getWorkspace()));
            ImGui.sameLine();
            if (ImGui.button(Sapphire.getLiteral("examine"))) {
                settings.setWorkspace(DiaUtils.selectDirectory(Sapphire.getLiteral("choose_workspace"), settings.getWorkspace()));
            }
            ImGui.separator();

            // Font
            String newFont = SappImGui.combo(Sapphire.getLiteral("font"), settings.getCurrentFont(), settings.getFontsList());
            if (newFont != null) {
                layer.changeFont(newFont);
                settingsChanged = true;
            }

            ImGui.sameLine();
            ImInt fontSize = new ImInt(settings.getFontSize());
            if (ImGui.inputInt("##", fontSize)) {
                settings.setFontSize(fontSize.get());
                settingsChanged = true;
            }

            if (ImGui.isItemHovered()) {
                ImGui.beginTooltip();
                ImGui.text(Sapphire.getLiteral("font_change_after_reboot"));
                ImGui.endTooltip();
            }

            // Language
            String newLang = SappImGui.combo(Sapphire.getLiteral("lang"), settings.getCurrentLang(), settings.getLanguages());
            if (newLang != null) {
                settings.changeLangTo(newLang);
                settingsChanged = true;
            }
            ImGui.endTabItem();
        }
    }
}

