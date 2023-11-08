package sapphire;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import diamondEngine.Environment;
import diamondEngine.Diamond;
import diamondEngine.diaUtils.DiaAssetManager;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import sapphire.imgui.SappImGui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class SappProject {

    // CONSTANTS
    public static final String PROJECT_FILE = "project.sapp";
    public static final String ENVS_DIR = "envs";
    public static final String RES_DIR = "res";
    public static final String TEX_DIR = "res/tex";
    public static final String SFX_DIR = "res/sfx";
    public static final String MUSIC_DIR = "res/mus";
    public static final String SPRITES_DIR = "res/spr";

    // ATTRIBUTES
    private List<File> openedFiles;
    private List<String> projectEnvFiles;
    private List<String> resources;
    private String currentEnv;
    private transient SappDir root;

    // CONSTRUCTORS
    public SappProject(SappDir root) {
        this.root = root;
        this.openedFiles = new ArrayList<>();
        this.projectEnvFiles = new ArrayList<>();
        this.resources = new ArrayList<>();
        this.currentEnv = "";
    }

    // GETTERS & SETTERS
    public List<File> getOpenedFiles() {
        return openedFiles;
    }

    public SappDir getRoot() {
        return root;
    }

    public void setRoot(SappDir root) {
        this.root = root;
    }

    public List<String> getProjectEnvFiles() {
        return projectEnvFiles;
    }

    public List<String> getResources() {
        return resources;
    }

    public String getCurrentEnv() {
        return currentEnv;
    }

    public void setCurrentEnv(String currentEnvId) {
        this.currentEnv = currentEnvId;
        save();
    }

    // METHODS
    public void addResource(String path) {
        this.resources.add(path);
        save();
    }

    public void removeResource(String path) {
        resources.remove(path);
    }

    /**
     * Saves current settings into the settings.json file.
     */
    public void save() {

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .create();
        try {
            Files.createDirectories(Paths.get("sapphire"));
            FileWriter writer = new FileWriter(root.getPath() + "/" + PROJECT_FILE);

            projectEnvFiles.clear();
            for (Environment env : Diamond.get().getEnvironments()) {
                if (env.getOriginFile() != null && !env.isToRemove()) projectEnvFiles.add(env.getOriginFile());
            }

            writer.write(gson.toJson(this, SappProject.class));
            writer.close();
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
                SappProject temp = gson.fromJson(inFile, SappProject.class);
                root.loadDirectory();
                openedFiles = temp.getOpenedFiles();
                projectEnvFiles = temp.getProjectEnvFiles();
                if (projectEnvFiles == null) projectEnvFiles = new ArrayList<>();
                resources = temp.getResources();
                if (resources == null) resources = new ArrayList<>();
                currentEnv = temp.getCurrentEnv();

                for (String asset : resources) {
                    DiaAssetManager.loadResource(asset);
                }

                for (String envFile : projectEnvFiles) {
                    Environment loadedEnv = Environment.load(envFile);
                    Diamond.get().addEnvironment(loadedEnv);
                    if (loadedEnv.getOriginFile().equals(currentEnv)) {
                        Diamond.setCurrentEnv(loadedEnv);
                    }
                }
                return true;
            }
        } catch (Exception e) {
            DiaLogger.log(this.getClass(), "Failed to open project from file '" + e.getMessage() + "'", DiaLoggerLevel.ERROR);
        }
        return false;
    }

    public void closeProject() {
        this.root.joinDirs();
    }
}
