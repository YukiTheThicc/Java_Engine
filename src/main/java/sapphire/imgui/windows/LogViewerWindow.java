package sapphire.imgui.windows;

import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaLoggerObserver;
import diamondEngine.diaUtils.DiaUtils;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import sapphire.SapphireObserver;
import sapphire.Sapphire;
import sapphire.imgui.AlignX;
import sapphire.imgui.AlignY;
import sapphire.imgui.SappImGUILayer;
import sapphire.imgui.SappImGui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class LogViewerWindow extends ImguiWindow implements DiaLoggerObserver, SapphireObserver {

    private static final int DEFAULT_LINES = 50;

    // ATTRIBUTES
    private ImBoolean autoScroll;
    private String[] entries;
    private DiaLoggerLevel[] levels;
    private ConfirmationWindow confWindow;
    private final String[] availableLevels;
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
    public void imgui(SappImGUILayer layer) {

        ImGui.begin(this.getTitle(), this.getFlags());

        if (ImGui.beginPopup("Options")) {
            ImGui.checkbox("Auto-scroll", this.autoScroll);
            ImGui.endPopup();
        }

        // Logger options part
        ImGui.pushStyleColor(ImGuiCol.ChildBg, 50, 50, 50, 255);
        if (ImGui.beginChild("options", 200f, 0f)) {

            ImGui.sameLine();
            ImInt index = new ImInt(DiaLogger.getCurrentLevel().ordinal());
            if (SappImGui.combo(Sapphire.getLiteral("severity"), index, availableLevels, 80f)) {
                DiaLogger.changeLevel(DiaLoggerLevel.values()[index.get()]);
                DiaLogger.log("Changed log level to: " + DiaLogger.getCurrentLevel());
            }

            ImInt lines = new ImInt(this.lines);
            if (ImGui.inputInt(Sapphire.getLiteral("log_lines"), lines)) changeLineCount(lines.get());

            float clearX = SappImGui.textSize(Sapphire.getLiteral("clear")) + ImGui.getStyle().getCellPaddingX() * 5;
            float clearY = ImGui.getFontSize() + ImGui.getStyle().getCellPaddingY() * 2;
            SappImGui.align(AlignX.LEFT, AlignY.BOTTOM, clearX, clearY);
            if (ImGui.button(Sapphire.getLiteral("clear"))) clear();
            ImGui.sameLine();
            String title = Sapphire.getLiteral("save_log");
            String message = Sapphire.getLiteral("sure_to_save_log");
            if (ImGui.button(title)) {
                confWindow = (ConfirmationWindow) SappImGui.confirmModal(title, message, this);
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
                    int[] color = Sapphire.get().getSettings().getColor("DiaLogger." + levels[j]);
                    ImGui.textColored(color[0], color[1], color[2], color[3], entries[j]);
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

    public void changeLineCount(int newCount) {

        String[] oldEntries = entries.clone();
        DiaLoggerLevel[] oldLevels = levels.clone();
        lines = newCount;
        entries = new String[lines];
        levels = new DiaLoggerLevel[lines];

        currentLine = 0;
        for (int i = 0; i < oldEntries.length; i++) {
            newEntry(oldEntries[i], oldLevels[i]);
        }
    }

    @Override
    public void newEntry(String message, DiaLoggerLevel level) {
        entries[currentLine % lines] = message;
        levels[currentLine % lines] = level;
        currentLine++;
    }

    @Override
    public void onNotify() {
        DiaLogger.log("Save log modal window response received");
        if (confWindow != null) {
            if (confWindow.result()) {
                File file = DiaUtils.saveFile();
                if (file != null) {
                    DiaLogger.log("Selected file to save: '" + file + "'");
                    try {
                        Files.copy(DiaLogger.getLog().toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        DiaLogger.log("Failed to save log to new file");
                    }
                }
            }
            confWindow.close(true);
        }
    }
}

