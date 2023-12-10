package sapphire;

import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SappDir extends Thread {

    // ATTRIBUTES
    private File path;
    private SappDir dir;
    private List<SappDir> dirs;
    private List<File> files;

    // CONSTRUCTORS
    public SappDir(SappDir parent, String path) {
        this.path = new File(path);
        this.files = new ArrayList<>();
        this.dirs = new ArrayList<>();
        this.dir = parent;
        this.setName("Directory Loader");
    }

    public SappDir(String path) {
        this.path = new File(path);
        this.files = new ArrayList<>();
        this.dirs = new ArrayList<>();
        this.dir = null;
        this.setName("Directory Loader");
    }

    // GETTERS & SETTERS
    public SappDir getDir() {
        return dir;
    }

    public void setDir(SappDir dir) {
        this.dir = dir;
    }

    public List<SappDir> getDirs() {
        return dirs;
    }

    public void setDirs(List<SappDir> dirs) {
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
    }

    public void loadDirectory() {
        if (!this.isAlive()) {
            this.start();
        }
        this.interrupt();
    }

    public void joinDirs() {
        for (SappDir dir : dirs) {
            try {
                dir.join();
            } catch (InterruptedException e) {
                DiaLogger.log("Couldn't join directory '" + dir.getPath() + "'", DiaLoggerLevel.ERROR);
                DiaLogger.log(e.getMessage(), DiaLoggerLevel.ERROR);
            }
        }
    }

    private static SappDir recursiveFolderDive(SappDir parent, File rootPath) {
        SappDir dir = new SappDir(parent, rootPath.getAbsolutePath());
        for (File file : DiaUtils.getFoldersInDir(dir.getPath().getAbsolutePath())) {
            dir.getDirs().add(recursiveFolderDive(dir, file));
        }
        for (File file : DiaUtils.getFilesInDir(dir.getPath().getAbsolutePath())) {
            dir.getFiles().add(file);
        }
        return dir;
    }
}
