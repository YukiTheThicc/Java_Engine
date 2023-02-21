package sapphire.imgui.windows;

import diamondEngine.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import sapphire.imgui.ImGUILayer;

public class GameViewWindow extends ImguiWindow {

    public GameViewWindow() {
        super("game_view", "Game View");
    }

    public void imgui(ImGUILayer layer) {

        int windowFlags = ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoScrollbar;
        imgui.internal.ImGui.setNextWindowDockID(layer.getDockId());
        ImGui.begin(this.getTitle(), windowFlags);

        ImVec2 windowSize2 = new ImVec2();
        ImGui.getContentRegionAvail(windowSize2);
        int textureId = Window.getFramebuffer().getTexture().getId();
        ImGui.image(textureId, windowSize2.x, windowSize2.y, 0, 1, 1, 0);

        ImGui.end();
    }
}
