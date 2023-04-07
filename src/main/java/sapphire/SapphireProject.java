package sapphire;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diamondEngine.DiaEnvironment;
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

    // ATTRIBUTES
    private List<File> openedFiles;
    private List<DiaEnvironment> environments;
    private transient SapphireDir root;

    // CONSTRUCTORS
    public SapphireProject(SapphireDir root) {
        this.root = root;
        this.openedFiles = new ArrayList<>();
        this.environments = new ArrayList<>();
    }

    // GETTERS & SETTERS
    public List<File> getOpenedFiles() {
        return openedFiles;
    }

    public List<DiaEnvironment> getEnvironments() {
        return environments;
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

        DiaLogger.log("Saving Sapphire settings...");
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .create();
        try {
            Files.createDirectories(Paths.get("sapphire"));
            FileWriter writer = new FileWriter("sapphire/settings.json");
            writer.write(gson.toJson(this));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
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
                environments = temp.getEnvironments();
                openedFiles = temp.getOpenedFiles();
                return true;
            }
        } catch (Exception e) {
            DiaLogger.log("Failed to open project file: " + e.getMessage(), DiaLoggerLevel.ERROR);
        }
        return false;
    }

    public void closeProject() {
        this.root.joinDirs();
    }
}
