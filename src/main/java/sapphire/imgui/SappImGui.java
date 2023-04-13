package sapphire.imgui;

import diamondEngine.diaRenderer.Texture;
import diamondEngine.diaUtils.DiaLogger;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector4f;
import sapphire.eventsSystem.SappObserver;
import sapphire.Sapphire;
import sapphire.imgui.windows.ModalConfirmation;
import sapphire.imgui.windows.ImguiWindow;
import sapphire.imgui.windows.ModalInformation;

public class SappImGui {

    private static final float DEFAULT_COLUMN_WIDTH = 100f;
    private static final float DEFAULT_CONF_WINDOW_WIDTH = 250f;
    private static final float DEFAULT_CONF_WINDOW_HEIGHT = 100f;

    public static void drawVec2Control(String label, Vector2f values) {
        drawVec2Control(label, values, 0.0f, DEFAULT_COLUMN_WIDTH);
    }

    public static void drawVec2Control(String label, Vector2f values, float resetValue) {
        drawVec2Control(label, values, resetValue, DEFAULT_COLUMN_WIDTH);
    }

    public static void drawVec2Control(String label, Vector2f values, float resetValue, float columnWidth) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 3.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 2.0f) / 2.0f;

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.7f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.8f, 0.3f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.2f, 0.7f, 0.2f, 1.0f);
        if (ImGui.button("X", buttonSize.x, buttonSize.y)) {
            values.x = resetValue;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesX = {values.x};
        ImGui.dragFloat("##x", vecValuesX, 0.025f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.1f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.2f, 0.7f, 0.2f, 1.0f);
        if (ImGui.button("Y", buttonSize.x, buttonSize.y)) {
            values.y = resetValue;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesY = {values.y};
        ImGui.dragFloat("##y", vecValuesY, 0.025f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.nextColumn();

        values.x = vecValuesX[0];
        values.y = vecValuesY[0];

        ImGui.popStyleVar();
        ImGui.columns(1);
        ImGui.popID();
    }

    public static float dragFloat(String label, float value) {
        return dragFloat(label, value, 0.025f);
    }

    public static float dragFloat(String label, float value, float step) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, DEFAULT_COLUMN_WIDTH);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] valArr = {value};
        ImGui.dragFloat("##dragFloat", valArr, step);

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }

    public static int dragInt(String label, int value) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, DEFAULT_COLUMN_WIDTH);
        ImGui.text(label);
        ImGui.nextColumn();

        int[] valArr = {value};
        ImGui.dragInt("##dragFloat", valArr, 0.1f);

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }

    public static boolean colorPicker4(String label, Vector4f color) {
        boolean res = false;
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, DEFAULT_COLUMN_WIDTH);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] imColor = {color.x, color.y, color.z, color.w};
        if (ImGui.colorEdit4("##colorPicker", imColor)) {
            color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            res = true;
        }

        ImGui.columns(1);
        ImGui.popID();

        return res;
    }

    public static String inputText(String label, String text) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, DEFAULT_COLUMN_WIDTH);
        ImGui.text(label);
        ImGui.nextColumn();

        ImString out = new ImString(text, 256);
        if (ImGui.inputText("##" + label, out)) {
            ImGui.columns(1);
            ImGui.popID();
            return out.get();
        }

        ImGui.columns(1);
        ImGui.popID();

        return text;
    }

    public static float textSize(String text) {
        return ImGui.getFont().calcTextSizeAX(ImGui.getFontSize(), ImGui.getWindowSizeX(), ImGui.getWindowSizeX(), text);
    }

    /**
     * Sets the ImGUI cursor to be aligned on both axis given the desired alignment and total size of the elements to be
     * aligned on both axis, within the window the function is called.
     *
     * @param alignX Enum value of the horizontal alignment
     * @param alignY Enum value of the vertical alignment
     * @param sizeX  Size on the horizontal axis to offset the cursor
     * @param sizeY  Size on the vertical axis to offset the cursor
     */
    public static void align(AlignX alignX, AlignY alignY, float sizeX, float sizeY) {

        ImGui.setCursorPos(0, 0);
        float x = 0f;
        float y = 0f;
        float titleBarY = ImGui.getFrameHeightWithSpacing();
        float regionX = ImGui.getWindowSizeX();
        float regionY = ImGui.getWindowSizeY();

        switch (alignX) {
            case LEFT:
                x = ImGui.getStyle().getWindowPaddingX();
                break;
            case CENTER:
                x = regionX / 2 - sizeX / 2;
                break;
            case RIGHT:
                x = regionX - sizeX - ImGui.getStyle().getWindowPaddingX();
                break;
        }

        switch (alignY) {
            case TOP:
                y = titleBarY;
                break;
            case CENTER:
                y = regionY / 2 - sizeY / 2;
                break;
            case BOTTOM:
                y = regionY - sizeY - ImGui.getStyle().getWindowPaddingY();
                break;
        }

        ImGui.setCursorPos(x, y);
    }

    /**
     * For windowsSets the ImGUI cursor to be aligned on both axis given the desired alignment and total size of the elements to be
     * aligned on both axis, within the window the function is called.
     *
     * @param alignX Enum value of the horizontal alignment
     * @param alignY Enum value of the vertical alignment
     * @param sizeX  Size on the horizontal axis to offset the cursor
     * @param sizeY  Size on the vertical axis to offset the cursor
     */
    public static void alignNoHeader(AlignX alignX, AlignY alignY, float sizeX, float sizeY) {

        ImGui.setCursorPos(0, 0);
        float x = 0f;
        float y = 0f;
        float regionX = ImGui.getWindowSizeX();
        float regionY = ImGui.getWindowSizeY();

        switch (alignX) {
            case LEFT:
                x = ImGui.getStyle().getWindowPaddingX();
                break;
            case CENTER:
                x = regionX / 2 - sizeX / 2;
                break;
            case RIGHT:
                x = regionX - sizeX - ImGui.getStyle().getWindowPaddingX();
                break;
        }

        switch (alignY) {
            case TOP:
                y = 0;
                break;
            case CENTER:
                y = regionY / 2 - sizeY / 2;
                break;
            case BOTTOM:
                y = regionY - sizeY - ImGui.getStyle().getWindowPaddingY();
                break;
        }

        ImGui.setCursorPos(x, y);
    }

    public static boolean combo(String title, ImInt index, String[] options) {

        ImGui.pushID(title);
        ImGui.columns(2);
        ImGui.setColumnWidth(0, DEFAULT_COLUMN_WIDTH);
        ImGui.text(title);
        ImGui.nextColumn();

        if (ImGui.combo("##" + title, index, options)) {
            ImGui.popID();
            return true;
        }
        ImGui.columns(1);
        ImGui.popID();
        return false;
    }

    public static String combo(String title, String selected, String[] options) {

        String result = null;
        ImGui.pushID(title);
        ImGui.columns(2);
        ImGui.setColumnWidth(0, DEFAULT_COLUMN_WIDTH);
        ImGui.text(title);
        ImGui.nextColumn();
        if (ImGui.beginCombo(title, selected)) {
            for (String option : options) {
                ImGui.pushID(option);
                if (ImGui.selectable(option)) {
                    DiaLogger.log("Selected '" + option + "'");
                    result = option;
                }
                ImGui.popID();
            }
            ImGui.endCombo();
        }
        ImGui.columns(1);
        ImGui.popID();
        return result;
    }

    public static boolean combo(String title, ImInt index, String[] options, float labelColWidth) {

        ImGui.pushID(title);
        ImGui.columns(2);
        ImGui.setColumnWidth(0, labelColWidth);
        ImGui.text(title);
        ImGui.nextColumn();

        if (ImGui.combo("##" + title, index, options)) {
            ImGui.popID();
            return true;
        }
        ImGui.columns(1);
        ImGui.popID();
        return false;
    }

    public static ImguiWindow confirmModal(String title, String message, String aff, String neg, SappObserver parent) {
        String id = title.toLowerCase().replace(" ", "_");
        ImguiWindow modal = Sapphire.get().getImGUILayer().getWindows().get(id);
        if (modal == null) {
            modal = new ModalConfirmation(id, title, message, aff, neg, parent);
            Sapphire.get().getImGUILayer().addWindow(modal);
        }
        modal.setActive(true);
        return modal;
    }

    public static ImguiWindow confirmModal(String title, String message, SappObserver parent) {
        String id = title.toLowerCase().replace(" ", "_");
        ImguiWindow modal = Sapphire.get().getImGUILayer().getWindows().get(id);
        if (modal == null) {
            modal = new ModalConfirmation(id, title, message, parent);
            Sapphire.get().getImGUILayer().addWindow(modal);
        }
        modal.setActive(true);
        return modal;
    }

    public static ImguiWindow infoModal(String title, String message) {
        String id = title.toLowerCase().replace(" ", "_");
        ImguiWindow modal = Sapphire.get().getImGUILayer().getWindows().get(id);
        if (modal == null) {
            modal = new ModalInformation(id, title, message);
            Sapphire.get().getImGUILayer().addWindow(modal);
        }
        modal.setActive(true);
        return modal;
    }

    public static void iconText(Texture image, String text) {
        ImGui.image(image.getId(), image.getWidth(), image.getHeight());
        ImGui.text(text);
    }
}
