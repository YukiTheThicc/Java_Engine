package diamondEngine.diaControls;

import org.joml.Vector2f;
import sapphire.Sapphire;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseControlsEditor {

    private static double scrollX, scrollY;
    private static double xPos, yPos, lastY, lastX, worldX, worldY, lastWorldX, lastWorldY;
    private static boolean mouseButtonPressed[] = new boolean[9];
    private static boolean isDragging;
    private static int mouseButtonDown = 0;
    private static Vector2f gameViewportPos = new Vector2f();
    private static Vector2f gameViewportSize = new Vector2f();

    public static void endFrame() {
        scrollY = 0.0;
        scrollX = 0.0;
    }

    public static void clear() {
        scrollX = 0.0;
        scrollY = 0.0;
        xPos = 0.0;
        yPos = 0.0;
        lastX = 0.0;
        lastY = 0.0;
        mouseButtonDown = 0;
        isDragging = false;
        Arrays.fill(mouseButtonPressed, false);
    }

    // CALLBACKS
    public static void mousePosCallback(long window, double xpos, double ypos) {
        if (Sapphire.get().getImGUILayer().getGameView().getWantCaptureMouse()) {
            clear();
        }

        if (mouseButtonDown > 0) {
            isDragging = true;
        }

        lastX = xPos;
        lastY = yPos;
        lastWorldX = worldX;
        lastWorldY = worldY;
        xPos = xpos;
        yPos = ypos;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            mouseButtonDown++;
            if (button < mouseButtonPressed.length) {
                mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            mouseButtonDown--;
            if (button < mouseButtonPressed.length) {
                mouseButtonPressed[button] = false;
                isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        scrollX = xOffset;
        scrollY = yOffset;
    }

    public static float getX() {
        return (float) xPos;
    }

    public static float getY() {
        return (float) yPos;
    }
}
