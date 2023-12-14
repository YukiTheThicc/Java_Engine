package diamondEngine.diaUtils.serializers;

import com.google.gson.*;
import diamondEngine.Diamond;
import diamondEngine.Entity;
import diamondEngine.Environment;
import diamondEngine.diaComponents.Component;
import diamondEngine.diaUtils.DiaHierarchyNode;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import sapphire.Sapphire;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EnvironmentSerializer {

    // CONSTRUCTORS
    public EnvironmentSerializer() {
    }

    // SERIALIZATION METHODS
    public void save(String path, Environment toSave) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentSerializer())
                .registerTypeAdapter(Entity.class, new EntitySerializer())
                .enableComplexMapKeySerialization()
                .create();

        try {
            toSave.setOriginFile(path);
            FileWriter writer = new FileWriter(path);
            writer.write(gson.toJson(toSave));
            writer.close();
            toSave.setSaved();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads an environment from a file passed as a parameter. Creates a new environment with the loaded environment data
     * instead od directly returning the loaded environment. This is done to ensure proper object UUID registration without
     * serializing the registeredObjects map.
     *
     * @param path Path of the environment file to load the new environment from
     * @return The newly loaded environment
     */
    public Environment load(String path) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentSerializer())
                .registerTypeAdapter(Entity.class, new EntitySerializer())
                .enableComplexMapKeySerialization()
                .create();
        String inFile = "";
        Environment finalEnv = null;
        try {
            inFile = new String(Files.readAllBytes(Paths.get(path)));
            if (!inFile.equals("")) {

                // The source environment is loaded
                Environment loaded = gson.fromJson(inFile, Environment.class);

                // A new environment is created with the loaded environment name and uuid
                finalEnv = new Environment(loaded.getName(), loaded.getUuid());

                // Data is transferred from the loaded environment to the new environment
                finalEnv.init();
                finalEnv.setOriginFile(path);
                for (DiaHierarchyNode node : loaded.getNodes().values()) {
                    finalEnv.addEntity(node);
                }
                finalEnv.updateEntityList();
                finalEnv.constructTree();
                finalEnv.setSaved();
                Sapphire.setActiveObject(finalEnv);
                Diamond.setCurrentEnv(finalEnv);

                // The source environment is discarded
                loaded.destroy();
            }
        } catch (Exception e) {
            DiaLogger.log(EnvironmentSerializer.class, "Failed to load environment from '" + path + "'\n\t" + e, DiaLoggerLevel.ERROR);
            return null;
        }
        return finalEnv;
    }
}
