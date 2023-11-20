package sapphire.imgui.widgets;

import diamondEngine.diaAssets.Texture;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;

public class ImageButton {

    /**
     * This class is here to retain the state of a button that doesn't change at runtime to reduce load
     */
    // ATTRIBUTES
    private String id;
    private final Texture image;
    private float buttonOriginX;
    private final float imageSizeX;
    private final float imageSizeY;

    // CONSTRUCTORS
    public ImageButton(Texture image) {
        this.id = "##button-" + image.getId();
        this.image = image;
        this.buttonOriginX = ImGui.getCursorPosX();
        this.imageSizeX = image.getWidth();
        this.imageSizeY = image.getHeight();
    }

    public ImageButton(Texture image, float sizeX, float sizeY) {
        this.id = "##button-" + image.getId();
        this.image = image;
        this.buttonOriginX = ImGui.getCursorPosX();
        this.imageSizeX = sizeX;
        this.imageSizeY = sizeY;
    }

    // METHODS
    public boolean draw(boolean active) {

        boolean result = false;

        if (!active) {
            ImGui.pushStyleVar(ImGuiStyleVar.Alpha, ImGui.getStyle().getAlpha() * 0.5f);
            ImGui.pushStyleVar(ImGuiStyleVar.DisabledAlpha, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0,0,0,0);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0,0,0,0);
        }

        ImGui.beginGroup();
        buttonOriginX = ImGui.getCursorPosX();

        ImGui.getStyle().setButtonTextAlign(0.75f, 0.5f);
        if (ImGui.button(id, imageSizeX, imageSizeY)) result = active;

        ImGui.sameLine();
        ImGui.setCursorPosX(buttonOriginX);
        ImGui.image(image.getId(), imageSizeX, imageSizeY, 0, 1, 1, 0);

        ImGui.endGroup();

        if (!active) {
            ImGui.popStyleVar(2);
            ImGui.popStyleColor(2);
        }

        return result;
    }

    public boolean draw() {

        boolean result = false;

        ImGui.beginGroup();
        buttonOriginX = ImGui.getCursorPosX();

        ImGui.getStyle().setButtonTextAlign(0.75f, 0.5f);
        if (ImGui.button(id, imageSizeX, imageSizeY)) result = true;

        ImGui.sameLine();
        ImGui.setCursorPosX(buttonOriginX);
        ImGui.image(image.getId(), imageSizeX, imageSizeY, 0, 1, 1, 0);

        ImGui.endGroup();

        return result;
    }
}
