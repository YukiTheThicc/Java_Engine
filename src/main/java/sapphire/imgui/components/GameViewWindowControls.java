package sapphire.imgui.components;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import sapphire.Sapphire;
import sapphire.SapphireEvents;
import sapphire.eventsSystem.events.SappEvent;
import sapphire.eventsSystem.eventTypes.SappEventType;
import sapphire.imgui.AlignX;
import sapphire.imgui.AlignY;
import sapphire.imgui.SappImGui;

public class GameViewWindowControls {

    // ATTRIBUTES
    private final SappImageButton play;
    private final SappImageButton stop;
    private final SappImageButton settings;
    private final float offsetY;
    private final float iconSizeX = 48f;
    private final float iconSizeY = 48f;
    private final float mainControlsX;
    private final float mainControlsY;
    private final ImBoolean menuOpen = new ImBoolean(false);
    private final int flags = ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize |
            ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoScrollbar |
            ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.NoSavedSettings | ImGuiWindowFlags.NoBackground;

    // CONSTRUCTORS
    public GameViewWindowControls() {
        this.play = new SappImageButton(Sapphire.getIcon("play.png"), iconSizeX, iconSizeY);
        this.stop = new SappImageButton(Sapphire.getIcon("stop.png"), iconSizeX, iconSizeY);
        this.settings = new SappImageButton(Sapphire.getIcon("gear.png"), iconSizeX, iconSizeY);
        this.offsetY = ImGui.getTextLineHeightWithSpacing();
        this.mainControlsX = iconSizeX * 4 + ImGui.getStyle().getFramePaddingX() * 3;
        this.mainControlsY = iconSizeY + ImGui.getStyle().getFramePaddingX() * 2;
    }

    // METHODS
    public void drawControls() {

        ImVec2 windowPos = ImGui.getWindowPos();
        ImVec2 windowSize = ImGui.getWindowSize();

        ImGui.setNextWindowPos(windowPos.x, windowPos.y + offsetY);
        ImGui.setNextWindowSize(windowSize.x, mainControlsY);
        ImGui.pushStyleColor(ImGuiCol.ChildBg, 50, 50, 50, 125);
        ImGui.pushStyleColor(ImGuiCol.Button, 0f, 0f, 0f, 0f);
        ImGui.pushStyleVar(ImGuiStyleVar.ChildRounding, 255f);
        ImGui.begin("controls", flags);

        SappImGui.alignNoHeader(AlignX.CENTER, AlignY.TOP, mainControlsX, mainControlsY);
        ImGui.beginChild("main_controls", mainControlsX, mainControlsY);
        SappImGui.align(AlignX.CENTER, AlignY.CENTER, iconSizeX * 2 + ImGui.getStyle().getFramePaddingX(), iconSizeY);
        if (play.draw()) {
            SapphireEvents.notify(new SappEvent(SappEventType.Play));
        }
        ImGui.sameLine();
        if (stop.draw()) {
            SapphireEvents.notify(new SappEvent(SappEventType.Stop));
        }
        ImGui.endChild();

        SappImGui.alignNoHeader(AlignX.RIGHT, AlignY.CENTER, iconSizeX + ImGui.getStyle().getFramePaddingX() * 2, mainControlsY);
        ImGui.beginChild("settings", iconSizeX + ImGui.getStyle().getFramePaddingX() * 2, mainControlsY);
        SappImGui.align(AlignX.CENTER, AlignY.CENTER, iconSizeX, iconSizeY);
        if (settings.draw()) {
            SapphireEvents.notify(new SappEvent(SappEventType.View_Port_Settings));
            ImGui.openPopup("view_settings");
        }
        gameViewSettings();
        ImGui.endChild();

        ImGui.popStyleVar(1);
        ImGui.popStyleColor(2);
        ImGui.end();
    }

    public void gameViewSettings() {

        if (ImGui.beginPopup("view_settings")) {

            if (ImGui.menuItem("item 1")) {

            }
            if (ImGui.menuItem("item 2")) {

            }
            if (ImGui.menuItem("item 3")) {

            }
            if (ImGui.menuItem("item 4")) {

            }

            ImGui.endPopup();
        }
    }
}
