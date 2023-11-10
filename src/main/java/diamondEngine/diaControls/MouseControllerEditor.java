package diamondEngine.diaControls;

import diamondEngine.Diamond;
import diamondEngine.Environment;
import diamondEngine.diaComponents.Component;
import diamondEngine.diaControls.MouseControls;
import diamondEngine.diaUtils.DiaLogger;
import imgui.ImGui;
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

        if (MouseControls.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE) && dragLag > 0) {
            clickOrigin = MouseControls.getWorld(GameViewWindow.editorCamera);
            dragLag -= dt;
            return;
        } else if (MouseControls.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
            Vector2f mousePos = MouseControls.getWorld(GameViewWindow.editorCamera);
            Vector2f delta = new Vector2f(mousePos.x, mousePos.y).sub(clickOrigin);
            GameViewWindow.editorCamera.pos.sub(delta.mul(dt).mul(dragSensitivity));
            clickOrigin.lerp(mousePos, dt);
        }

        if (dragLag <= 0.0f && !MouseControls.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
            dragLag = 0.032f;
        }

        Diamond.getProfiler().endMeasurement("Editor Controller");
    }
}
