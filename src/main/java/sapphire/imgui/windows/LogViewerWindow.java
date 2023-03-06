package sapphire.imgui.windows;

import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaLoggerObserver;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import sapphire.Sapphire;
import sapphire.imgui.ImGUILayer;

public class LogViewerWindow extends ImguiWindow implements DiaLoggerObserver {

    private static final int DEFAULT_LINES = 20;

    // ATTRIBUTES
    private ImBoolean autoScroll;
    private String[] entries;
    private DiaLoggerLevel[] levels;
    private String[] availableLevels;
    private int currentLine;
    private int lines;

    // CONSTRUCTORS
    public LogViewerWindow() {
        super("log_viewer", "Log Viewer");
        this.lines = DEFAULT_LINES;
        this.entries = new String[this.lines];
        this.levels = new DiaLoggerLevel[this.lines];
        this.availableLevels = new String[DiaLoggerLevel.values().length];
        for (int i = 0; i < DiaLoggerLevel.values().length; i++) {
            this.availableLevels[i] = DiaLoggerLevel.values()[i].toString();
        }
        this.currentLine = 0;
        this.autoScroll = new ImBoolean(true);
        this.setFlags(ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.NoScrollbar);
        DiaLogger.addObserver(this);
    }

    // METHODS
    public void clear() {
        entries = new String[lines];
        levels = new DiaLoggerLevel[lines];
        currentLine = 0;
    }

    @Override
    public void imgui(ImGUILayer layer) {

        ImGui.begin(this.getTitle(), this.getFlags());

        if (ImGui.beginPopup("Options")) {
            ImGui.checkbox("Auto-scroll", this.autoScroll);
            ImGui.endPopup();
        }

        // Logger options part
        ImGui.pushStyleColor(ImGuiCol.ChildBg, 50, 50, 50, 255);
        if (ImGui.beginChild("options", 200f, 0f)) {
            if (ImGui.button(Sapphire.getLiteral("clear"))) clear();
            ImGui.sameLine();
            if (ImGui.button("Add test entry")) newEntry("[DEBUG] Test entry", DiaLoggerLevel.DEBUG);
            ImInt index = new ImInt(DiaLogger.getCurrentLevel().ordinal());
            if (ImGui.combo(Sapphire.getLiteral("severity"), index, availableLevels)) {
                DiaLogger.changeLevel(DiaLoggerLevel.values()[index.get()]);
                DiaLogger.log("Changed log level to: " + DiaLogger.getCurrentLevel());
            }
        }
        ImGui.endChild();
        ImGui.popStyleColor();
        ImGui.sameLine();

        // Log viewer part
        if (ImGui.beginChild("scrolling", 0f, 0f, false, ImGuiWindowFlags.HorizontalScrollbar)) {
            int j;
            for (int i = 0; i < entries.length; i++) {
                j = currentLine >= entries.length ? (currentLine + i) % entries.length : i;
                if (entries[j] != null) {
                    switch (levels[j]) {
                        case CRITICAL:
                            ImGui.textColored(175, 50, 233, 255, entries[j]);
                            break;
                        case ERROR:
                            ImGui.textColored(200, 50, 50, 255, entries[j]);
                            break;
                        case WARN:
                            ImGui.textColored(200, 175, 50, 255, entries[j]);
                            break;
                        case DEBUG:
                            ImGui.textColored(50, 200, 50, 255, entries[j]);
                            break;
                        default:
                            ImGui.textColored(255, 255, 255, 255, entries[j]);
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
        ImGui.sameLine();

        ImGui.end();
    }

    @Override
    public void newEntry(String message, DiaLoggerLevel level) {
            entries[currentLine % lines] = message;
            levels[currentLine % lines] = level;
            currentLine++;

    }
}

