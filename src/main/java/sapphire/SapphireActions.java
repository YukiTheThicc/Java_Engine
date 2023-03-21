package sapphire;

import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaUtils;
import sapphire.imgui.SappImGUILayer;
import sapphire.imgui.windows.FileWindow;

import java.io.File;

public class SapphireActions {

    public static void newFile(SappImGUILayer layer) {
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

    public static void openFile(SappImGUILayer layer) {
        String[] paths = DiaUtils.selectFiles();
        if (paths != null) {
            for (String path : paths) {
                DiaLogger.log("Trying to open file on path '" + path + "'...");
                File newFile = new File(path);
                layer.addWindow(new FileWindow(newFile.getName(), newFile));
            }
        }
    }

    public static void saveFile(SappImGUILayer layer) {
        if (layer.getLastFocusedFile() != null) {
            layer.getLastFocusedFile().saveFile();
        }
    }

    public static void saveFileAs(SappImGUILayer layer) {
        FileWindow focusedFile = layer.getLastFocusedFile();
        if (focusedFile != null) {
            File file = DiaUtils.saveFile(focusedFile.getFile().getAbsolutePath());
            if (file != null && file.isFile()) {
                focusedFile.setFile(file);
                focusedFile.saveFile();
            }
        }
    }

    public static void openProject(SappImGUILayer layer) {
        String file = DiaUtils.selectDirectory(Sapphire.getLiteral("open_project"), Sapphire.get().getSettings().getWorkspace());
        if (file != null && !file.isEmpty()) {
            Sapphire.get().getSettings().setWorkspace(file);
            DiaLogger.log("Opening project '" + file + "'");
        }
    }

    public static void exportProject(SappImGUILayer layer) {

    }
}
