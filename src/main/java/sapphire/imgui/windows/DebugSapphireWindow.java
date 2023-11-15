package sapphire.imgui.windows;

import diamondEngine.diaUtils.DiaFIFO;
import imgui.ImGui;
import imgui.type.ImInt;
import sapphire.imgui.SappImGui;
import sapphire.imgui.SappImGuiLayer;

public class DebugSapphireWindow extends ImguiWindow {

    private DiaFIFO fifo;
    private ImInt num;
    private Object lastPopped;

    public DebugSapphireWindow() {
        super("debug_sapp", "Sapphire Debugger");
        num = new ImInt();
        fifo = new DiaFIFO(8);
    }

    @Override
    public void imgui(SappImGuiLayer layer) {

        if (ImGui.begin("Dev window")) {

            SappImGui.inputInt("Input", num);
            if (ImGui.button("Push")) fifo.push(num.get());
            ImGui.sameLine();
            if (ImGui.button("Pop")) lastPopped = fifo.pop();
            ImGui.sameLine();
            ImGui.text("Last popped: " + lastPopped);

            ImGui.separator();
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
            ImGui.end();
        }
    }
}
