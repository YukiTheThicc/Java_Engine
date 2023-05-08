package sapphire.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaUtils;
import sapphire.Sapphire;
import sapphire.SapphireDir;
import sapphire.SapphireProject;
import sapphire.imgui.SappImGui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SappProjectCreator {

    public static SapphireProject create(String path) {
        File dir = new File(path);
        if (dir.isDirectory() && DiaUtils.getFilesInDir(path).isEmpty() && DiaUtils.getFoldersInDir(path).isEmpty()) {
            File sappProject = new File(path + "\\" + SapphireProject.PROJECT_FILE);
            File envsDir = new File(path + "\\" + SapphireProject.ENVS_DIR);
            File resDir = new File(path + "\\" + SapphireProject.RES_DIR);
            File texDir = new File(path + "\\" + SapphireProject.TEX_DIR);
            File sfxDir = new File(path + "\\" + SapphireProject.SFX_DIR);
            File musDir = new File(path + "\\" + SapphireProject.MUSIC_DIR);
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

                        SapphireDir root = new SapphireDir(path);
                        root.loadDirectory();
                        SapphireProject project = new SapphireProject(root);

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
