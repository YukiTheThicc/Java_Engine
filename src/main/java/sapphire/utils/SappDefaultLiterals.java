package sapphire.utils;

import java.util.HashMap;

public class SappDefaultLiterals {

    public static HashMap<String, String> generateDefaults() {
        HashMap<String, String> literals = new HashMap<>();

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
        literals.put("accept", "Accept");
        literals.put("ok", "Ok");
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
        literals.put("save_log", "Save log");
        literals.put("general_settings", "General");
        literals.put("style_settings", "Styles");
        literals.put("font", "Font");
        literals.put("create_project", "Create project");
        literals.put("no_project_file", "No project configuration has been found on the directory");
        literals.put("dir_not_empty", "Cannot create a project on this directory, choose an empty directory");
        literals.put("create_env", "New Environment");
        literals.put("delete_env", "Delete Environment");
        literals.put("create_entity", "Create Entity");

        return literals;
    }

    public static void generateLangJson(String name, String code, HashMap<String, String> literals) {

    }
}