package sapphire;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import imgui.ImVec4;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class SapphireSettings {

    // CONSTANTS
    private static final String UNKNOWN_LITERAL = "UNKNOWN";

    // ATTRIBUTES
    private String workspace;
    private String font;
    private String currentLang;
    private HashMap<String, Boolean> activeWindows;
    private HashMap<String, String> literals;
    private HashMap<String, String> languages;
    private HashMap<String, ImVec4> colors;
    private HashMap<String, Boolean> showPreferences;

    // CONSTRUCTORS
    public SapphireSettings() {
        this.workspace = "";
        this.font = "";
        this.activeWindows = new HashMap<>();
        this.literals = new HashMap<>();
        this.languages = new HashMap<>();
        this.colors = new HashMap<>();
        this.showPreferences = new HashMap<>();
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

    public String getLiteral(String literal) {
        if (this.literals.get(literal) != null) {
            return this.literals.get(literal);
        }
        return UNKNOWN_LITERAL;
    }

    public String[] getLanguages() {
        String[] langs = new String[1];
        langs = languages.keySet().toArray(langs);
        return langs;
    }

    public String getCurrentLang() {
        return currentLang;
    }

    // METHODS
    public void init() {
        DiaLogger.log("Initializing settings...");
        this.load();
        if (this.font == null || this.font.equals("")) {
            this.font = "res/fonts/consola.ttf";
        }

        ArrayList<File> langFiles = SapphireUtils.getFilesInDir("sapphire/lang", "json");

        for (File langFile : langFiles) {
            try {
                ArrayList<String> data = (ArrayList<String>) Files.readAllLines(Paths.get(langFile.getAbsolutePath()));
                JsonElement langJson = JsonParser.parseString(String.join("", data));
                if (langJson != null && langJson.isJsonObject()) {
                    languages.put(langFile.getName().substring(0, langFile.getName().lastIndexOf('.')), langJson.getAsJsonObject().get("lang").toString().replace("\"", ""));
                } else {
                    DiaLogger.log("Language file is not valid: '" + langFile.getAbsolutePath() + "'", DiaLoggerLevel.WARN);
                }
            } catch (IOException e) {
                DiaLogger.log("Failed to load language map from '" + langFile.getAbsolutePath() + "'", DiaLoggerLevel.ERROR);
            }
        }

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
        literals.put("clear", "Clear");
        literals.put("severity", "Severity");
        literals.put("lang", "Language");
        literals.put("save_log", "Save log");
    }

    public void setShowPreference(String window, boolean show) {
        showPreferences.put(window, show);
    }

    /**
     * Gets the preference for a confirmation window to be showed again or not
     * @param window ID of the confirmation window
     * @return Returns the stored value or true in case it does not exist
     */
    public boolean getShowPreference(String window) {
        return showPreferences.get(window) != null ? showPreferences.get(window) : true;
    }

    /**
     * @param lang
     * @return
     */
    public int changeLangTo(String lang) {
        if (lang != null && !lang.isEmpty()) {
            String path = "sapphire/lang/" + lang + ".json";
            this.currentLang = lang;
            try {
                byte[] data = Files.readAllBytes(Paths.get(path));
            } catch (IOException e) {
                DiaLogger.log("Failed to load language map from '" + path + "'", DiaLoggerLevel.ERROR);
            }

            String[] langs = getLanguages();
            for (int i = 0; i < langs.length; i++) {
                if (lang.equals(langs[i])) {
                    return i;
                }
            }
        }
        return -1;
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
