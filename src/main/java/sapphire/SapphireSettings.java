package sapphire;

import com.google.gson.*;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class SapphireSettings {

    // CONSTANTS
    private static final String DEFAULT_LANG = "en";

    // ATTRIBUTES
    private String workspace;
    private String currentFont;
    private String currentLang;
    private String lastProject;
    private HashMap<String, Boolean> activeWindows;
    private HashMap<String, int[]> colors;
    private HashMap<String, Boolean> showPreferences;
    private transient HashMap<String, String> languages;
    private transient HashMap<String, String> fonts;
    private int fontSize;

    // CONSTRUCTORS
    public SapphireSettings() {
        this.workspace = "";
        this.currentFont = "";
        this.lastProject = "";
        this.currentLang = DEFAULT_LANG;
        this.activeWindows = new HashMap<>();
        this.languages = new HashMap<>();
        this.languages.put(this.currentLang, "English");
        this.colors = new HashMap<>();
        this.showPreferences = new HashMap<>();
        this.fonts = new HashMap<>();
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

    public String[] getFonts() {
        String[] fonts = new String[1];
        fonts = this.fonts.keySet().toArray(fonts);
        return fonts;
    }

    public String getFont(String font) {
        return this.fonts.get(font);
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        Sapphire.get().getImGUILayer().isDirtyFont();
    }

    public String getLastProject() {
        return lastProject;
    }

    public void setLastProject(String lastProject) {
        this.lastProject = lastProject;
    }

    // METHODS
    public void init() {
        DiaLogger.log("Initializing settings...");
        // Initialize literal map, by default in english, and default colors
        this.defaultColors();
        this.load();
        if (currentFont == null || currentFont.equals("")) {
            currentFont = "consola";
        }

        ArrayList<File> fonts = DiaUtils.getFilesInDir("sapphire/fonts", "ttf");
        for (File font : fonts) {
            String fontName = font.getName().substring(0, font.getName().lastIndexOf('.'));
            this.fonts.put(fontName, font.getAbsolutePath());
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
            inFile = new String(Files.readAllBytes(Paths.get("sapphire/settings.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!inFile.equals("")) {

            SapphireSettings temp = gson.fromJson(inFile, SapphireSettings.class);
            workspace = temp.getWorkspace();
            currentFont = temp.getCurrentFont();
            activeWindows = temp.getActiveWindows();
            colors = temp.getColors();
            showPreferences = temp.getShowPreferences();
            currentLang = temp.getCurrentLang();
            changeLangTo(currentLang);

            if (!workspace.isEmpty() && new File(workspace).isDirectory()) {
                Sapphire.get().setProject(SapphireProject.create(workspace));
            }
        }
    }
}
