package sapphire;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diamondEngine.Environment;
import diamondEngine.Diamond;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaUtils;
import sapphire.imgui.SappImGui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class SapphireProject {

    // CONSTANTS
    public static final String PROJECT_FILE = "project.sapp";
    public static final String ENVS_DIR = "envs";
    public static final String RES_DIR = "res";
    public static final String TEX_DIR = "res/tex";
    public static final String SFX_DIR = "res/sfx";
    public static final String MUSIC_DIR = "res/mus";

    // ATTRIBUTES
    private List<File> openedFiles;
    private transient SapphireDir root;

    // CONSTRUCTORS
    public SapphireProject(SapphireDir root) {
        this.root = root;
        this.openedFiles = new ArrayList<>();
    }

    // GETTERS & SETTERS
    public List<File> getOpenedFiles() {
        return openedFiles;
    }

    public SapphireDir getRoot() {
        return root;
    }

    public void setRoot(SapphireDir root) {
        this.root = root;
    }

    // METHODS
    /**
     * Saves current settings into the settings.json file.
     */
    public void save() {

        DiaLogger.log("Saving Project...");
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .create();
        try {
            Files.createDirectories(Paths.get("sapphire"));
            FileWriter writer = new FileWriter(root.getPath() + "/" + PROJECT_FILE);
            writer.write(gson.toJson(this));
            writer.close();
            for (Environment env : Diamond.get().getEnvironments()) {
                env.save(root.getPath().getAbsolutePath()  + "/" + ENVS_DIR);
            }
        } catch (IOException e) {
            DiaLogger.log(this.getClass(), "Failed to save project file: ", DiaLoggerLevel.ERROR);
            DiaLogger.log(e.getMessage(), DiaLoggerLevel.ERROR);
        }
    }

    /**
     * Loads this project
     *
     * @return True if the project has loaded correctly
     */
    public boolean load() {

        File dir = root.getPath();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .create();
        String inFile = "";

        try {
            File projectFile = new File(dir.getAbsoluteFile() + "\\" + PROJECT_FILE);
            if (projectFile.exists()) {
                inFile = new String(Files.readAllBytes(projectFile.toPath()));
                DiaLogger.log("Loading project file from '" + projectFile.getAbsolutePath() + "'");
            } else {
                SappImGui.infoModal(Sapphire.getLiteral("project"), Sapphire.getLiteral("no_project_file"));
                root = null;
            }
            if (!inFile.equals("")) {
                SapphireProject temp = gson.fromJson(inFile, SapphireProject.class);
                root.loadDirectory();
                openedFiles = temp.getOpenedFiles();

                DiaUtils.getFilesInDir(dir.getAbsoluteFile() + "\\" + ENVS_DIR);
                return true;
            }
        } catch (Exception e) {
            DiaLogger.log(this.getClass(), "Failed to open project file: " + e.getMessage(), DiaLoggerLevel.ERROR);
        }
        return false;
    }

    public void closeProject() {
        this.root.joinDirs();
    }
}
