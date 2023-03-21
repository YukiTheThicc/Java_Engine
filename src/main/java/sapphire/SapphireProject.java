package sapphire;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diamondEngine.DiaEnvironment;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SapphireProject {

    // ATTRIBUTES
    private List<File> openedFiles;
    private List<DiaEnvironment> environments;
    private String rootPath;
    private transient SapphireDir root;

    // CONTRUCTORS
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
     * Loads into this instance all stored properties in the settings.json file.
     */
    public void load(String path) {

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .create();
        String inFile = "";

        try {
            ArrayList<File> projectFile = DiaUtils.getFilesInDir(path, "sapp");
            if (projectFile.size() > 0) {
                if (projectFile.size() > 1) {
                    DiaLogger.log("Found to project files within the directory, using most recent one", DiaLoggerLevel.WARN);
                }
                inFile = new String(Files.readAllBytes(projectFile.get(0).toPath()));
            } else {
                DiaLogger.log("No Sapphire project file was found in '" + path + "'", DiaLoggerLevel.WARN);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            SapphireProject temp = gson.fromJson(inFile, SapphireProject.class);
            this.environments = temp.getEnvironments();
            this.openedFiles = temp.getOpenedFiles();
        }
    }
}
