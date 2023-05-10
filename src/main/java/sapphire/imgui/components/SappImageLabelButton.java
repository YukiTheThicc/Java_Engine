package sapphire.imgui.components;

import diamondEngine.diaRenderer.Texture;
import imgui.ImGui;
import sapphire.SappEvents;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappEventType;
import sapphire.eventsSystem.SappObserver;

public class SappImageLabelButton implements SappObserver {

    /**
     * This class is here to retain the state of a button that doesn't change at runtime to reduce load
     */
    // ATTRIBUTES
    private final Texture image;
    private final String label;
    private float buttonSizeX;
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
        SappEvents.addObserver(this);
    }

    public SappImageLabelButton(Texture image, String label, float sizeX, float sizeY) {
        this.image = image;
        this.label = label;
        this.buttonSizeX = sizeX + ImGui.calcTextSize(label).x + ImGui.getStyle().getFramePaddingX() * 4;
        this.buttonOriginX = ImGui.getCursorPosX();
        this.imageSizeX = sizeX;
        this.imageSizeY = sizeY;
        SappEvents.addObserver(this);
    }

    // METHODS
    public boolean draw() {

        boolean result = false;
        ImGui.pushID(label);
        ImGui.beginGroup();
        buttonOriginX = ImGui.getCursorPosX();

        if (ImGui.button(label, buttonSizeX, imageSizeY)) result = true;

        ImGui.sameLine();
        ImGui.setCursorPosX(buttonOriginX);
        ImGui.image(image.getId(), imageSizeX, imageSizeY, 0, 1, 1, 0);

        ImGui.endGroup();
        ImGui.popID();

        return result;

    }
    @Override
    public void onNotify(SappEvent event) {
        if (event.type == SappEventType.Font_changed) {
            buttonSizeX = imageSizeX + ImGui.calcTextSize(label).x + ImGui.getStyle().getFramePaddingX() * 4;
        }
    }
}
