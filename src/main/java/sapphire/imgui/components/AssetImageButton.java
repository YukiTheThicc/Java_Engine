package sapphire.imgui.components;

import diamondEngine.diaRenderer.Texture;
import imgui.ImGui;
import imgui.ImVec2;
import sapphire.Sapphire;

import java.io.File;

public class AssetImageButton {

    /**
     * This class is here to retain the state of a button that doesn't change at runtime to reduce load on other elements.
     * Holds a label for the button, a Texture for the image and an ID for ImGui
     */
    // ATTRIBUTES
    private final String id;
    private final String label;
    private final Texture image;
    private final int buttonSize = ImGui.getFontSize() * 5;
    private final float imageSizeX = ImGui.getFontSize() * 4;
    private final float imageSizeY;
    private final float paddingX;
    private final float paddingYTop;

    // CONSTRUCTORS
    public AssetImageButton(Texture image) {
        this.id = image.getPath();
        this.label = new File(image.getPath()).getName();
        this.image = image;
        this.paddingX = ((float) this.buttonSize - this.imageSizeX) / 2;
        this.paddingYTop = ImGui.getStyle().getFramePaddingY();
        float imageSizeY = imageSizeX * ((float) image.getHeight() / image.getWidth());
        float maxImgSizeY = imageSizeX + paddingYTop * 2 - ImGui.getFontSize() + 4;
        this.imageSizeY = Math.min(imageSizeY, maxImgSizeY);
    }

    public AssetImageButton(String id, String label, Texture image) {
        this.id = id;
        this.label = label;
        this.image = image;
        this.paddingX = ((float) this.buttonSize - this.imageSizeX) / 2;
        this.paddingYTop = ImGui.getStyle().getFramePaddingY();
        float imageSizeY = imageSizeX * ((float) image.getHeight() / image.getWidth());
        float maxImgSizeY = imageSizeX + paddingYTop * 2 - ImGui.getFontSize() + 4;
        this.imageSizeY = Math.min(imageSizeY, maxImgSizeY);
    }

    // GETTERS & SETTERS
    public String getId() {
        return id;
    }

    public int getButtonSize() {
        return buttonSize;
    }

    public float getPaddingX() {
        return paddingX;
    }

    // METHODS
    public boolean draw() {
        ImVec2 origin = ImGui.getCursorPos();
        ImGui.getStyle().setButtonTextAlign(0.5f, 1f);
        ImGui.pushFont(Sapphire.get().getImGUILayer().getSmallFont());
        ImGui.pushID(id);
        boolean result = ImGui.button(label, buttonSize, buttonSize);
        ImGui.setCursorPos(origin.x + paddingX, origin.y + paddingYTop);
        ImGui.image(image.getId(), imageSizeX, imageSizeY, 0, 1, 1, 0);
        ImGui.setCursorPos(origin.x + buttonSize + paddingX * 2, origin.y);
        ImGui.popID();
        ImGui.popFont();
        return result;
    }
}
