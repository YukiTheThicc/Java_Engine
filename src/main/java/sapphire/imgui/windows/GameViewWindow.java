package sapphire.imgui.windows;

import diamondEngine.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import sapphire.imgui.SappImGUILayer;

public class GameViewWindow extends ImguiWindow {

    public GameViewWindow() {
        super("game_view", "Game View");
        this.setFlags(ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoScrollbar);
    }

    public void imgui(SappImGUILayer layer) {

        imgui.internal.ImGui.setNextWindowDockID(layer.getDockId());
        ImGui.begin(this.getTitle(), this.getFlags());

        ImVec2 windowSize2 = new ImVec2();
        ImGui.getContentRegionAvail(windowSize2);
        int textureId = Window.getFramebuffer().getTexture().getId();
        ImGui.image(textureId, windowSize2.x, windowSize2.y, 0, 1, 1, 0);

        ImGui.end();
    }
}
