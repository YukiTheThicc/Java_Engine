package sapphire.imgui.components;

import imgui.ImGui;
import imgui.ImGuiStyle;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import sapphire.Sapphire;
import sapphire.SapphireEvents;
import sapphire.events.SappEvent;
import sapphire.events.SappEventType;
import sapphire.imgui.AlignX;
import sapphire.imgui.AlignY;
import sapphire.imgui.SappImGui;

public class GameViewWindowControls {

    // ATTRIBUTES
    private final SappImageButton play;
    private final SappImageButton stop;
    private final float offsetY;
    private final float sizeY;

    // CONSTRUCTORS
    public GameViewWindowControls() {
        this.play = new SappImageButton(Sapphire.getIcon("play.png"), 40f, 40f);
        this.stop = new SappImageButton(Sapphire.getIcon("stop.png"), 40f, 40f);
        this.offsetY = ImGui.getStyle().getWindowPaddingY() + ImGui.getStyle().getFramePaddingY() * 2;
        this.sizeY = ImGui.getStyle().getWindowPaddingY() + ImGui.getStyle().getFramePaddingY() * 2;
    }

    // METHODS
    public void drawControls() {

        ImVec2 windowPos = ImGui.getWindowPos();
        ImVec2 windowSize = ImGui.getWindowSize();

        ImGui.setNextWindowPos(windowPos.x, windowPos.y + offsetY);
        ImGui.setNextWindowSize(windowSize.x, sizeY);
        ImGui.pushStyleColor(ImGuiCol.ChildBg, 50, 50, 50, 155);
        ImGui.pushStyleColor(ImGuiCol.Button, 0f, 0f, 0f, 0f);
        ImGui.pushStyleVar(ImGuiStyleVar.ChildRounding, 255f);
        ImGui.begin("controls", ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoDocking |
                ImGuiWindowFlags.NoBackground);

        SappImGui.align(AlignX.CENTER, AlignY.TOP, 100f, 40f);
        ImGui.beginChild("main_controls", 200f, 40f);
        SappImGui.align(AlignX.CENTER, AlignY.CENTER, 100f, 40f);
        if (play.draw()) {
            SapphireEvents.notify(new SappEvent(SappEventType.Play));
        }
        ImGui.sameLine();
        if (stop.draw()) {
            SapphireEvents.notify(new SappEvent(SappEventType.Stop));
        }

        ImGui.endChild();

        ImGui.popStyleVar(1);
        ImGui.popStyleColor(2);
        ImGui.end();
    }
}
