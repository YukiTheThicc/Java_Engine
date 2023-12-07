package sapphire.imgui.windows;

import diamondEngine.Diamond;
import diamondEngine.diaComponents.Camera;
import diamondEngine.diaControls.MouseControls;
import diamondEngine.diaUtils.DiaMath;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;
import org.joml.Vector2i;
import sapphire.imgui.SappImGuiLayer;
import sapphire.imgui.widgets.GameViewWindowControls;

public class GameViewWindow extends ImguiWindow {

    // ATTRIBUTES
    private int leftX, rightX, topY, bottomY;
    public static Camera editorCamera;
    private final GameViewWindowControls controls = new GameViewWindowControls();
    private boolean isFocused = false;

    // CONSTRUCTORS
    public GameViewWindow() {
        super("game_view", "Main View");
        this.setFlags(ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoCollapse |
                ImGuiWindowFlags.NoResize);
        Vector2i pm = DiaMath.getFractionFromFloat((float) Diamond.getCurrentEnv().getFrameY() / Diamond.getCurrentEnv().getFrameX());
        GameViewWindow.editorCamera = new Camera(new Vector2f(), 1f, 1f);
        editorCamera.setActive();
    }

    // GETTERS & SETTERS
    public int getLeftX() {
        return leftX;
    }

    public int getRightX() {
        return rightX;
    }

    public int getTopY() {
        return topY;
    }

    public int getBottomY() {
        return bottomY;
    }

    public boolean isFocused() {
        return isFocused;
    }

    // METHODS
    public boolean getWantCaptureMouse() {
        return MouseControls.getX() >= leftX && MouseControls.getX() <= rightX &&
                MouseControls.getY() >= topY && MouseControls.getY() <= bottomY && isFocused;
    }

    private ImVec2 getLargestSizeForViewport() {

        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Diamond.getCurrentEnv().getAspectRatio();

        if (aspectHeight > windowSize.y) {
            // We must switch to pillarbox mode
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Diamond.getCurrentEnv().getAspectRatio();
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

    private void drawAxisLines() {

    }

    public void imgui(SappImGuiLayer layer) {

        editorCamera.update(0);
        ImGui.setNextWindowDockID(layer.getDockId());
        if (ImGui.begin(this.getTitle(), this.getFlags())) {

            isFocused = ImGui.isWindowFocused();
            ImVec2 windowSize = getLargestSizeForViewport();
            ImVec2 viewPortPos = getCenteredPositionForViewport(windowSize);
            ImVec2 viewPortWindowPos = ImGui.getWindowViewport().getWorkPos();
            ImVec2 windowPos = ImGui.getWindowPos();

            ImGui.setCursorPos(viewPortPos.x, viewPortPos.y);

            this.setSizeX((int) windowSize.x);
            this.setSizeY((int) windowSize.y);

            /* The actual position of the viewport is the imgui window position minus the position of the viewport
             * window plus the padding created by calculating the pillarbox view of the viewport itself
             */
            leftX = (int) (windowPos.x - viewPortWindowPos.x + viewPortPos.x);
            rightX = (int) (windowPos.x - viewPortWindowPos.x + viewPortPos.x + this.getSizeX());
            topY = (int) (windowPos.y - viewPortWindowPos.y + viewPortPos.y);
            bottomY = (int) (windowPos.y - viewPortWindowPos.y + viewPortPos.y + this.getSizeY());

            // Send view port pos and size data to mouse controls
            MouseControls.setGameViewportPos(leftX, topY);
            MouseControls.setGameViewportSize(windowSize.x, windowSize.y);

            // Controls
            controls.drawControls();

            int textureId = Diamond.getCurrentEnv().getFrame().getTexture().getId();
            ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);
        }
        ImGui.end();
    }
}
