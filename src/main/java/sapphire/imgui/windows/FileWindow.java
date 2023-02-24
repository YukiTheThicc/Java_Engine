package sapphire.imgui.windows;

import diamondEngine.diaUtils.DiaConsole;
import imgui.ImGui;
import imgui.extension.texteditor.TextEditor;
import imgui.flag.ImGuiCond;
import sapphire.imgui.ImGUILayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileWindow extends ImguiWindow {

    // ATTRIBUTES
    private final TextEditor textEditor;
    private final File file;

    // CONSTRUCTORS
    public FileWindow(String name, File file) {
        super(name, name, false);
        this.textEditor = new TextEditor();
        this.file = file;
        this.setActive(true);

        try {
            byte[] data = Files.readAllBytes(Paths.get(file.getPath()));
            textEditor.setText(new String(data));
        } catch (IOException e) {
            DiaConsole.log("Failed to load data from file '" + file.getPath() + "'", DiaConsole.ERROR);
        }
    }

    // GETTERS & SETTERS

    // METHODS
    @Override
    public void imgui(ImGUILayer layer) {

        ImGui.setNextWindowSize(400f, 600f, ImGuiCond.FirstUseEver);
        imgui.internal.ImGui.setNextWindowDockID(layer.getDockId(), ImGuiCond.FirstUseEver);
        if (this.isActive().get()) {
            ImGui.begin(this.getTitle(), this.isActive(), this.getFlags());
            textEditor.render(this.getTitle());
            ImGui.end();
        } else {
            this.shouldClose(true);
        }
    }

    public void saveFile() {
        DiaConsole.log("Trying to save file '" + file.getPath() + "'");
    }
}
