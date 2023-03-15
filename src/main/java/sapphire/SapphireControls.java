package sapphire;

import diamondEngine.Window;
import diamondEngine.diaUtils.DiaLogger;
import imgui.ImGuiIO;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class SapphireControls {

    private static final int NUM_KEYS = 350;
    private static boolean[] keyPressed = new boolean[NUM_KEYS];

    public static void processControls(ImGuiIO io) {
        io.getKeysDown(keyPressed);
        if (io.getKeyCtrl() && keyPressed[GLFW_KEY_S]) {
            SapphireActions.saveFile(Window.getImGuiLayer());
        }
        if (io.getKeyCtrl() && keyPressed[GLFW_KEY_O]) {
            SapphireActions.openFile(Window.getImGuiLayer());
        }
        if (io.getKeyCtrl() && keyPressed[GLFW_KEY_N]) {
            SapphireActions.newFile(Window.getImGuiLayer());
        }
    }
}
