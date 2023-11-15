package sapphire.imgui.windows;

import diamondEngine.Diamond;
import diamondEngine.diaUtils.DiaProfiler;
import imgui.ImFont;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.extension.implot.ImPlot;
import imgui.extension.implot.flag.*;
import sapphire.Sapphire;
import sapphire.imgui.SappImGuiLayer;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Set;

public class ProfilerWindow extends ImguiWindow {

    // ATTRIBUTES
    private final DecimalFormat df;
    private float maxTime;
    private float staticTime = 3f;
    private float elapsedTime = 0f;

    // CONSTRUCTORS
    public ProfilerWindow() {
        super("profiler", "Profiler");
        this.maxTime = 0f;
        this.df = new DecimalFormat("#.######");
        df.setRoundingMode(RoundingMode.CEILING);
    }

    // METHODS
    @Override
    public void imgui(SappImGuiLayer layer) {

        if (ImGui.begin(this.getTitle(), this.getFlags())) {
            if (Diamond.getCurrentEnv() != null) {

                DiaProfiler profiler = Diamond.getProfiler();

                // Check for new maximum total. If new maximum is achieved, then the timer is reset
                float total = profiler.getRegisters().get("Total");
                if (total > maxTime) {
                    maxTime = total;
                    elapsedTime = 0f;
                }

                ImFont font = ImGui.getFont();
                font.setScale(0.80f);
                ImGui.pushFont(font);

                ImPlot.setNextPlotLimits(0, maxTime, 0, 0.5, 1);
                if (ImPlot.beginPlot("Runtime", "Time (ms)", "",
                        new ImVec2(ImGui.getContentRegionAvailX(), 150),
                        ImPlotFlags.NoMousePos | ImPlotFlags.NoTitle,
                        ImPlotAxisFlags.None,
                        ImPlotAxisFlags.NoTickMarks | ImPlotAxisFlags.NoTickLabels | ImPlotAxisFlags.NoGridLines | ImPlotAxisFlags.NoLabel)) {


                    ImPlot.setLegendLocation(ImPlotLocation.West, ImPlotOrientation.Vertical, true);
                    elapsedTime += Sapphire.get().getDt();
                    ImPlot.pushColormap(ImPlotColormap.Jet);
                    for (String register : profiler.getRegisters().keySet()) {
                        Float[] valueX = {profiler.getRegisters().get(register)};
                        Float[] valueY = {0f};
                        ImPlot.plotBarsH(register, valueX, valueY, 1f, 5);
                    }

                    /* Each time the timer surpasses the static time, check if the current maxTime minus the current
                     * total is higher than the current total. This is done to avoid the scale changing to often and to
                     * erratically. If not the timer is reset.
                     */
                    if (elapsedTime > staticTime) {
                        if ((maxTime - total) > total) {
                            maxTime -= total / 2;
                        } else {
                            elapsedTime -= staticTime;
                        }
                    }
                    ImPlot.popColormap();
                    ImPlot.endPlot();
                }
                font.setScale(1f);
                ImGui.popFont();
            }

            ImGui.separator();
            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            ImGui.text(Sapphire.getLiteral("current_threads"));
            if (ImGui.beginChild("options", ImGui.getContentRegionAvailX(), 200)) {
                for (Thread thread : threadSet) {
                    ImGui.text(thread.getName() + ": " + thread.getState());
                }
            }
            ImGui.endChild();
        }
        ImGui.end();
    }
}
