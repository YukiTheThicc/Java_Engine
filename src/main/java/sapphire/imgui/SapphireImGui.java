package sapphire.imgui;

import diamondEngine.diaUtils.DiaLogger;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector4f;
import sapphire.Sapphire;

import java.util.HashMap;

public class SapphireImGui {

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

    public static String combo(String label, String selected, HashMap<String, String> values) {
        /*for (int i = 0; i < values.length; i++) {
            if () {

            }
        }
        if (ImGui.combo(label, index, values)) {
            index.set();
        }*/
        return "";
    }

    /**
     * Sets the ImGUI cursor to be aligned on both axis given the desired alignment (CENTER, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT or
     * BOTTOM_RIGHT) and total size of the elements to be
     * aligned on both axis
     * @param alignment Enum value of the alignment (default CENTER)
     * @param sizeX Size on the horizontal axis to offset the cursor
     * @param sizeY Size on the vertical axis to offset the cursor
     */
    public static void align(Alignment alignment, float sizeX, float sizeY) {

        float x = 0f;
        float y = 0f;
        float titleBarY = ImGui.getFrameHeightWithSpacing();
        float regionX = ImGui.getWindowSizeX();
        float regionY = ImGui.getWindowSizeY();
        DiaLogger.log(ImGui.getContentRegionAvailX() + "");

        DiaLogger.log("regX: " + regionX + ", regY: " + regionY);

        switch (alignment) {
            case CENTER:
                x = (regionX - sizeX/2)/2;
                y = (regionY - sizeY/2 + titleBarY)/2;
                break;
            case TOP_LEFT:
                x = ImGui.getStyle().getFramePaddingX();
                y = (regionY - sizeY + titleBarY)/2;
                break;
            case TOP_RIGHT:
                break;
            case BOTTOM_LEFT:
                break;
            case BOTTOM_RIGHT:
                break;
        }
        DiaLogger.log("x: " + x + ", y: " + y);
        DiaLogger.log(ImGui.getContentRegionAvailX() + "");

        ImGui.setCursorPos(x, y);
    }

    /**
     * Sets the ImGUI cursor to be aligned horizontally given an alignment (CENTER, RIGHT or LEFT) and size on the horizontal
     * axis.
     * @param alignment Enum value of the alignment (default CENTER)
     * @param size Size on the horizontal axis to offset the cursor
     */
    public static void alignH(Alignment alignment, float size) {

    }

    /**
     * Sets the ImGUI cursor to be centered vertically given an alignment (CENTER, TOP or BOTTOM) and size on the vertical
     * axis.
     * @param alignment Enum value of the alignment (default CENTER)
     * @param size Size on the vertical axis to offset the cursor
     */
    public static void alignV(Alignment alignment, float size) {

    }

    public static boolean confirmModal(String title, String message) {
        ImBoolean showAgain = new ImBoolean(Sapphire.get().getSettings().getShowPreference(title));
        boolean result = false;
        if (showAgain.get()) {

            ImGui.setNextWindowSize(DEFAULT_CONF_WINDOW_WIDTH, DEFAULT_CONF_WINDOW_HEIGHT, ImGuiCond.Always);

            if (ImGui.beginPopupModal(title, ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize |
                    ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse)) {

                align(Alignment.CENTER, message.length()*ImGui.getFontSize(), ImGui.getFontSize());
                ImGui.text(message);
                if (ImGui.button(Sapphire.getLiteral("yes"))) {
                    result = true;
                    ImGui.closeCurrentPopup();;
                }
                ImGui.sameLine();
                if (ImGui.button(Sapphire.getLiteral("no"))) {
                    result = false;
                    ImGui.closeCurrentPopup();
                }
                if (ImGui.checkbox(Sapphire.getLiteral("dont_show_again"), showAgain))
                    Sapphire.get().getSettings().setShowPreference(title, showAgain.get());
                ImGui.endPopup();
            }
        }
        return result;
    }
}
