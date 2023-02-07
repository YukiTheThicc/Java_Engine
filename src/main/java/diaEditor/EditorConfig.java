package diaEditor;

public class EditorConfig {

    // ATTRIBUTES
    private static EditorConfig editorConfig;
    private String dir;
    private String font;

    // CONSTRUCTORS
    private EditorConfig() {
        this.dir = "";
        this.font = "";
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

    // METHODS
    public static EditorConfig get() {
        if (editorConfig == null) {
            editorConfig = new EditorConfig();
        }
        return editorConfig;
    }

    public void init() {
        this.font = "res/fonts/visitor.ttf";
    }

    public void loadConfig(String file) {

    }
}
