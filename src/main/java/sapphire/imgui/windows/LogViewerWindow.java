package sapphire.imgui.windows;

import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaLoggerObserver;
import imgui.ImGui;
import imgui.ImGuiListClipper;
import imgui.ImGuiTextFilter;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import sapphire.imgui.ImGUILayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogViewerWindow extends ImguiWindow implements DiaLoggerObserver {

    // ATTRIBUTES
    private String[] lines;
    private int[] levels;
    private int currentLine;
    private ImGuiTextFilter filter;
    private ImGuiListClipper clipper;
    private ImBoolean autoScroll;
    private final Pattern pattern;

    // CONSTRUCTORS
    public LogViewerWindow() {
        super("log_viewer", "Log Viewer");
        this.lines = new String[500];
        this.levels = new int[500];
        this.currentLine = 0;
        this.filter = new ImGuiTextFilter();
        this.autoScroll = new ImBoolean(true);
        this.pattern = Pattern.compile("\\[(\\d)\\]");
        DiaLogger.addObserver(this);
    }

    // METHODS
    @Override
    public void imgui(ImGUILayer layer) {

        ImGui.begin(this.getTitle());

        if (ImGui.beginPopup("Options")) {
            ImGui.checkbox("Auto-scroll", this.autoScroll);
            ImGui.endPopup();
        }

        // Main window
        if (ImGui.button("Options")) ImGui.openPopup("Options");
        ImGui.sameLine();
        boolean clear = ImGui.button("Clear");
        ImGui.sameLine();
        boolean copy = ImGui.button("Copy");
        ImGui.sameLine();

        ImGui.separator();

        if (ImGui.beginChild("scrolling", 0f, 0f, false, ImGuiWindowFlags.HorizontalScrollbar)) {
            if (copy) ImGui.logToClipboard();

            for (int i = 0; i < lines.length; i++) {
                if (lines[i] != null) {
                    switch (levels[i]) {
                        case DiaLogger.CRITICAL:
                            ImGui.textColored(175, 50, 233, 255, lines[i]);
                            break;
                        case DiaLogger.ERROR:
                            ImGui.textColored(200, 50, 50, 255, lines[i]);
                            break;
                        case DiaLogger.WARN:
                            ImGui.textColored(200, 175, 50, 255, lines[i]);
                            break;
                        case DiaLogger.DEBUG:
                            ImGui.textColored(50, 200, 50, 255, lines[i]);
                            break;
                        default:
                            ImGui.textColored(255, 255, 255, 255, lines[i]);
                            break;
                    }
                } else {
                    break;
                }
            }

            // Keep up at the bottom of the scroll region if we were already at the bottom at the beginning of the frame.
            // Using a scrollbar or mouse-wheel will take away from the bottom edge.
            if (ImGui.getScrollY() >= ImGui.getScrollMaxY()) ImGui.setScrollHereY(1.0f);
        }
        ImGui.endChild();
        ImGui.end();
    }

    @Override
    public void newEntry(String message, DiaLoggerLevel level) {
        lines[currentLine] = message;
        levels[currentLine] = level.ordinal();
        currentLine = (currentLine + 1 == lines.length) ? 0 : currentLine + 1;
    }
}

