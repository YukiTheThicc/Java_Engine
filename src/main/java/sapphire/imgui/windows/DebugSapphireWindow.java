package sapphire.imgui.windows;

import diamondEngine.Diamond;
import diamondEngine.DiamondObject;
import diamondEngine.Environment;
import diamondEngine.diaUtils.DiaFIFO;
import imgui.ImGui;
import imgui.type.ImInt;
import sapphire.imgui.SappImGui;
import sapphire.imgui.SappImGuiLayer;

import java.util.HashMap;

public class DebugSapphireWindow extends ImguiWindow {

    private ImInt num;
    private Object lastPopped;

    public DebugSapphireWindow() {
        super("debug_sapp", "Sapphire Debugger");
        num = new ImInt();
    }

    @Override
    public void imgui(SappImGuiLayer layer) {

        if (ImGui.begin("Dev window")) {

            DiaFIFO fifo = Diamond.getCurrentEnv().getAvailableIDs();
            HashMap<Long, Long> objs = Diamond.getCurrentEnv().getRegisteredObjects();

            ImGui.text("First: " + fifo.getFirst());
            ImGui.sameLine();
            ImGui.text("Last: " + fifo.getLast());
            ImGui.sameLine();
            ImGui.text("Stored: " + fifo.getStored());
            ImGui.separator();

            ImGui.columns(fifo.getList().length);
            for (int i = 0; i < fifo.getList().length; i++) {
                Object obj = fifo.getList()[i];
                ImGui.text(obj + "");
                if (i < fifo.getList().length - 1) ImGui.nextColumn();
            }
            ImGui.columns(1);

            ImGui.beginChild("Objects");
            ImGui.columns(2);
            ImGui.text("ID");
            ImGui.nextColumn();
            ImGui.text("Object");
            for (long key : objs.keySet()) {
                ImGui.text("" + key);
                ImGui.nextColumn();
                ImGui.text("" + objs.get(key));
            }
            ImGui.columns(1);
            ImGui.endChild();

            ImGui.end();
        }
    }
}
