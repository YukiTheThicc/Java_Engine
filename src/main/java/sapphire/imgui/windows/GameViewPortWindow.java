package sapphire.imgui.windows;

import diamondEngine.Window;
import imgui.ImGui;
import imgui.ImVec2;

public class GameViewPortWindow extends ImguiWindow {

    public GameViewPortWindow() {
        super("view_port", "Game Viewport");
    }

    @Override
    public void imgui() {
        ImGui.begin(this.getTitle());

        ImVec2 windowSize2 = new ImVec2();
        ImGui.getContentRegionAvail(windowSize2);
        int textureId = Window.getFramebuffer().getTexture().getId();
        ImGui.image(textureId, windowSize2.x, windowSize2.y, 0, 1, 1, 0);

        ImGui.end();
    }
}
