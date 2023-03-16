package sapphire;

import diamondEngine.diaUtils.DiaLogger;
import imgui.ImGuiIO;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class SapphireControls {

    private static final int NUM_KEYS = 350;
    private static boolean[] keyPressed = new boolean[NUM_KEYS];

    public static void processControls(ImGuiIO io) {
        io.getKeysDown(keyPressed);
        if (io.getKeyCtrl() && keyPressed[GLFW_KEY_S]) {
            SapphireActions.saveFile(Sapphire.get().getImGUILayer());
        }
        if (io.getKeyCtrl() && keyPressed[GLFW_KEY_O]) {
            SapphireActions.openFile(Sapphire.get().getImGUILayer());
        }
        if (io.getKeyCtrl() && keyPressed[GLFW_KEY_N]) {
            SapphireActions.newFile(Sapphire.get().getImGUILayer());
        }
        Arrays.fill(keyPressed, false);
    }
}
