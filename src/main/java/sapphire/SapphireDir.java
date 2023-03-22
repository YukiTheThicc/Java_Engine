package sapphire;

import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SapphireDir extends Thread{

    // ATTRIBUTES
    private File path;
    private SapphireDir dir;
    private List<SapphireDir> dirs;
    private List<File> files;

    // CONSTRUCTORS
    public SapphireDir(SapphireDir parent, String path) {
        this.path = new File(path);
        this.files = new ArrayList<>();
        this.dirs = new ArrayList<>();
        this.dir = parent;
    }

    public SapphireDir(String path) {
        this.path = new File(path);
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

    public List<SapphireDir> getDirs() {
        return dirs;
    }

    public void setDirs(List<SapphireDir> dirs) {
        this.dirs = dirs;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }

    // METHODS
    @Override
    public void run() {
        if (dir == null) {
            files = DiaUtils.getFilesInDir(path.getAbsolutePath());
            ArrayList<File> dirs = DiaUtils.getFoldersInDir(path.getAbsolutePath());
            if (dirs.size() > 0) {
                for (File dir : dirs) {
                    this.dirs.add(recursiveFolderDive(this, dir));
                }
            }
            DiaLogger.log("Successfully loaded project folder '" + path + "'");
        } else {
            DiaLogger.log("Cannot load directory if it isn't the root dir", DiaLoggerLevel.WARN);
        }
        interrupt();
    }

    public void loadDirectory() {
        this.start();
    }

    private static SapphireDir recursiveFolderDive(SapphireDir parent, File rootPath) {
        SapphireDir dir = new SapphireDir(parent, rootPath.getAbsolutePath());
        for (File file : DiaUtils.getFoldersInDir(dir.getPath().getAbsolutePath())) {
            dir.getDirs().add(recursiveFolderDive(dir, file));
        }
        for (File file : DiaUtils.getFilesInDir(dir.getPath().getAbsolutePath())) {
            dir.getFiles().add(file);
        }
        return dir;
    }
}
