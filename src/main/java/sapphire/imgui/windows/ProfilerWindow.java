package sapphire.imgui.windows;

import imgui.ImGui;
import sapphire.Sapphire;
import sapphire.imgui.SappImGuiLayer;

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
    public void imgui(SappImGuiLayer layer) {

        if (ImGui.begin(this.getTitle(), this.getFlags())) {

            accTime += Sapphire.get().getDt();
            if (accTime > 0.1f) {
                fps = (int) (1 / Sapphire.get().getDt());
                accTime -= 0.1f;
            }

            ImGui.columns(2);
            ImGui.text("FPS: ");
            ImGui.sameLine();
            ImGui.text(String.valueOf(fps));
            ImGui.text(layer.getLastFocusedFile() != null ? layer.getLastFocusedFile().getTitle() : "No file opened");

            ImGui.nextColumn();

            ImGui.columns(1);
            ImGui.separator();
            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            ImGui.text(Sapphire.getLiteral("current_threads"));
            if (ImGui.beginChild("options")) {
                for (Thread thread : threadSet) {
                    ImGui.text(thread.getName() + ": " + thread.getState());
                }
            }
            ImGui.endChild();
        }
        ImGui.end();
    }
}
