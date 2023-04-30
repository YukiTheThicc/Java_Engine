package sapphire.imgui.windows;

import diamondEngine.Diamond;
import diamondEngine.Window;
import diamondEngine.diaComponents.Camera;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;
import sapphire.imgui.SappImGUILayer;
import sapphire.imgui.components.GameViewWindowControls;

public class GameViewWindow extends ImguiWindow {

    // ATTRIBUTES
    private int leftX, rightX, topY, bottomY;
    public static Camera editorCamera;
    private final GameViewWindowControls controls = new GameViewWindowControls();

    // CONSTRUCTORS
    public GameViewWindow() {
        super("game_view", "Main View");
        this.setFlags(ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoCollapse |
                ImGuiWindowFlags.NoResize);
        GameViewWindow.editorCamera = new Camera(new Vector2f(0, 0));
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

        if (ImGui.begin(this.getTitle(), this.getFlags())) {
            ImGui.setNextWindowDockID(layer.getDockId());

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

            int textureId = Diamond.get().getCurrentEnv().getFrame().getTexture().getId();
            ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);
        }
        ImGui.end();
    }
}
