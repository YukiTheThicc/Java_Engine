package sapphire;

import com.google.gson.*;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaUtils;
import imgui.ImVec4;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
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
    private HashMap<String, int[]> colors;
    private HashMap<String, Boolean> showPreferences;
    private transient HashMap<String, String> literals;
    private transient HashMap<String, String> languages;

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
        return literal + ": " + UNKNOWN_LITERAL;
    }

    public HashMap<String, int[]> getColors() {
        return colors;
    }

    public int[] getColor(String color) {
        return colors.get(color);
    }

    public void setColor(String color, int[] value) {
        if (value.length == 4) {
            colors.put(color, value);
        } else {
            DiaLogger.log("Color values not valid");
        }
    }

    public String[] getLanguages() {
        String[] langs = new String[1];
        langs = languages.keySet().toArray(langs);
        return langs;
    }

    public String getCurrentLang() {
        return currentLang;
    }

    /**
     * Return all the windows show preferences. Used only to load settings form file.
     * @return Hashmap with all the colors
     */
    private HashMap<String, Boolean> getShowPreferences() {
        return showPreferences;
    }

    // METHODS
    public void init() {
        DiaLogger.log("Initializing settings...");
        // Initialize literal map, by default in english, and default colors
        this.defaultLiterals();
        this.defaultColors();
        this.load();
        if (font == null || font.equals("")) {
            font = "sapphire/fonts/consola.ttf";
        }

        ArrayList<File> langFiles = DiaUtils.getFilesInDir("sapphire/lang", "json");
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
    }

    private void defaultLiterals() {
        literals.put("file", "File");
        literals.put("window", "Window");
        literals.put("new_file", "New file");
        literals.put("open_file", "Open file");
        literals.put("save_file", "Save file");
        literals.put("save_as", "Save as...");
        literals.put("open_project", "Open project");
        literals.put("export_project", "Export project");
        literals.put("settings", "Settings");
        literals.put("active_windows", "Active windows");
        literals.put("assets", "Assets");
        literals.put("sprites", "Sprites");
        literals.put("tiles", "Tiles");
        literals.put("sounds", "Sounds");
        literals.put("entity_properties", "Entity properties");
        literals.put("env_hierarchy", "Environments");
        literals.put("apply", "Apply");
        literals.put("close", "Close");
        literals.put("cancel", "Cancel");
        literals.put("yes", "Yes");
        literals.put("no", "No");
        literals.put("confirm_save", "Save?");
        literals.put("sure_to_save_log", "Save log to new file?");
        literals.put("clear", "Clear");
        literals.put("severity", "Severity");
        literals.put("lang", "Language");
        literals.put("dont_ask_again", "Don't ask this again");
    }

    private void defaultColors() {
        colors.put("DiaLogger.CRITICAL", new int[]{175,50,233,255});
        colors.put("DiaLogger.ERROR", new int[]{200,50,50,255});
        colors.put("DiaLogger.WARN", new int[]{200,175,50,255});
        colors.put("DiaLogger.DEBUG", new int[]{50,200,50,255});
        colors.put("DiaLogger.INFO", new int[]{255,255,255,255});
    }

    public void setShowPreference(String window, boolean show) {
        showPreferences.put(window, show);
    }

    /**
     * Gets the preference for a confirmation window to be shown again or not
     * @param window ID of the confirmation window
     * @return Returns the stored value or true in case it does not exist
     */
    public boolean getShowPreference(String window) {
        return showPreferences.get(window) != null ? showPreferences.get(window) : true;
    }

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
            workspace = temp.getWorkspace();
            font = temp.getFont();
            activeWindows = temp.getActiveWindows();
            colors = temp.getColors();
            showPreferences = temp.getShowPreferences();
        }
    }
}
