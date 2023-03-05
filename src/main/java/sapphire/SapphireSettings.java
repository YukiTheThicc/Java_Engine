package sapphire;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class SapphireSettings {

    // ATTRIBUTES
    private String workspace;
    private String font;
    private HashMap<String, Boolean> activeWindows;
    private HashMap<String, String> literals;
    private HashMap<String, String> languages;

    // CONSTRUCTORS
    public SapphireSettings() {
        this.workspace = "";
        this.font = "";
        this.activeWindows = new HashMap<>();
        this.literals = new HashMap<>();
        this.languages = new HashMap<>();
    }

    // GETTERS & SETTERS
    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public HashMap<String, Boolean> getActiveWindows() {
        return activeWindows;
    }

    public void setActiveWindows(HashMap<String, Boolean> activeWindows) {
        this.activeWindows = activeWindows;
    }

    public String getLiteral(String literal) {
        return this.literals.get(literal);
    }

    // METHODS
    public void init() {
        DiaLogger.log("Initializing settings...");
        this.load();
        if (this.font == null || this.font.equals("")) {
            this.font = "res/fonts/consola.ttf";
        }

        SapphireUtils.getFilesInDir("sapphire/lang", "json");

        // Initialize literal map, by default in english
        this.defaultLiterals();
    }

    private void defaultLiterals() {
        literals.put("file", "File");
        literals.put("window", "Window");
        literals.put("new_file", "File");
        literals.put("open_file", "File");
        literals.put("save_file", "File");
        literals.put("open_project", "File");
        literals.put("export_project", "File");
        literals.put("settings", "File");
        literals.put("active_windows", "File");
        literals.put("assets", "File");
        literals.put("sprites", "File");
        literals.put("tiles", "File");
        literals.put("sounds", "File");
        literals.put("entity_properties", "File");
        literals.put("env_hierarchy", "File");
        literals.put("apply", "Apply");
        literals.put("close", "Close");
        literals.put("cancel", "Cancel");
        literals.put("yes", "Yes");
        literals.put("no", "No");
        literals.put("confirm_save", "Save?");
    }

    public void changeLangTo(String lang) {
        String path = "sapphire/lang/" + lang + ".json";
        try {
            byte[] data = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            DiaLogger.log("Failed to load language map from '" + path + "'", DiaLoggerLevel.ERROR);
        }
    }

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

    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .create();
        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("sapphire/settings.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!inFile.equals("")) {

            SapphireSettings temp = gson.fromJson(inFile, SapphireSettings.class);
            this.activeWindows = temp.getActiveWindows();
        }
    }
}
