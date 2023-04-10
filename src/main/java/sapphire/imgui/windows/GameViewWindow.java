package sapphire.imgui.windows;

import diamondEngine.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import sapphire.imgui.SappImGUILayer;
import sapphire.imgui.components.GameViewWindowControls;

public class GameViewWindow extends ImguiWindow {

    // ATTRIBUTES
    private int leftX, rightX, topY, bottomY;
    private final GameViewWindowControls controls = new GameViewWindowControls();

    // CONSTRUCTORS
    public GameViewWindow() {
        super("game_view", "Main View");
        this.setFlags(ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoCollapse |
                ImGuiWindowFlags.NoResize);
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

        ImGui.setNextWindowDockID(layer.getDockId());
        ImGui.begin(this.getTitle(), this.getFlags());

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

        // Controls
        controls.drawControls();

        int textureId = Window.getFramebuffer().getTexture().getId();
        ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);

        ImGui.end();
    }
}
