package sapphire.imgui.windows;

import imgui.ImGui;
import imgui.extension.texteditor.TextEditor;
import imgui.flag.ImGuiCond;
import sapphire.imgui.ImGUILayer;

import java.io.File;

public class FileWindow extends ImguiWindow {

    // ATTRIBUTES
    private TextEditor textEditor;
    private String path;

    // CONSTRUCTORS
    public FileWindow(String name, File file) {
        super(name, name);
        this.textEditor = new TextEditor();
        this.path = path;
    }

    // GETTERS & SETTERS
    public String getPath() {
        return path;
    }

    // METHODS
    @Override
    public void imgui(ImGUILayer layer) {

        ImGui.setNextWindowSize(400f, 600f, ImGuiCond.FirstUseEver);
        imgui.internal.ImGui.setNextWindowDockID(layer.getDockId());
        ImGui.begin(this.getTitle());

        int cposX = textEditor.getCursorPositionLine();
        int cposY = textEditor.getCursorPositionColumn();

        String overwrite = textEditor.isOverwrite() ? "Ovr" : "Ins";
        String canUndo = textEditor.canUndo() ? "*" : " ";

        ImGui.text(cposX + "/" + cposY + " " + textEditor.getTotalLines() + " lines | " + overwrite + " | " + canUndo);

        textEditor.render("TextEditor");

        ImGui.end();
    }
}
