package diamondEngine.diaUtils.serializers;

import com.google.gson.*;
import diamondEngine.Diamond;
import diamondEngine.Entity;
import diamondEngine.Environment;
import diamondEngine.diaComponents.Component;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import sapphire.Sapphire;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EnvironmentSerializer {

    // ATTRIBUTES
    private Environment env;

    // CONSTRUCTORS
    public EnvironmentSerializer(Environment env) {
        this.env = env;
    }

    // SERIALIZATION METHODS
    public void save(String path) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentSerializer(env))
                .registerTypeAdapter(Entity.class, new EntitySerializer(env))
                .enableComplexMapKeySerialization()
                .create();

        try {
            env.setOriginFile(path);
            FileWriter writer = new FileWriter(path);
            writer.write(gson.toJson(env));
            writer.close();
            env.setSaved();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads an environment from a file passed as a parameter
     *
     * @param path Path of the environment file to load the new environment from
     * @return The newly loaded environment
     */
    public Environment load(String path) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentSerializer(env))
                .registerTypeAdapter(Entity.class, new EntitySerializer(env))
                .enableComplexMapKeySerialization()
                .create();
        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get(path)));
            if (!inFile.equals("")) {
                Environment loaded = gson.fromJson(inFile, Environment.class);
                env.setName(loaded.getName());
                env.setOriginFile(path);
                if (loaded.getChildren() != null) for (Environment e : loaded.getChildren()) env.addChild(e);
                if (loaded.getEntities() != null)for (Entity e : loaded.getEntities()) env.addEntity(e);
                env.setSaved();
                Sapphire.setActiveObject(env);
                Diamond.setCurrentEnv(env);
            }
        } catch (Exception e) {
            DiaLogger.log(EnvironmentSerializer.class, "Failed to load environment from '" + path + "'\n\t" + e.getMessage(), DiaLoggerLevel.ERROR);
            return null;
        }
        return env;
    }
}
