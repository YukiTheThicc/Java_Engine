package sapphire.imgui.components;

import diamondEngine.diaRenderer.Texture;
import imgui.ImGui;

public class SappImageLabelButton {

    /**
     * This class is here to retain the state of a button that doesn't change at runtime to reduce load
     */
    // ATTRIBUTES
    private final Texture image;
    private final String label;
    private final float buttonSizeX;
    private float buttonOriginX;
    private final float imageSizeX;
    private final float imageSizeY;

    // CONSTRUCTORS
    public SappImageLabelButton(Texture image, String label) {
        this.image = image;
        this.label = label;
        this.buttonSizeX = image.getWidth() + ImGui.calcTextSize(label).x + ImGui.getStyle().getFramePaddingX() * 4;
        this.buttonOriginX = ImGui.getCursorPosX();
        this.imageSizeX = image.getWidth();
        this.imageSizeY = image.getHeight();
    }

    public SappImageLabelButton(Texture image, String label, float sizeX, float sizeY) {
        this.image = image;
        this.label = label;
        this.buttonSizeX = sizeX + ImGui.calcTextSize(label).x + ImGui.getStyle().getFramePaddingX() * 4;
        this.buttonOriginX = ImGui.getCursorPosX();
        this.imageSizeX = sizeX;
        this.imageSizeY = sizeY;
    }

    // METHODS
    public boolean draw() {

        boolean result = false;
        ImGui.pushID(label);
        ImGui.beginGroup();
        buttonOriginX = ImGui.getCursorPosX();

        ImGui.getStyle().setButtonTextAlign(0.75f, 0.5f);
        if (ImGui.button(label, buttonSizeX, imageSizeY)) result = true;

        ImGui.sameLine();
        ImGui.setCursorPosX(buttonOriginX);
        ImGui.image(image.getId(), imageSizeX, imageSizeY, 0, 1, 1, 0);

        ImGui.endGroup();
        ImGui.popID();
        return result;
    }
}
