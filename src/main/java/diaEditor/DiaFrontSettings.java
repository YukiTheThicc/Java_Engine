package diaEditor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diaEditor.imgui.windows.ImguiWindow;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class DiaFrontSettings {

    // ATTRIBUTES
    private String dir;
    private String font;
    HashMap<String, Boolean> activeWindows;

    // CONSTRUCTORS
    public DiaFrontSettings() {
        this.dir = "";
        this.font = "";
        this.activeWindows = new HashMap<>();
    }

    // GETTERS & SETTERS
    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
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

    // METHODS
    public void init() {
        this.load();
        if (this.font == null || this.font.equals("")) {
            this.font = "res/fonts/visitor.ttf";
        }
    }

    public void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .create();
        try {
            FileWriter writer = new FileWriter("settings.json");
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
            inFile = new String(Files.readAllBytes(Paths.get("settings.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!inFile.equals("")) {

            DiaFrontSettings temp = gson.fromJson(inFile, DiaFrontSettings.class);
            this.activeWindows = temp.getActiveWindows();
        }
    }

    public void imgui() {

    }
}
