package sapphire.imgui.windows;

import diamondEngine.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;
import sapphire.imgui.SappImGUILayer;

public class GameViewWindow extends ImguiWindow {

    // ATTRIBUTES
    private int leftX, rightX, topY, bottomY;

    // CONSTRUCTORS
    public GameViewWindow() {
        super("game_view", "Game View");
        this.setFlags(ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.MenuBar);
    }

    // METHODS
    private ImVec2 getLargestSizeForViewport() {

        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Window.getTargetAspectRatio();

        if (aspectHeight > windowSize.y) {
            // We must switch to pillarbox mode
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    private ImVec2 getCenteredPositionForViewport(ImVec2 aspectSize) {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);

        float viewportX = (windowSize.x / 2.0f) - (aspectSize.x / 2.0f);
        float viewportY = (windowSize.y / 2.0f) - (aspectSize.y / 2.0f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
    }

    public void imgui(SappImGUILayer layer) {

        imgui.internal.ImGui.setNextWindowDockID(layer.getDockId());
        ImGui.begin(this.getTitle(), this.getFlags());

        // Menu bar
        menuBar();

        ImGui.setCursorPos(ImGui.getCursorPosX(), ImGui.getCursorPosY());
        ImVec2 windowSize = getLargestSizeForViewport();
        ImVec2 windowPos = getCenteredPositionForViewport(windowSize);
        ImGui.setCursorPos(windowPos.x, windowPos.y);
        this.setSizeX((int) windowSize.x);
        this.setSizeY((int) windowSize.y);
        leftX = (int) (windowPos.x + ImGui.getStyle().getFramePaddingX() * 2);
        rightX = (int) (windowPos.x + ImGui.getStyle().getFramePaddingX() * 2);
        bottomY = (int) (windowPos.y + ImGui.getStyle().getFramePaddingY());
        topY = (int) (windowPos.y + this.getSizeY() + ImGui.getStyle().getFramePaddingY());

        int textureId = Window.getFramebuffer().getTexture().getId();
        ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);

        ImGui.end();
    }

    private void menuBar() {
        ImGui.beginMenuBar();
        if (ImGui.menuItem("Play")) {
        }
        if (ImGui.menuItem("Stop")) {
        }
        ImGui.endMenuBar();
    }
}
