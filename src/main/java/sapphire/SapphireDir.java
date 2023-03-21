package sapphire;

import diamondEngine.DiaEnvironment;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SapphireDir {

    // ATTRIBUTES
    private String path;
    private SapphireDir dir;
    private List<File> dirs;
    private List<File> files;

    // CONSTRUCTORS
    public SapphireDir(SapphireDir parent, String path) {
        this.path = path;
        this.files = new ArrayList<>();
        this.dirs = new ArrayList<>();
        this.dir = parent;
    }

    public SapphireDir(String path) {
        this.path = path;
        this.files = new ArrayList<>();
        this.dirs = new ArrayList<>();
        this.dir = null;
    }

    // GETTERS & SETTERS
    public SapphireDir getDir() {
        return dir;
    }

    public void setDir(SapphireDir dir) {
        this.dir = dir;
    }

    public List<File> getDirs() {
        return dirs;
    }

    public void setDirs(List<File> dirs) {
        this.dirs = dirs;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    // METHODS
    public void loadDirectory() {
        if (dir != null) {

            File currentDir = new File(path);
            dirs = DiaUtils.getFoldersInDir(path);
            files = DiaUtils.getFilesInDir(path);

        } else {
            DiaLogger.log("Cannot load directory if it isn't the root dir", DiaLoggerLevel.WARN);
        }
    }
}
