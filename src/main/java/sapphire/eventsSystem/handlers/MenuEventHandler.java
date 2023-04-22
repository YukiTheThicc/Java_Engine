package sapphire.eventsSystem.handlers;

import diamondEngine.Diamond;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaUtils;
import sapphire.Sapphire;
import sapphire.SapphireDir;
import sapphire.SapphireProject;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappObserver;
import sapphire.imgui.SappImGUILayer;
import sapphire.imgui.windows.FileWindow;
import sapphire.utils.SappProjectCreator;

import java.io.File;

public class MenuEventHandler implements SappObserver {
    @Override
    public void onNotify(SappEvent event) {
        switch(event.type) {
            case New_file:
                DiaLogger.log("Clicked on New_root_env", DiaLoggerLevel.SAPP_DEBUG);
                Diamond.get().addEmptyEnvironment();
                break;
            case Open_file:
                DiaLogger.log("Clicked on Add_env", DiaLoggerLevel.SAPP_DEBUG);
                break;
            case Save_file:
                DiaLogger.log("Clicked on Add_entity", DiaLoggerLevel.SAPP_DEBUG);
                break;
            case Save_as:
                DiaLogger.log("Clicked on Add_entity", DiaLoggerLevel.SAPP_DEBUG);
                break;
            case Create_project:
                DiaLogger.log("Clicked on Add_entity", DiaLoggerLevel.SAPP_DEBUG);
                break;
            case Open_project:
                DiaLogger.log("Clicked on Add_entity", DiaLoggerLevel.SAPP_DEBUG);
                break;
            case Export_project:
                DiaLogger.log("Clicked on Add_entity", DiaLoggerLevel.SAPP_DEBUG);
                break;
            case Settings:
                DiaLogger.log("Clicked on Add_entity", DiaLoggerLevel.SAPP_DEBUG);
                break;
        }
    }

    private static void newFile(SappImGUILayer layer) {
        String fileName = Sapphire.getLiteral("new_file");
        if (layer.getWindows().get(fileName) != null) {
            // If the new file name has a number, retrieve that number and add 1 to it. If not, add ' 1' to the
            // new files name
            String[] splitName = fileName.split("(\\d+)(?!.*\\d)");
            if (splitName.length > 1) {
                int fileNumber = Integer.parseInt(splitName[splitName.length - 1]) + 1;
                fileName += " " + fileNumber;
            } else {
                fileName += "New File";
            }
        }
        layer.addWindow(new FileWindow(fileName, new File("")));
    }

    private static void openFile(SappImGUILayer layer) {
        String[] paths = DiaUtils.selectFiles();
        if (paths != null) {
            for (String path : paths) {
                DiaLogger.log("Trying to open file on path '" + path + "'...");
                File newFile = new File(path);
                layer.addWindow(new FileWindow(newFile.getName(), newFile));
            }
        }
    }

    private static void saveFile(SappImGUILayer layer) {
        if (layer.getLastFocusedFile() != null) {
            layer.getLastFocusedFile().saveFile();
        }
    }

    private static void saveFileAs(SappImGUILayer layer) {
        FileWindow focusedFile = layer.getLastFocusedFile();
        if (focusedFile != null) {
            File file = DiaUtils.saveFile(focusedFile.getFile().getAbsolutePath());
            if (file != null && file.isFile()) {
                focusedFile.setFile(file);
                focusedFile.saveFile();
            }
        }
    }

    private static void createProject() {
        String path = DiaUtils.selectDirectory(Sapphire.getLiteral("create_project"), Sapphire.get().getSettings().getWorkspace());
        if (path != null && !path.isEmpty()) {
            DiaLogger.log("Creating project in '" + path + "'");
            SapphireProject project = SappProjectCreator.create(path);
            if (project != null) {
                Sapphire.get().setProject(project);
            }
        }
    }

    private static void openProject() {
        String path = DiaUtils.selectDirectory(Sapphire.getLiteral("open_project"), Sapphire.get().getSettings().getWorkspace());
        if (path != null && !path.isEmpty()) {
            DiaLogger.log("Opening project '" + path + "'");
            SapphireProject project = new SapphireProject(new SapphireDir(path));
            if (project.load()) {
                Sapphire.get().setProject(project);
            }
        }
    }

    private static void exportProject(SappImGUILayer layer) {

    }
}