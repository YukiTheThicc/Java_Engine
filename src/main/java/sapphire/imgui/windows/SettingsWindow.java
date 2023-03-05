package sapphire.imgui.windows;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import sapphire.Sapphire;
import sapphire.SapphireSettings;
import sapphire.imgui.ImGUILayer;
import sapphire.imgui.SapphireImGui;

public class SettingsWindow extends ImguiWindow {

    private final float sizeX;
    private final float sizeY;

    public SettingsWindow() {
        super("settings", "Settings", false);
        this.setFlags(ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove);
        this.setActive(false);
        this.sizeX = 600f;
        this.sizeY = 600f;
    }

    @Override
    public void imgui(ImGUILayer layer) {

        ImGui.setNextWindowSize(this.sizeX, this.sizeY, ImGuiCond.Always);
        if (this.isActive().get()) {
            ImGui.openPopup(this.getTitle());
            if (ImGui.beginPopupModal(this.getTitle(), this.isActive(), this.getFlags())) {

                SapphireSettings settings = Sapphire.get().getSettings();

                settings.setFont(SapphireImGui.inputText("Font", settings.getFont()));

                // Calculate close and accept buttons position
                float applyButton = settings.getLiteral("apply").length() * ImGui.getFontSize() + ImGui.getStyle().getCellPaddingX();
                float closeButton = settings.getLiteral("close").length() * ImGui.getFontSize() + ImGui.getStyle().getCellPaddingX();
                float applyOffset = sizeX - applyButton - closeButton + ImGui.getStyle().getWindowPaddingX();
                float closeOffset = sizeX - closeButton - ImGui.getStyle().getCellPaddingX() + ImGui.getStyle().getWindowPaddingX();
                float yOffset = sizeY - ImGui.getFontSize() - ImGui.getStyle().getCellPaddingY() * 2 - ImGui.getStyle().getWindowPaddingY() * 2;

                ImGui.setCursorPos(applyOffset, yOffset);
                if (ImGui.button(settings.getLiteral("apply"))) {
                    settings.save();
                }

                ImGui.setCursorPos(closeOffset, yOffset);
                if (ImGui.button(settings.getLiteral("close"))) {
                    this.setActive(false);
                }

                ImGui.endPopup();
            }
        }
    }
}

