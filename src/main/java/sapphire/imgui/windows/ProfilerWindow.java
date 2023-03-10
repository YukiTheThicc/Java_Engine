package sapphire.imgui.windows;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.type.ImString;
import sapphire.Sapphire;
import sapphire.imgui.SappImGUILayer;

import java.util.Set;

public class ProfilerWindow extends ImguiWindow {

    // ATTRIBUTES
    private int fps;
    private float accTime;

    // CONSTRUCTORS
    public ProfilerWindow() {
        super("profiler", "Profiler");
        this.fps = 0;
        this.accTime = 0;
    }

    // METHODS
    @Override
    public void imgui(SappImGUILayer layer) {

        ImGui.begin(this.getTitle(), this.getFlags());

        accTime += Sapphire.get().getContainer().getDt();
        if (accTime > 0.1f) {
            fps = (int) (1 / Sapphire.get().getContainer().getDt());
            accTime -= 0.1f;
        }

        ImGui.columns(2);
        ImGui.text("FPS: ");
        ImGui.sameLine();
        ImGui.text(String.valueOf(fps));
        ImGui.inputText("TEST", new ImString());

        ImGui.nextColumn();
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        ImGui.text(Sapphire.getLiteral("current_threads"));
        ImGui.pushStyleColor(ImGuiCol.ChildBg, 50, 50, 50, 255);
        if (ImGui.beginChild("options")) {
            for (Thread thread : threadSet) {
                ImGui.text(thread.getName() + ": " + thread.getState());
            }
        }
        ImGui.endChild();
        ImGui.popStyleColor();


        ImGui.end();
    }
}
