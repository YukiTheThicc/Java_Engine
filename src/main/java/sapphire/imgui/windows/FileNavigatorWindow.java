package sapphire.imgui.windows;

import diamondEngine.diaRenderer.Texture;
import diamondEngine.diaUtils.DiaUtils;
import imgui.ImGui;
import imgui.flag.ImGuiSelectableFlags;
import sapphire.Sapphire;
import sapphire.SappDir;
import sapphire.SappEvents;
import sapphire.SappProject;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappEventType;
import sapphire.imgui.AlignX;
import sapphire.imgui.AlignY;
import sapphire.imgui.SappImGuiLayer;
import sapphire.imgui.SappImGui;

import java.io.File;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class FileNavigatorWindow extends ImguiWindow {

    // ATTRIBUTES
    private static final float DEFAULT_ICON_X = 20f;
    private static final float DEFAULT_ICON_Y = 20f;

    // CONSTRUCTORS
    public FileNavigatorWindow() {
        super("navigator", "Navigator");
    }

    // METHODS
    @Override
    public void imgui(SappImGuiLayer layer) {

        if (ImGui.begin(this.getTitle(), this.getFlags())) {
            SappProject project = Sapphire.get().getProject();
            if (project != null && project.getRoot() != null) {
                if (project.getRoot().isAlive()) {
                    SappImGui.align(AlignX.CENTER, AlignY.CENTER, SappImGui.textSize(Sapphire.getLiteral("loading")), ImGui.getFontSize());
                    ImGui.text(Sapphire.getLiteral("loading"));
                } else {
                    ImGui.setNextItemOpen(true);
                    drawDir(project.getRoot(), layer);
                }
            }
        }
        ImGui.end();
    }

    private void drawDir(SappDir dir, SappImGuiLayer layer) {
        Texture tex = Sapphire.getIcon("dir.png");
        ImGui.image(tex.getId(), DEFAULT_ICON_X, DEFAULT_ICON_Y, 0, 1, 1, 0);
        ImGui.sameLine();
        if (dir.getDir() == null) {
            ImGui.setNextItemOpen(true);
        }

        if (ImGui.treeNode(dir.getPath().getName())) {

            for (SappDir nestedDir : dir.getDirs()) {
                drawDir(nestedDir, layer);
            }

            String iconFile;
            String selected;
            for (File file : DiaUtils.getFilesInDir(dir.getPath().getAbsolutePath())) {

                selected = null;
                iconFile = file.getName().substring(file.getName().lastIndexOf('.') + 1) + ".png";
                tex = Sapphire.getIcon(iconFile);
                ImGui.image(tex.getId(), DEFAULT_ICON_X, DEFAULT_ICON_Y, 0, 1, 1, 0);
                ImGui.sameLine();

                if (ImGui.selectable(file.getName(), false, ImGuiSelectableFlags.AllowDoubleClick)) selected = file.getName();
                fileContextMenu(file);
                if (ImGui.isMouseDoubleClicked(GLFW_MOUSE_BUTTON_LEFT) && selected != null) {
                    if (!file.getName().equals(SappProject.PROJECT_FILE)) layer.addWindow(new FileWindow(file.getName(), file));
                }
            }

            ImGui.treePop();
            ImGui.spacing();
        }
    }

    private void fileContextMenu(File file) {
        if (ImGui.beginPopupContextItem(file.getName())) {
            if (ImGui.menuItem(Sapphire.getLiteral("delete"))) SappEvents.notify(
                    new SappEvent(SappEventType.Delete_file, file));
            ImGui.endPopup();
        }
    }
}
