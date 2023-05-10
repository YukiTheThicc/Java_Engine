package sapphire.imgui.windows;

import diamondEngine.Window;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import sapphire.Sapphire;
import sapphire.imgui.AlignX;
import sapphire.imgui.AlignY;
import sapphire.imgui.SappImGUILayer;
import sapphire.imgui.SappImGui;

public class ModalInformation extends ImguiWindow{

    // ATTRIBUTES
    private boolean result;
    private final String message;
    private final String ok;

    // CONSTRUCTORS
    public ModalInformation(String id, String title, String message) {
        super(id, title, false);
        this.result = false;
        this.message = message;
        this.ok = Sapphire.getLiteral("ok");
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
    public void imgui(SappImGUILayer layer) {
        ImGui.openPopup(getTitle());
        ImGui.setNextWindowSize(this.getSizeX(), this.getSizeY());
        ImGui.setNextWindowPos(Window.getPosition().x + (Window.getWidth() - this.getSizeX()) / 2,
                Window.getPosition().y + (Window.getHeight() - this.getSizeY()) / 2);
        if (ImGui.beginPopupModal(getTitle(), ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize |
                ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse)) {

            SappImGui.align(AlignX.LEFT, AlignY.TOP, SappImGui.textSize(message), ImGui.getFontSize());
            ImGui.textWrapped(message);

            float totalSizeButtonsX = SappImGui.textSize(ok) + ImGui.getStyle().getCellPaddingX() * 2;
            float totalSizeButtonsY = ImGui.getFontSize() + ImGui.getStyle().getCellPaddingY() * 2;
            SappImGui.align(AlignX.CENTER, AlignY.BOTTOM, totalSizeButtonsX, totalSizeButtonsY);
            if (ImGui.button(ok)) {
                this.setActive(false);
                ImGui.closeCurrentPopup();
            }

            ImGui.endPopup();
        }
    }
}
