package sapphire.imgui.windows;

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
            ImInt iFont = new ImInt(1);
            if (SappImGui.combo(settings.getLiteral("font"), iFont, settings.getFonts())) settings.setCurrentFont(settings.getFont(settings.getFonts()[iFont.get()]));
            ImGui.sameLine();
            ImInt fontSize = new ImInt(settings.getFontSize());
            if (ImGui.inputInt(settings.getLiteral("font"), fontSize)) settings.setFontSize(fontSize.get());

            // Language
            ImInt iLang = new ImInt();
            SappImGui.combo(settings.getLiteral("lang"), iLang, settings.getLanguages());

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

