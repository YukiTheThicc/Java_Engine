package sapphire.imgui.components;

import diamondEngine.diaRenderer.Texture;
import imgui.ImGui;

public class SappImageButton {

    /**
     * This class is here to retain the state of a button that doesn't change at runtime to reduce load
     */
    // ATTRIBUTES
    private final Texture image;
    private final String label;
    private final float buttonSizeX;
    private final float buttonOriginX;

    // CONSTRUCTORS
    public SappImageButton(Texture image, String label) {
        this.image = image;
        this.label = label;
        this.buttonSizeX = image.getWidth() + ImGui.calcTextSize(label).x + ImGui.getStyle().getFramePaddingX() * 4;
        this.buttonOriginX = ImGui.getCursorPosX();
    }

    // METHODS
    public boolean draw() {

        boolean result = false;
        ImGui.pushID(label);
        ImGui.beginGroup();

        ImGui.getStyle().setButtonTextAlign(0.75f, 0.5f);
        if (ImGui.button(label, buttonSizeX, image.getHeight())) result = true;

        ImGui.sameLine();
        ImGui.setCursorPosX(buttonOriginX);
        ImGui.image(image.getId(), image.getWidth(), image.getHeight(), 0, 1, 1, 0);

        ImGui.endGroup();
        ImGui.popID();
        return result;
    }
}
