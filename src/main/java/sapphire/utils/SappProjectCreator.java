package sapphire.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaUtils;
import sapphire.Sapphire;
import sapphire.SappDir;
import sapphire.SappProject;
import sapphire.imgui.SappImGui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class SappProjectCreator {

    public static SappProject create(String path) {
        File dir = new File(path);
        if (dir.isDirectory() && DiaUtils.getFilesInDir(path).isEmpty() && DiaUtils.getFoldersInDir(path).isEmpty()) {
            File sappProject = new File(path + "\\" + SappProject.PROJECT_FILE);
            File envsDir = new File(path + "\\" + SappProject.ENVS_DIR);
            File resDir = new File(path + "\\" + SappProject.RES_DIR);
            File texDir = new File(path + "\\" + SappProject.TEX_DIR);
            File sfxDir = new File(path + "\\" + SappProject.SFX_DIR);
            File musDir = new File(path + "\\" + SappProject.MUSIC_DIR);
            try {
                if (sappProject.createNewFile()) {
                    try {
                        Gson gson = new GsonBuilder()
                                .setPrettyPrinting()
                                .enableComplexMapKeySerialization()
                                .create();

                        Files.createDirectory(envsDir.toPath());
                        Files.createDirectory(resDir.toPath());
                        Files.createDirectory(texDir.toPath());
                        Files.createDirectory(sfxDir.toPath());
                        Files.createDirectory(musDir.toPath());

                        SappDir root = new SappDir(path);
                        root.loadDirectory();
                        SappProject project = new SappProject(root);

                        FileWriter writer = new FileWriter(sappProject.getAbsolutePath());
                        writer.write(gson.toJson(project));
                        writer.close();
                        return project;
                    } catch (Exception e) {
                        DiaLogger.log("Failed to write data to project file", DiaLoggerLevel.ERROR);
                        DiaLogger.log(e.getMessage(), DiaLoggerLevel.ERROR);
                    }
                } else {
                    DiaLogger.log("Failed to create project file", DiaLoggerLevel.ERROR);
                }
            } catch (IOException e) {
                DiaLogger.log("Failed while creating new project", DiaLoggerLevel.ERROR);
            }
        } else {
            SappImGui.infoModal(Sapphire.getLiteral("create_project"), Sapphire.getLiteral("dir_not_empty"));
        }
        return null;
    }
}
