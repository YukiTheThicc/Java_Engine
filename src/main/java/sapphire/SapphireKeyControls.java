package sapphire;

import imgui.ImGuiIO;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappEventType;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class SapphireKeyControls {

    private static final int NUM_KEYS = 350;
    private static boolean[] keyPressed = new boolean[NUM_KEYS];

    public static void processControls(ImGuiIO io) {
        io.getKeysDown(keyPressed);
        if (io.getKeyCtrl() && keyPressed[GLFW_KEY_S])
            SapphireEvents.notify(new SappEvent(SappEventType.Save_file));
        if (io.getKeyCtrl() && keyPressed[GLFW_KEY_O])
            SapphireEvents.notify(new SappEvent(SappEventType.Open_file));
        if (io.getKeyCtrl() && keyPressed[GLFW_KEY_N])
            SapphireEvents.notify(new SappEvent(SappEventType.New_file));
        Arrays.fill(keyPressed, false);
    }
}
