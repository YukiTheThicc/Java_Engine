package sapphire.imgui.windows;

import diamondEngine.diaUtils.DiaConsole;
import imgui.ImGui;
import sapphire.imgui.ImGUILayer;

import java.io.File;
import java.nio.file.*;
import java.util.List;

public class LogViewerWindow extends ImguiWindow {

    private File log;

    public LogViewerWindow() {
        super("log_viewer", "Log Viewer");
        this.log = new File("log.txt");

        Path logDir = Paths.get(this.log.getAbsolutePath());

        try {
            WatchService watcher = logDir.getFileSystem().newWatchService();
            logDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

            WatchKey watchKey = watcher.take();

            List<WatchEvent<?>> events = watchKey.pollEvents();
            for (WatchEvent event : events) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    System.out.println("Created: " + event.context().toString());
                }
                if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                    System.out.println("Delete: " + event.context().toString());
                }
                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                    System.out.println("Modify: " + event.context().toString());
                }
            }

        } catch (Exception e) {
            DiaConsole.log("Failed to set up observer for log viewer: " + e, DiaConsole.ERROR);
        }
    }

    @Override
    public void imgui(ImGUILayer layer) {

        ImGui.begin(this.getTitle());

        ImGui.end();
    }
}
