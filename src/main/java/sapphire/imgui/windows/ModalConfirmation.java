package sapphire.imgui.windows;

import diamondEngine.Window;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappEventType;
import sapphire.eventsSystem.SappObserver;
import sapphire.Sapphire;
import sapphire.imgui.AlignX;
import sapphire.imgui.AlignY;
import sapphire.imgui.SappImGuiLayer;
import sapphire.imgui.SappImGuiUtils;

public class ModalConfirmation extends ImguiWindow {

    // ATTRIBUTES
    private boolean result;
    private final String message;
    private final String affirmative;
    private final String negative;
    private final SappObserver parent;
    private ImBoolean askAgain;

    // CONSTRUCTORS
    public ModalConfirmation(String id, String title, String message, String affirmative, String negative, SappObserver parent) {
        super(id, title, false);
        this.result = false;
        this.message = message;
        this.affirmative = affirmative;
        this.negative = negative;
        this.askAgain = new ImBoolean(true);
        this.parent = parent;
        this.setSizeX(Sapphire.get().getSettings().getFontSize() * 25);
        this.setSizeY(Sapphire.get().getSettings().getFontSize() * 10);
        this.setActive(true);
    }

    public ModalConfirmation(String id, String title, String message, SappObserver parent) {
        super(id, title, false);
        this.result = false;
        this.message = message;
        this.affirmative = Sapphire.getLiteral("yes");
        this.negative = Sapphire.getLiteral("no");
        this.askAgain = new ImBoolean(true);
        this.parent = parent;
        this.setSizeX(Sapphire.get().getSettings().getFontSize() * 25);
        this.setSizeY(Sapphire.get().getSettings().getFontSize() * 10);
        this.setActive(true);
    }

    // GETTERS & SETTERS
    public boolean result() {
        return result;
    }

    // METHODS
    @Override
    public void imgui(SappImGuiLayer layer) {
        ImGui.openPopup(getTitle());
        ImGui.setNextWindowSize(this.getSizeX(), this.getSizeY());
        ImGui.setNextWindowPos(Window.getPosition().x + (Window.getWidth() - this.getSizeX()) / 2,
                Window.getPosition().y + (Window.getHeight() - this.getSizeY()) / 2);
        if (ImGui.beginPopupModal(getTitle(), ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize |
                ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse)) {

            SappImGuiUtils.align(AlignX.LEFT, AlignY.CENTER, SappImGuiUtils.textSize(message), ImGui.getFontSize());
            ImGui.text(message);

            float totalSizeButtonsX = SappImGuiUtils.textSize(affirmative) + SappImGuiUtils.textSize(negative) + ImGui.getStyle().getCellPaddingX() * 5;
            float totalSizeButtonsY = ImGui.getFontSize() + ImGui.getStyle().getCellPaddingY() * 2;
            SappImGuiUtils.align(AlignX.LEFT, AlignY.BOTTOM, totalSizeButtonsX, totalSizeButtonsY);

            if (ImGui.button(affirmative)) {
                result = true;
                parent.onNotify(new SappEvent(SappEventType.Confirm));
            }
            ImGui.sameLine();
            if (ImGui.button(negative)) {
                result = false;
                parent.onNotify(new SappEvent(SappEventType.Cancel));
            }

            SappImGuiUtils.align(AlignX.RIGHT, AlignY.BOTTOM, SappImGuiUtils.textSize(Sapphire.getLiteral("dont_ask_again")) + ImGui.getFontSize() + ImGui.getStyle().getFramePaddingX() * 2, ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2);
            ImGui.checkbox(Sapphire.getLiteral("dont_ask_again"), askAgain);
            ImGui.endPopup();
        }
    }
}
