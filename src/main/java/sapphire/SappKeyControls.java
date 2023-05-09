package sapphire;

import imgui.ImGuiIO;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappEventType;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class SappKeyControls {

    private static final int NUM_KEYS = 350;
    private static boolean[] keyPressed = new boolean[NUM_KEYS];

    public static void processControls(ImGuiIO io) {
        io.getKeysDown(keyPressed);
        if (io.getKeyCtrl() && keyPressed[GLFW_KEY_N])
            SappEvents.notify(new SappEvent(SappEventType.Add_env));
        if (io.getKeyCtrl() && keyPressed[GLFW_KEY_I])
            SappEvents.notify(new SappEvent(SappEventType.Import_env));
        if (io.getKeyCtrl() && keyPressed[GLFW_KEY_S])
            SappEvents.notify(new SappEvent(SappEventType.Save_env));
        if (io.getKeyCtrl() && io.getKeyAlt() && keyPressed[GLFW_KEY_S])
            SappEvents.notify(new SappEvent(SappEventType.Save_env_as));
        Arrays.fill(keyPressed, false);
    }
}
