package sapphire.imgui.windows;

import diamondEngine.diaUtils.DiaConsole;
import imgui.ImGui;
import imgui.ImGuiListClipper;
import imgui.ImGuiTextFilter;
import imgui.ImVec2;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import sapphire.imgui.ImGUILayer;

import java.io.File;
import java.nio.file.*;
import java.util.List;

public class LogViewerWindow extends ImguiWindow {

    // ATTRIBUTES
    private boolean autoScroll;
    private String feed;
    private File log;
    private ImGuiTextFilter filter;
    private ImGuiListClipper clipper;
    private WatchKey watchKey;


    // CONSTRUCTORS
    public LogViewerWindow() {
        super("log_viewer", "Log Viewer");
        this.log = new File("log.txt");
        this.autoScroll = true;
        this.feed = "";
        this.filter = new ImGuiTextFilter();

        Path logDir = Paths.get(this.log.getAbsolutePath());
        DiaConsole.log(logDir.getParent().toString());
        try {
            WatchService watcher = logDir.getParent().getFileSystem().newWatchService();
            this.watchKey = logDir.getParent().register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (Exception e) {
            DiaConsole.log("Failed to set up observer for log viewer: " + e, DiaConsole.ERROR);
        }
    }

    // METHODS
    public void update() {
        if (this.watchKey != null) {
            List<WatchEvent<?>> events = watchKey.pollEvents();
            for (WatchEvent event : events) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                    System.out.println("Modify: " + event.context().toString());
                }
            }
        }
    }

    public void clear() {
        this.feed = "";
    }

    public void log(String text) {
        this.feed += text;
    }

    @Override
    public void imgui(ImGUILayer layer) {

        this.update();
        ImGui.begin(this.getTitle());

        ImBoolean optionOpen = new ImBoolean(false);
        if (ImGui.beginPopup("Options")) {
            ImGui.checkbox("Auto-scroll", optionOpen);
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



            // Keep up at the bottom of the scroll region if we were already at the bottom at the beginning of the frame.
            // Using a scrollbar or mouse-wheel will take away from the bottom edge.
            if (ImGui.getScrollY() >= ImGui.getScrollMaxY()) ImGui.setScrollHereY(1.0f);
        }
        ImGui.endChild();
        ImGui.end();
    }
}

