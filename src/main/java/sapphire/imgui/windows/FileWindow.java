package sapphire.imgui.windows;

import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import imgui.ImGui;
import imgui.extension.texteditor.TextEditor;
import imgui.flag.ImGuiCond;
import sapphire.imgui.SappImGUILayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileWindow extends ImguiWindow {

    // ATTRIBUTES
    private final TextEditor textEditor;
    private final File file;
    private boolean dirty;

    // CONSTRUCTORS
    public FileWindow(String name, File file) {
        super(name, name, false);
        this.textEditor = new TextEditor();
        textEditor.setShowWhitespaces(false);
        this.file = file;
        this.dirty = true;
        this.setActive(true);

        if (file.exists()) {
            try {
                byte[] data = Files.readAllBytes(Paths.get(file.getPath()));
                textEditor.setText(new String(data));
                dirty = false;
            } catch (IOException e) {
                DiaLogger.log("Failed to load data from file '" + file.getPath() + "'", DiaLoggerLevel.ERROR);
            }
        }
    }

    // METHODS
    @Override
    public void imgui(SappImGUILayer layer) {

        ImGui.setNextWindowSize(450f, 600f, ImGuiCond.FirstUseEver);
        imgui.internal.ImGui.setNextWindowDockID(layer.getDockId(), ImGuiCond.FirstUseEver);
        if (this.isActive().get()) {
            ImGui.begin(this.getTitle() + (dirty?"*":"") + "###" + this.getTitle(), this.isActive(), this.getFlags());
            textEditor.render(this.getTitle());
            if (textEditor.isTextChanged()) dirty = true;
            ImGui.end();
        } else {
            this.close(true);
        }
    }

    public void saveFile() {
        DiaLogger.log("Trying to save file '" + file.getPath() + "'");
    }
}
