package diaEditor.imgui.windows;

import imgui.ImGui;

public class GameViewPortWindow extends ImguiWindow {

    public GameViewPortWindow() {
        super("Game View Port");
    }

    @Override
    public void imgui() {
        ImGui.begin(this.getTitle());

        ImGui.end();
    }
}
