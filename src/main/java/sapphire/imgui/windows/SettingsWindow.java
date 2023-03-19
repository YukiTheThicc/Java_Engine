package sapphire.imgui.windows;

import diamondEngine.Window;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaUtils;
import imgui.*;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImInt;
import sapphire.Sapphire;
import sapphire.SapphireSettings;
import sapphire.imgui.AlignX;
import sapphire.imgui.AlignY;
import sapphire.imgui.SappImGUILayer;
import sapphire.imgui.SappImGui;

import java.io.File;
import java.util.ArrayList;

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
            ImGui.setNextWindowPos(Window.getPosition().x + (Window.getWidth() - this.getSizeX()) / 2,
                    Window.getPosition().y + (Window.getHeight() - this.getSizeY()) / 2);
            ImGui.openPopup(this.getTitle());
            if (ImGui.beginPopupModal(this.getTitle(), this.isActive(), this.getFlags())) {

                ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 6, 6);
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

                ImGui.popStyleVar(1);
                ImGui.endPopup();
            }
        }
    }

    private void generalSettingsTab(SapphireSettings settings) {
        if (ImGui.beginTabItem(settings.getLiteral("general_settings"))) {

            // Font
            String newFont = SappImGui.combo(settings.getLiteral("font"), settings.getCurrentFont(), settings.getFonts());
            if (newFont != null) {
                settings.setCurrentFont(newFont);
            }
            ImGui.sameLine();
            ImInt fontSize = new ImInt(settings.getFontSize());
            if (ImGui.inputInt(settings.getLiteral("font"), fontSize)) settings.setFontSize(fontSize.get());

            // Language
            String newLang = SappImGui.combo(settings.getLiteral("lang"), settings.getCurrentLang(), settings.getLanguages());
            if (newLang != null) {
                settings.changeLangTo(newLang);
            }
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

