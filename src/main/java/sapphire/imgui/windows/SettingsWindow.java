package sapphire.imgui.windows;

import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImInt;
import sapphire.Sapphire;
import sapphire.SapphireSettings;
import sapphire.imgui.AlignX;
import sapphire.imgui.AlignY;
import sapphire.imgui.SappImGUILayer;
import sapphire.imgui.SappImGui;

public class SettingsWindow extends ImguiWindow {

    public SettingsWindow() {
        super("settings", "Settings", false);
        this.setFlags(ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);
        this.setActive(false);
        this.setSizeX(600f);
        this.setSizeY(600f);
    }

    @Override
    public void imgui(SappImGUILayer layer) {

        ImGui.setNextWindowSize(this.getSizeX(), this.getSizeY(), ImGuiCond.Always);
        if (this.isActive().get()) {
            ImGui.openPopup(this.getTitle());
            if (ImGui.beginPopupModal(this.getTitle(), this.isActive(), this.getFlags())) {

                SapphireSettings settings = Sapphire.get().getSettings();
                if (ImGui.beginTabBar(this.getTitle(), this.getFlags())) {
                    generalSettingsTab(settings);
                    styleSettingsTab(settings);
                    ImGui.endTabBar();
                }

                // Calculate close and accept buttons position
                float sizeX = SappImGui.textSize(settings.getLiteral("apply")) + SappImGui.textSize(settings.getLiteral("close")) + ImGui.getStyle().getFramePaddingX() * 6;
                float sizeY = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2;

                SappImGui.align(AlignX.RIGHT, AlignY.BOTTOM, sizeX, sizeY);
                if (ImGui.button(settings.getLiteral("apply"))) settings.save();
                ImGui.sameLine();
                if (ImGui.button(settings.getLiteral("close"))) this.setActive(false);

                ImGui.endPopup();
            }
        }
    }

    private void generalSettingsTab(SapphireSettings settings) {
        if (ImGui.beginTabItem(settings.getLiteral("general_settings"))) {

            settings.setFont(SappImGui.inputText("Font", settings.getFont()));
            /*
                settings.getLiteral("lang");
                settings.getLanguages();
                settings.changeLangTo(settings.getLanguages()[index.get()]);
                DiaLogger.log("Changed language to: " + settings.getLanguages()[index.get()]);*/
            //SapphireImGui.comboString(settings.getLiteral("lang"), settings.getCurrentLang(), settings.getLanguages());

            ImGui.endTabItem();
        }
    }

    private void styleSettingsTab(SapphireSettings settings) {
        if (ImGui.beginTabItem(settings.getLiteral("style_settings"))) {

            String[] colors = {};
            colors = settings.getColors().keySet().toArray(colors);
            ImInt index = new ImInt();
            if (ImGui.combo(Sapphire.getLiteral("Color"), index, colors)) {
                DiaLogger.log("Selected color: " + colors[index.get()]);
            }

            ImGui.endTabItem();
        }
    }
}

