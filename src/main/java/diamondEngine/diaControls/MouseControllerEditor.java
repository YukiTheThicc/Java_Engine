package diamondEngine.diaControls;

import diamondEngine.Diamond;
import diamondEngine.Environment;
import diamondEngine.diaComponents.Component;
import diamondEngine.diaControls.MouseControls;
import org.joml.Vector2f;
import sapphire.Sapphire;
import sapphire.imgui.windows.GameViewWindow;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

public class MouseControllerEditor {

    // ATTRIBUTES
    private static Vector2f clickOrigin;
    private static float dragLag = 0.032f;
    private static float dragSensitivity = 30.0f;
    private static float scrollSensitivity = 0.1f;
    private static boolean reset = false;
    private static float lerpTime = 0.0f;

    public static void update(float dt) {
        Diamond.getProfiler().beginMeasurement("Editor Controller");
        if (MouseControls.getScreenY() != 0.0f) {
            float addValue = (float) Math.pow(Math.abs(MouseControls.getScrollY()) * scrollSensitivity, 1 / GameViewWindow.editorCamera.zoom);
            addValue *= -Math.signum(MouseControls.getScrollY());
            GameViewWindow.editorCamera.zoom += addValue;
        }
        Diamond.getProfiler().endMeasurement("Editor Controller");
    }
}
