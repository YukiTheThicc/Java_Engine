package sapphire.imgui.windows;

import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaUtils;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import sapphire.Sapphire;
import sapphire.SapphireEvents;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappEventType;
import sapphire.imgui.SappImGUILayer;
import sapphire.imgui.SappImGui;
import sapphire.imgui.components.SappImageButton;

public class AssetsWindow extends ImguiWindow {

    // ATTRIBUTES
    private float toolbarWidth;
    private final SappImageButton addAssetButton;
    private final SappImageButton removeAssetButton;
    private final SappImageButton copyAssetButton;

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
                if (ImGui.beginTabItem("Sounds")) {
                    ImGui.endTabItem();
                }

                ImGui.endTabBar();
            }
        }

        ImGui.end();
    }

    private void toolbar() {
        ImGui.pushStyleColor(ImGuiCol.Button, 0, 0, 0, 0);
        if (addAssetButton.draw()) SapphireEvents.notify(new SappEvent(SappEventType.Add_asset));
        if (removeAssetButton.draw());
        if (copyAssetButton.draw());
        ImGui.popStyleColor(1);
    }
}
