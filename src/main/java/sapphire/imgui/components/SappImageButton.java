package sapphire.imgui.components;

import diamondEngine.diaRenderer.Texture;
import imgui.ImGui;

public class SappImageButton {

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
    public SappImageButton(Texture image) {
        this.id = "##button-" + image.getId();
        this.image = image;
        this.buttonOriginX = ImGui.getCursorPosX();
        this.imageSizeX = image.getWidth();
        this.imageSizeY = image.getHeight();
    }

    public SappImageButton(Texture image, float sizeX, float sizeY) {
        this.id = "##button-" + image.getId();
        this.image = image;
        this.buttonOriginX = ImGui.getCursorPosX();
        this.imageSizeX = sizeX;
        this.imageSizeY = sizeY;
    }

    // METHODS
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
