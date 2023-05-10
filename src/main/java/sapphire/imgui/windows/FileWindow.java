package sapphire.imgui.windows;

import diamondEngine.diaUtils.DiaUtils;
import imgui.ImGui;
import imgui.extension.texteditor.TextEditor;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import sapphire.imgui.SappImGuiLayer;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileWindow extends ImguiWindow {

    // ATTRIBUTES
    private final TextEditor textEditor;
    private File file;
    private boolean dirty;

    // CONSTRUCTORS
    public FileWindow(String name, File file) {
        super(name, name, false);
        this.textEditor = new TextEditor();
        textEditor.setShowWhitespaces(false);
        this.file = file;
        this.dirty = true;
        this.setActive(true);
        textEditor.setText(new String(DiaUtils.readAllBytes(file), StandardCharsets.UTF_8));
        dirty = false;
        this.setAllFlags(ImGuiWindowFlags.None);
    }

    // GETTERS & SETTERS
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    // METHODS
    @Override
    public void imgui(SappImGuiLayer layer) {

        ImGui.setNextWindowSize(450f, 600f, ImGuiCond.FirstUseEver);
        imgui.internal.ImGui.setNextWindowDockID(layer.getDockId(), ImGuiCond.FirstUseEver);

        if (ImGui.begin(this.getTitle() + (dirty ? "*" : "") + "###" + this.getTitle(), this.isActive(), this.getFlags())) {
            textEditor.render(this.getTitle());
            if (textEditor.isTextChanged()) dirty = true;
            if (ImGui.isWindowFocused()) layer.setLastFocusedFile(this);
        }

        ImGui.end();
        if (!this.isActive().get()) {
            this.close(true);
            if (layer.getLastFocusedFile() != null && layer.getLastFocusedFile().getId().equals(this.getId())) layer.setLastFocusedFile(null);
        }
    }

    public void saveFile() {
        DiaUtils.saveToFile(file, textEditor.getTextLines());
        dirty = false;
    }
}
