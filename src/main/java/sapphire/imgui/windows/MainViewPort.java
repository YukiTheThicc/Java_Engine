package sapphire.imgui.windows;

import diamondEngine.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.type.ImString;
import imgui.flag.ImGuiTabBarFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.extension.texteditor.TextEditor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainViewPort extends ImguiWindow {

    private TextEditor textEditor;

    public MainViewPort() {
        super("view_port", "Game Viewport");
        this.textEditor = new TextEditor();
    }

    @Override
    public void imgui() {

        ImGui.begin(this.getTitle(), ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoDecoration);

        if (ImGui.beginTabBar(this.getTitle(), ImGuiTabBarFlags.NoTooltip)) {
            if (ImGui.beginTabItem("Game view")) {
                ImVec2 windowSize2 = new ImVec2();
                ImGui.getContentRegionAvail(windowSize2);
                int textureId = Window.getFramebuffer().getTexture().getId();
                ImGui.image(textureId, windowSize2.x, windowSize2.y, 0, 1, 1, 0);
                ImGui.endTabItem();
            }

            String inFile = "";
            String path = "script-test.java";
            try {
                inFile = new String(Files.readAllBytes(Paths.get(path)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (ImGui.beginTabItem(path)) {
                int cposX = textEditor.getCursorPositionLine();
                int cposY = textEditor.getCursorPositionColumn();

                String overwrite = textEditor.isOverwrite() ? "Ovr" : "Ins";
                String canUndo = textEditor.canUndo() ? "*" : " ";

                ImGui.text(cposX + "/" + cposY + " " + textEditor.getTotalLines() + " lines | " + overwrite + " | " + canUndo);

                textEditor.render("TextEditor");
                ImGui.endTabItem();
            }

            ImGui.endTabBar();
        }

        ImGui.end();
    }
}
