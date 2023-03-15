package sapphire.imgui.windows;

import diamondEngine.diaUtils.DiaUtils;
import imgui.ImGui;
import imgui.extension.texteditor.TextEditor;
import imgui.flag.ImGuiCond;
import sapphire.imgui.SappImGUILayer;

import java.io.*;

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
        textEditor.setText(new String(DiaUtils.readAllBytes(file)));
        dirty = false;
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
            if (ImGui.isWindowFocused()) layer.setLastFocusedFile(this);
            ImGui.end();
        } else {
            this.close(true);
        }
    }

    public void saveFile() {
        DiaUtils.saveToFile(file, textEditor.getTextLines());
        dirty = false;
    }
}
