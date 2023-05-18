package diamondEngine.diaUtils;

import diamondEngine.Template;
import diamondEngine.diaAudio.Sound;
import diamondEngine.diaEvents.DiaEvent;
import diamondEngine.diaEvents.DiaEventType;
import diamondEngine.diaEvents.DiaEvents;
import diamondEngine.diaRenderer.Shader;
import diamondEngine.diaRenderer.Texture;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DiaAssetManager {

    // ATTRIBUTES
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Sound> sounds = new HashMap<>();
    private static Map<String, Template> templates = new HashMap<>();

    // METHODS
    /**
     * Anonymously loads a single resource form a file path
     * @return True if resource was loaded and false if not
     */
    public static boolean loadResource(String path) {
        if (path != null && !path.isEmpty()) {
            String fileExtension = path.substring(path.lastIndexOf('.')).toLowerCase();
            File resource = new File(path);
            if (resource.exists() && resource.isFile()) {
                switch (fileExtension) {
                    case ".wav":
                    case ".mp3":
                    case ".ogg":
                        getSound(path);
                        break;
                    case ".png":
                    case ".jpg":
                    case ".jpeg":
                        getTexture(path);
                        break;
                    case ".glsl":
                        break;
                    default:
                        DiaLogger.log(DiaAssetManager.class, "Resource type '" + fileExtension + "' not supported '" + path + "'", DiaLoggerLevel.WARN);
                        return false;
                }
            } else {
                DiaLogger.log(DiaAssetManager.class, "Resource does not exist '" + path + "'", DiaLoggerLevel.ERROR);
                return false;
            }
        } else {
            DiaLogger.log(DiaAssetManager.class, "Invalid path '" + path + "'", DiaLoggerLevel.ERROR);
            return false;
        }
        return true;
    }

    /**
     * Tries to remove an asset form the loaded assets
     * @param path Path/id of the asset to remove
     * @return True if asset was removed
     */
    public static boolean removeResource(String path) {
        boolean wasRemoved = false;
        if (shaders.remove(path) != null) wasRemoved = true;
        if (textures.remove(path) != null) wasRemoved = true;
        if (sounds.remove(path) != null) wasRemoved = true;
        if (templates.remove(path) != null) wasRemoved = true;
        if (wasRemoved) DiaEvents.notify(new DiaEvent(DiaEventType.ASSET_REMOVED, path));
        return wasRemoved;
    }

    public static Shader addShader(String name, String path) {
        File file = new File(path);
        if (DiaAssetManager.shaders.containsKey(file.getAbsolutePath())) {
            return DiaAssetManager.shaders.get(name);
        } else {
            Shader shader = new Shader(name, path);
            shader.compile();
            DiaAssetManager.shaders.put(file.getAbsolutePath(), shader);
            DiaEvents.notify(new DiaEvent(DiaEventType.ASSET_ADDED));
            return shader;
        }
    }

    public static Collection<Texture> getAllTextures() {
        return textures.values();
    }

    public static Texture getTexture(String path) {
        File file = new File(path);
        if (textures.containsKey(file.getAbsolutePath())) {
            return textures.get(file.getAbsolutePath());
        } else {
            Texture texture = new Texture();
            texture.load(path);
            textures.put(file.getAbsolutePath(), texture);
            DiaEvents.notify(new DiaEvent(DiaEventType.ASSET_ADDED, texture));
            return texture;
        }
    }

    public static Collection<Sound> getAllSounds() {
        return sounds.values();
    }

    public static Sound getSound(String path)    {
        File file = new File(path);
        if (sounds.containsKey(file.getAbsolutePath())) {
            return sounds.get(file.getAbsolutePath());
        } else {
            Sound sound = new Sound(file.getAbsolutePath(), false);
            sounds.put(file.getAbsolutePath(), sound);
            DiaEvents.notify(new DiaEvent(DiaEventType.ASSET_ADDED, sound));
            return sound;
        }
    }
}
