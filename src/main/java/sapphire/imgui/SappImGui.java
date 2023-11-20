package sapphire.imgui;

import diamondEngine.diaUtils.DiaLogger;
import imgui.ImGui;
import imgui.flag.*;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import sapphire.eventsSystem.SappObserver;
import sapphire.Sapphire;
import sapphire.imgui.windows.ModalConfirmation;
import sapphire.imgui.windows.ImguiWindow;
import sapphire.imgui.windows.ModalInformation;

public class SappImGui {

    public static float BIG_ICON_SIZE;
    public static float MEDIUM_ICON_SIZE;
    public static float SMALL_ICON_SIZE;
    public static float BIG_COLUMN_SIZE;
    public static float MEDIUM_COLUMN_SIZE;
    public static float SMALL_COLUMN_SIZE;

    public static void init() {
        float fontSize = Sapphire.get().getSettings().getFontSize();
        BIG_ICON_SIZE = fontSize * 2.5f;
        MEDIUM_ICON_SIZE = fontSize * 2f;
        SMALL_ICON_SIZE = fontSize * 1.5f;
        BIG_COLUMN_SIZE = fontSize * 30;
        MEDIUM_COLUMN_SIZE = fontSize * 20;
        SMALL_COLUMN_SIZE = fontSize * 10;
    }

    public static void drawMatrix4f(String label, Matrix4f matrix) {
        ImGui.pushID(label);
        ImGui.columns(2);
        ImGui.setColumnWidth(0, (ImGui.getWindowWidth()) / 3);
        ImGui.textWrapped(label);
        ImGui.nextColumn();
        ImGui.beginTable("##", 4);

        ImGui.tableNextColumn();
        ImGui.text("" + matrix.m00());
        ImGui.tableNextColumn();
        ImGui.text("" + matrix.m01());
        ImGui.tableNextColumn();
        ImGui.text("" + matrix.m02());
        ImGui.tableNextColumn();
        ImGui.text("" + matrix.m03());

        ImGui.tableNextRow();
        ImGui.tableNextColumn();
        ImGui.text("" + matrix.m10());
        ImGui.tableNextColumn();
        ImGui.text("" + matrix.m11());
        ImGui.tableNextColumn();
        ImGui.text("" + matrix.m12());
        ImGui.tableNextColumn();
        ImGui.text("" + matrix.m13());

        ImGui.tableNextRow();
        ImGui.tableNextColumn();
        ImGui.text("" + matrix.m20());
        ImGui.tableNextColumn();
        ImGui.text("" + matrix.m21());
        ImGui.tableNextColumn();
        ImGui.text("" + matrix.m22());
        ImGui.tableNextColumn();
        ImGui.text("" + matrix.m23());

        ImGui.tableNextRow();
        ImGui.tableNextColumn();
        ImGui.text("" + matrix.m30());
        ImGui.tableNextColumn();
        ImGui.text("" + matrix.m31());
        ImGui.tableNextColumn();
        ImGui.text("" + matrix.m32());
        ImGui.tableNextColumn();
        ImGui.text("" + matrix.m33());
        ImGui.endTable();

        ImGui.columns(1);
        ImGui.popID();
    }

    public static void drawVec2Control(String label, Vector2f values) {
        drawVec2Control(label, values, 0.0f, (ImGui.getWindowWidth()) / 3);
    }

    public static void drawVec2Control(String label, Vector2f values, float resetValue) {
        drawVec2Control(label, values, resetValue, (ImGui.getWindowWidth()) / 3);
    }

    public static void drawVec2Control(String label, Vector2f values, float resetValue, float columnWidth) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.textWrapped(label);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, ImGui.getStyle().getCellPaddingY());
        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 3.0f, lineHeight);
        float widthEach = (ImGui.getContentRegionAvailX() - buttonSize.x * 2.0f) / 2.0f;

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

    public static void drawVec3Control(String label, Vector3f values) {
        drawVec3Control(label, values, 0.0f, (ImGui.getWindowWidth()) / 3);
    }

    public static void drawVec3Control(String label, Vector3f values, float resetValue, float columnWidth) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.textWrapped(label);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, ImGui.getStyle().getCellPaddingY());
        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 3.0f, lineHeight);
        float widthEach = (ImGui.getContentRegionAvailX() - buttonSize.x * 3.0f) / 3.0f;

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

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.1f, 0.15f, 0.8f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.2f, 0.2f, 0.9f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.2f, 0.7f, 0.2f, 1.0f);

        if (ImGui.button("Z", buttonSize.x, buttonSize.y)) {
            values.y = resetValue;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesZ = {values.z};
        ImGui.dragFloat("##z", vecValuesZ, 0.025f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.nextColumn();

        values.x = vecValuesX[0];
        values.y = vecValuesY[0];
        values.z = vecValuesZ[0];

        ImGui.popStyleVar();
        ImGui.columns(1);
        ImGui.popID();
    }

    public static boolean dragFloat(String label, ImFloat value) {
        return dragFloat(label, value, 0.025f);
    }

    public static boolean dragFloat(String label, ImFloat value, float step) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, (ImGui.getWindowWidth()) / 3);
        ImGui.textWrapped(label);
        ImGui.nextColumn();

       ImGui.pushItemWidth(ImGui.getContentRegionAvailX() + ImGui.getStyle().getFramePaddingX() * 2);
        float[] valArr = {value.get()};
        if (ImGui.dragFloat("##dragFloat", valArr, step)) {
            value.set(valArr[0]);
            ImGui.columns(1);
            ImGui.popID();
            return true;
        }
        ImGui.popItemWidth();

        ImGui.columns(1);
        ImGui.popID();

        return false;
    }

    public static boolean dragInt(String label, ImInt value) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, (ImGui.getWindowWidth()) / 3);
        ImGui.textWrapped(label);
        ImGui.nextColumn();

       ImGui.pushItemWidth(ImGui.getContentRegionAvailX() + ImGui.getStyle().getFramePaddingX() * 2);
        int[] valArr = {value.get()};
        if (ImGui.dragInt("##dragInt", valArr, 0.1f)) {
            value.set(valArr[0]);
            ImGui.columns(1);
            ImGui.popID();
            return true;
        }
        ImGui.popItemWidth();

        ImGui.columns(1);
        ImGui.popID();

        return false;
    }

    public static boolean colorPicker4(String label, Vector4f color) {
        boolean res = false;
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, (ImGui.getWindowWidth()) / 3);
        ImGui.textWrapped(label);
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

    public static boolean inputInt(String label, ImInt value) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, (ImGui.getWindowWidth()) / 3);
        ImGui.textWrapped(label);
        ImGui.nextColumn();

       ImGui.pushItemWidth(ImGui.getContentRegionAvailX() + ImGui.getStyle().getFramePaddingX() * 2);
        if (ImGui.inputInt("##" + label, value)) {
            ImGui.columns(1);
            ImGui.popID();
            return true;
        }
        ImGui.popItemWidth();

        ImGui.columns(1);
        ImGui.popID();

        return false;
    }

    public static boolean inputText(String label, ImString text) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, (ImGui.getWindowWidth()) / 3);
        ImGui.textWrapped(label);
        ImGui.nextColumn();

       ImGui.pushItemWidth(ImGui.getContentRegionAvailX() + ImGui.getStyle().getFramePaddingX() * 2);
        if (ImGui.inputText("##" + label, text)) {
            ImGui.columns(1);
            ImGui.popID();
            return true;
        }
        ImGui.popItemWidth();

        ImGui.columns(1);
        ImGui.popID();

        return false;
    }

    public static void textLabel(String label, String text) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, (ImGui.getWindowWidth()) / 3);
        ImGui.textWrapped(label);
        ImGui.nextColumn();

        ImGui.textWrapped(text);
        ImGui.columns(1);
        ImGui.popID();
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
     * For windows. Sets the ImGUI cursor to be aligned on both axis given the desired alignment and total size of the elements to be
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

    public static String combo(String title, String selected, String[] options) {

        String result = null;
        ImGui.pushID(title);
        ImGui.columns(2);
        ImGui.setColumnWidth(0, (ImGui.getWindowWidth()) / 3);
        ImGui.textWrapped(title);
        ImGui.nextColumn();
        if (ImGui.beginCombo("##" + title, selected)) {
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
        ImGui.textWrapped(title);
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

    public static boolean checkboxLabel(String label, boolean checked) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, (ImGui.getWindowWidth()) / 3);
        ImGui.textWrapped(label);
        ImGui.nextColumn();
        boolean changed = ImGui.checkbox("##" + label, checked);
        ImGui.columns(1);
        ImGui.popID();
        return changed;
    }

    public static void toolTip() {

    }
}
