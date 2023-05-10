package sapphire.imgui.windows;

import diamondEngine.diaAudio.Sound;
import diamondEngine.diaRenderer.Texture;
import diamondEngine.diaUtils.DiaAssetManager;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import sapphire.Sapphire;
import sapphire.SappEvents;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappEventType;
import sapphire.imgui.SappImGUILayer;
import sapphire.imgui.SappImGui;
import sapphire.imgui.components.SappImageButton;

import java.io.File;

public class AssetsWindow extends ImguiWindow {

    // ATTRIBUTES
    private float toolbarWidth;
    private final SappImageButton addAssetButton;
    private final SappImageButton removeAssetButton;
    private final SappImageButton copyAssetButton;
    private final int buttonSize = ImGui.getFontSize() * 5;
    private final int imageSize = ImGui.getFontSize() * 4;

    public AssetsWindow() {
        super("assets", "Assets");
        this.toolbarWidth = SappImGui.SMALL_ICON_SIZE + ImGui.getStyle().getFramePaddingX() * 4;
        this.addAssetButton = new SappImageButton(Sapphire.getIcon("add.png"), SappImGui.SMALL_ICON_SIZE, SappImGui.SMALL_ICON_SIZE);
        this.removeAssetButton = new SappImageButton(Sapphire.getIcon("trash.png"), SappImGui.SMALL_ICON_SIZE, SappImGui.SMALL_ICON_SIZE);
        this.copyAssetButton = new SappImageButton(Sapphire.getIcon("copy.png"), SappImGui.SMALL_ICON_SIZE, SappImGui.SMALL_ICON_SIZE);
    }

    @Override
    public void imgui(SappImGUILayer layer) {

        ImGui.setNextWindowSize(400f, 400f, ImGuiCond.FirstUseEver);
        if (ImGui.begin(this.getTitle(), this.getFlags())) {
            ImGui.columns(2);
            ImGui.setColumnWidth(0, toolbarWidth);
            toolbar();
            ImGui.nextColumn();
            if (ImGui.beginTabBar(this.getTitle(), this.getFlags())) {
                if (ImGui.beginTabItem("Sprites")) {
                    ImGui.endTabItem();
                }
                if (ImGui.beginTabItem("Tiles")) {
                    ImGui.endTabItem();
                }
                drawTextureTab();
                drawSoundTab();
                ImGui.endTabBar();
            }
        }

        ImGui.end();
    }

    private void toolbar() {
        ImGui.pushStyleColor(ImGuiCol.Button, 0, 0, 0, 0);
        if (addAssetButton.draw()) SappEvents.notify(new SappEvent(SappEventType.Add_asset));
        if (removeAssetButton.draw());
        if (copyAssetButton.draw());
        ImGui.popStyleColor(1);
    }

    private void drawSoundTab() {
        if (ImGui.beginTabItem("Sounds")) {

            for (Sound s : DiaAssetManager.getAllSounds()) {
                ImGui.pushID(s.getPath());
                File tmp = new File(s.getPath());
                ImGui.selectable(tmp.getName());
                if (ImGui.getContentRegionAvailX() > 500) {
                    ImGui.sameLine();
                }
                ImGui.popID();
            }
            ImGui.endTabItem();
        }
    }

    private void drawTextureTab() {
        if (ImGui.beginTabItem("Textures")) {

            for (Texture t : DiaAssetManager.getAllTextures()) {
                ImGui.pushID(t.getPath());
                drawTexture(t);
                if (ImGui.getContentRegionAvailX() > 500) {
                    ImGui.sameLine();
                }
                ImGui.popID();
            }
            ImGui.endTabItem();
        }
    }

    private void drawTexture(Texture tex) {
        ImVec2 origin = ImGui.getCursorPos();
        ImGui.button("##" + tex.getPath(), buttonSize, buttonSize);
        ImGui.sameLine();
        float ratio = (float) tex.getHeight() / tex.getWidth();
        ImGui.setCursorPos(origin.x + ((float) buttonSize - imageSize) / 2, origin.y);
        ImGui.image(tex.getId(), imageSize, imageSize * ratio, 0, 1, 1, 0);
    }
}
