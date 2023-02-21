package sapphire.imgui.windows;

import diamondEngine.diaUtils.DiaConsole;
import imgui.ImGui;
import imgui.extension.texteditor.TextEditor;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import sapphire.imgui.ImGUILayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileWindow extends ImguiWindow {

    // ATTRIBUTES
    private final TextEditor textEditor;
    private File file;

    // CONSTRUCTORS
    public FileWindow(String name, File file) {
        super(name, name);
        this.textEditor = new TextEditor();
        this.file = file;

        try {
            byte[] data = Files.readAllBytes(Paths.get(file.getPath()));
            textEditor.setText(new String(data));
        } catch (IOException e) {
            DiaConsole.log("Failed to load data from file '" + file.getPath() + "'", "error");
        }
    }

    // GETTERS & SETTERS

    // METHODS
    @Override
    public void imgui(ImGUILayer layer) {

        ImGui.setNextWindowSize(400f, 600f, ImGuiCond.FirstUseEver);
        imgui.internal.ImGui.setNextWindowDockID(layer.getDockId(), ImGuiCond.FirstUseEver);
        if (ImGui.begin(this.getTitle())) {
            textEditor.render(this.getTitle());
            ImGui.end();
        } else {
            ImGui.end();
        }
    }
}
