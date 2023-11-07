package sapphire;

import com.google.gson.*;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaUtils;
import imgui.ImFont;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class SappSettings {

    // CONSTANTS
    private static final String DEFAULT_LANG = "en";

    // ATTRIBUTES
    private String workspace;
    private String currentFont;
    private String currentLang;
    private String lastProject;
    private boolean sappDebug;
    private HashMap<String, Boolean> activeWindows;
    private HashMap<String, Boolean> showPreferences;
    private transient HashMap<String, String> languages;
    private transient ArrayList<ImFont> fonts;              // Actual list of all the added fonts
    private transient ArrayList<ImFont> smallFonts;         // List of the small version of the fonts
    private transient String[] fontsList;                   // String with the names for the fonts, so they don't need to be calculated each frame for the combo
    private int fontSize;

    // CONSTRUCTORS
    public SappSettings() {
        this.workspace = "";
        this.currentFont = "";
        this.lastProject = "";
        this.sappDebug = false;
        this.currentLang = DEFAULT_LANG;
        this.activeWindows = new HashMap<>();
        this.languages = new HashMap<>();
        this.languages.put(this.currentLang, "English");
        this.showPreferences = new HashMap<>();
        this.fonts = new ArrayList<>();
        this.smallFonts = new ArrayList<>();
        this.fontSize = 12;
    }

    // GETTERS & SETTERS
    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public String getCurrentFont() {
        return currentFont;
    }

    public void setCurrentFont(String currentFont) {
        this.currentFont = currentFont;
    }

    public HashMap<String, Boolean> getActiveWindows() {
        return activeWindows;
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
     *
     * @return Hashmap with all the colors
     */
    private HashMap<String, Boolean> getShowPreferences() {
        return showPreferences;
    }

    public String[] getFontsList() {
        return fontsList;
    }

    public void setFontsList(String[] fontsList) {
        this.fontsList = fontsList;
    }

    public ArrayList<ImFont> getFonts() {
        return fonts;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getLastProject() {
        return lastProject;
    }

    public void setLastProject(String lastProject) {
        this.lastProject = lastProject;
    }

    public ArrayList<ImFont> getSmallFonts() {
        return smallFonts;
    }

    public boolean isSappDebug() {
        return sappDebug;
    }

    public void setSappDebug(boolean sappDebug) {
        this.sappDebug = sappDebug;
    }

    // METHODS
    public void init() {
        DiaLogger.log("Initializing settings...");
        // Initialize literal map, by default in english, and default colors
        this.load();
        if (currentFont == null || currentFont.equals("")) {
            currentFont = "consola";
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

    public void addFont(ImFont font) {
        this.fonts.add(font);
    }

    public void addSmallFont(ImFont font) {
        this.smallFonts.add(font);
    }

    public void setShowPreference(String window, boolean show) {
        showPreferences.put(window, show);
    }

    /**
     * Gets the preference for a confirmation window to be shown again or not
     *
     * @param window ID of the confirmation window
     * @return Returns the stored value or true in case it does not exist
     */
    public boolean getShowPreference(String window) {
        return showPreferences.get(window) != null ? showPreferences.get(window) : true;
    }

    public void changeLangTo(String lang) {
        if (lang != null && !lang.isEmpty()) {
            DiaLogger.log("Changing language to '" + lang + "'");
            File path = new File("sapphire/lang/" + lang + ".json");
            try {
                if (DEFAULT_LANG.equals(lang)) {
                    Sapphire.get().defaultLiterals();
                } else {
                    ArrayList<String> data = (ArrayList<String>) Files.readAllLines(path.toPath());
                    JsonElement langJson = JsonParser.parseString(String.join("", data));
                    if (langJson != null && langJson.isJsonObject()) {

                        JsonObject literals = langJson.getAsJsonObject().get("literals").getAsJsonObject();
                        if (literals != null && literals.isJsonObject()) {
                            Sapphire.get().setLiterals(literals);
                        }
                    } else {
                        DiaLogger.log("Language file is not valid: '" + languages.get(lang) + "'", DiaLoggerLevel.WARN);
                    }
                }
                this.currentLang = lang;
                DiaLogger.log("Successfully changed language to '" + lang + "'");
            } catch (IOException e) {
                DiaLogger.log("Failed to load language map from '" + path.getAbsolutePath() + "'", DiaLoggerLevel.ERROR);
            }
        }
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
            File settingsFile = new File("sapphire/settings.json");
            if (settingsFile.exists() && settingsFile.isFile()) {
                inFile = new String(Files.readAllBytes(Paths.get("sapphire/settings.json")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!inFile.equals("")) {

            SappSettings temp = gson.fromJson(inFile, SappSettings.class);
            workspace = temp.getWorkspace();
            sappDebug = temp.isSappDebug();
            currentFont = temp.getCurrentFont();
            activeWindows = temp.getActiveWindows();
            showPreferences = temp.getShowPreferences();
            currentLang = temp.getCurrentLang();
            lastProject = temp.getLastProject();
            fontSize = temp.getFontSize();
            changeLangTo(currentLang);

            if (!lastProject.isEmpty() && new File(lastProject).isDirectory()) {
                SappProject project = new SappProject(new SappDir(lastProject));
                if (project.load()) Sapphire.get().setProject(project);
            }
        }
    }
}
